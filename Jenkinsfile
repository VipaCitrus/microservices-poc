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
                git branch: 'dev', credentialsId: "${GITHUB_CREDENTIALS}", url: 'https://github.com/<your-username>/<your-repo>.git'
            }
        }

        stage('Build Docker Images') {
            steps {
                sh 'docker build -t ${USER_IMAGE}:latest ./user-service'
                sh 'docker build -t ${ORDER_IMAGE}:latest ./order-service'
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh 'echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin'
                    sh 'docker push ${USER_IMAGE}:latest'
                    sh 'docker push ${ORDER_IMAGE}:latest'
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh '''
                kubectl apply -f k8s/mysql-user/
                kubectl apply -f k8s/mysql-order/
                kubectl apply -f k8s/user-service/
                kubectl apply -f k8s/order-service/
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
