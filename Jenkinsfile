pipeline {
    agent any

    environment {
        // Credentials and environment setup
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-creds')
        GITHUB_CREDENTIALS = credentials('github-creds')
        USER_IMAGE = "vipashyna07654/user-service"
        ORDER_IMAGE = "vipashyna07654/order-service"
        KUBECONFIG = 'C:\\ProgramData\\Jenkins\\.kube\\config'
    }

    stages {
        stage('Checkout') {
            steps {
                echo "Checking out code..."
                git branch: 'dev', credentialsId: 'github-creds', url: 'https://github.com/VipaCitrus/microservices-poc.git'
            }
        }

        stage('Build JARs') {
            steps {
                echo "Building JARs..."
                dir('user-service') {
                    bat 'mvn clean package -DskipTests'
                }
                dir('order-service') {
                    bat 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                echo " Building Docker images..."
                bat 'docker build -t %USER_IMAGE%:latest ./user-service'
                bat 'docker build -t %ORDER_IMAGE%:latest ./order-service'
            }
        }

        stage('Push to Docker Hub') {
            steps {
                echo " Pushing images to Docker Hub..."
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
                echo "Deploying to Minikube..."
                bat '''
                kubectl config use-context minikube
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
            echo "Deployment successful!"
        }
        failure {
            echo "Build failed!"
        }
    }
}
