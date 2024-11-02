pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'  // Adjust if necessary
        maven 'M2_HOME'  // Adjust if necessary
    }

    stages {
        stage('GIT') {
            steps {
                git branch: 'NouhaSedraoui', url: 'https://github.com/Melek-ElHajri/Projet-Devops.git'
            }
        }

        // Uncomment and adjust the following stages as needed
        /*
        stage('Compile') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'sudo docker build -t rymasd29/tp-foyer:5.0.0 .'
            }
        }

        stage('Push Docker Image to DockerHub') {
            steps {
                sh '''
                    sudo docker login -u rymasd29 -p 223JFT4309
                    sudo docker push rymasd29/tp-foyer:5.0.0
                '''
            }
        }

        stage('Run Docker Compose') {
            steps {
                script {
                    sh '''
                        sudo docker-compose down 
                        sudo docker-compose up -d
                    '''
                }
            }
        }
*/
        stage('Run Docker Compose for ELK Stack') {
            steps {
                script {
                    sh '''
                        sudo docker-compose -f docker-compose-elk.yml down 
                        sudo docker-compose -f docker-compose-elk.yml up -d
                    '''
                }
            }
        }
        

      /*  stage('Check and Start Prometheus') {
            steps {
                script {
                    def prometheusRunning = sh(script: 'docker ps -q -f name=prometheus', returnStdout: true).trim()
                    if (prometheusRunning) {
                        echo 'Prometheus is already running.'
                    } else {
                        echo 'Starting Prometheus container...'
                        sh 'docker start prometheus'
                    }
                }
            }
        }

        stage('Check and Start Grafana') {
            steps {
                script {
                    def grafanaRunning = sh(script: 'docker ps -q -f name=grafana', returnStdout: true).trim()
                    if (grafanaRunning) {
                        echo 'Grafana is already running.'
                    } else {
                        echo 'Starting Grafana container...'
                        sh 'docker start grafana'
                    }
                }
            }
        }

        stage('Validate Setup') {
            steps {
                script {
                    echo 'Validating Prometheus and Grafana setup...'
                    sh 'curl -f http://localhost:9090/ || echo "Prometheus is not accessible"'
                    sh 'curl -f http://localhost:3000/ || echo "Grafana is not accessible"'
                }
            }
        }
*/
        stage('Verify Logstash and Elasticsearch') {
            steps {
                script {
                    // Check if Logstash is running on port 5000
                    def logstashStatus = sh(script: "curl -s -o /dev/null -w '%{http_code}' http://logstash:5000", returnStdout: true).trim()

                    if (logstashStatus == '200') {
                        echo 'Logstash is running and reachable on port 5000.'
                    } else {
                        error 'Logstash is not reachable on port 5000.'
                    }

                    // Query Elasticsearch to verify logs are present
                    def logs = sh(script: "curl -s -X GET 'http://elasticsearch:9200/devops-logs-*/_search?pretty'", returnStdout: true).trim()

                    // Check if logs contain expected data
                    if (logs.contains('Build successful:')) {
                        echo 'Logs are present in Elasticsearch.'
                    } else {
                        error 'No logs found in Elasticsearch.'
                    }
                }
            }
        }
    }
}
