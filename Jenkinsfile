pipeline {
    agent any

    environment {
        RENDER_API_URL = "https://api.render.com/deploy/srv-csc9os8gph6c73bov4h0?key=td5OIBZfhCM" // Thay <SERVICE_ID> và <API_KEY> với thông tin của bạn
        GITHUB_USER = 'TungPham2862'
        GITHUB_TOKEN = credentials('github-access-token')
    }

    stages {
        stage('Checkout') {
            steps {
                // Lấy mã nguồn từ Git
                checkout scm
            }
        }

        stage('Build') {
            steps {
                // Biên dịch dự án bằng Maven
                bat 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
                // Chạy unit tests
                bat 'mvn test'
            }
        }

        stage('Build Docker Image') {
                    steps {
                        script {

                            def imageName = 'tungpham286/spring-quiz-app'
                            def imageTag = 'latest' // You can use a version tag or a timestamp here

                            // Build Docker image
                            bat "docker build -t ${imageName}:${imageTag} ."
                        }
                    }
                }

                stage('Push Docker Image') {
                    steps {
                        script {
                            // Login to Docker Hub
                            withCredentials([usernamePassword(credentialsId: 'dockerhub-credential', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                                bat "docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}"

                                // Push the Docker image
                                bat "docker push ${imageName}:${imageTag}"
                            }
                        }
                    }
                }

//         stage('Build and Commit') {
//                     steps {
//                         script {
//                         withCredentials([gitUsernamePassword(credentialsId: 'github-access-token', gitToolName: 'Default')]) {
//                         bat '''
//                             git pull --rebase origin deploy
//                             git add .
//                             git commit -m "Deploy new version from Jenkins"
//                             git push -u origin deploy
//                         '''
//
//
//                         }
//                         }
//                     }
//                 }

        stage('Deploy to Render') {
            steps {
                script {
                    // Gửi yêu cầu đến Render API để triển khai bằng curl
                    def response = bat(script: """
                        curl -X POST "https://api.render.com/deploy/srv-csc9os8gph6c73bov4h0?key=td5OIBZfhCM" -H "Content-Type: application/json"
                    """, returnStdout: true).trim()

                    echo "Deployment response: ${response}"

                    // Kiểm tra phản hồi
                    if (response.contains("error")) { // Thay đổi điều kiện kiểm tra theo nội dung phản hồi thực tế
                        error "Deployment failed with response: ${response}"
                    } else {
                        echo 'Deployment succeeded!'
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}