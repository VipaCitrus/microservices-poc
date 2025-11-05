pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-creds')
        GITHUB_CREDENTIALS = credentials('github-creds')
        USER_IMAGE = "vipashynasharma/user-service"
        ORDER_IMAGE = "vipashynasharma/order-service"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'dev', credentialsId: 'github-creds', url: 'https://github.com/VipaCitrus/microservices-poc.git'
            }
        }

        stage('Build Docker Images') {
            steps {
                bat 'docker build -t %USER_IMAGE%:latest ./user-service'
                bat 'docker build -t %ORDER_IMAGE%:latest ./order-service'
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    bat '''
                    echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin
                    docker push %USER_IMAGE%:latest
                    docker push %ORDER_IMAGE%:latest
                    '''
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                bat '''
                kubectl apply -f k8s\\mysql-user\\
                kubectl apply -f k8s\\mysql-order\\
                kubectl apply -f k8s\\user-service\\
                kubectl apply -f k8s\\order-service\\
                '''
            }
        }
    }

    post {
        success {
            echo "✅ Deployment successful!"
        }
        failure {
            echo "❌ Build failed!"
        }
    }
}
