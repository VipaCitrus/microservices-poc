package com.poc.user_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(name="order-service", url= "http://order-service-container:8082")
public interface OrderClient {

    @GetMapping("/orders/user/{userId}")
    List<Map<String, Object>> getOrdersByUserId(@PathVariable("userId") Long userId);
}
