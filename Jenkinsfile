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

       
        
       /* stage('Build Docker Image') {
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
        }*/
       stage('Install Node.js and Angular CLI') {
    steps {
        // Install Node.js version 18
        sh 'curl -sL https://deb.nodesource.com/setup_18.x | sudo -E bash -'
        sh 'apt-get install -y nodejs'

        // Install npm (in case it's not installed correctly with Node.js)
        sh 'apt-get install -y npm'

        // Install Angular CLI globally
        sh 'npm install -g @angular/cli'
    }
}

        stage('DOCKER DEPLOY FRONTEND') {
            steps {
                sh '''
                    sudo docker login -u rymasd29 -p 223JFT4309
                    sudo docker push rymasd29/front_angular:latest
                '''
            }
        }
/*
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

        stage('Check and Start Prometheus') {
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
    }
  

}
