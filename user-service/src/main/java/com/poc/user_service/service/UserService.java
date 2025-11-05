package com.poc.user_service.service;

import com.poc.user_service.clients.OrderClient;
import com.poc.user_service.model.User;
import com.poc.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final OrderClient orderClient;

    @Autowired
    public UserService(UserRepository userRepository, OrderClient orderClient){
        this.userRepository=userRepository;
        this.orderClient=orderClient;
    }

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public User createUser(User user){
        return userRepository.save(user);
    }

    public Map<String, Object> getUserWithOrders(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        List<Map<String, Object>> orders = orderClient.getOrdersByUserId(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("user", user);
        response.put("orders", orders);
        return response;
    }
}
