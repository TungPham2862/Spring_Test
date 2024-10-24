pipeline {
    agent any

    environment {
        RENDER_API_URL = "https://api.render.com/deploy/srv-csc9os8gph6c73bov4h0?key=td5OIBZfhCM" // Thay <SERVICE_ID> và <API_KEY> với thông tin của bạn
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

        stage('Push to GitHub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'github-access-token', usernameVariable: 'GITHUB_USER', passwordVariable: 'GITHUB_TOKEN')]) {
                        bat '''
                            git config --global user.name "${GITHUB_USER}"
                            git config --global user.email "your_email@example.com"
                            git add .
                            git commit -m "Deploy new version from Jenkins"
                            git push https://${GITHUB_USER}:${GITHUB_TOKEN}@github.com/TungPham2862/Spring_Test.git deploy
                        '''
                    }
                }
            }
        }

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