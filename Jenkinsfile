pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    stages {
        stage('GIT') {
            steps {
                git branch: 'NouhaSedraoui',
                    url: 'https://github.com/Melek-ElHajri/Projet-Devops.git'
            }
        }

        stage('Compile Stage') {
            steps {
                sh 'mvn clean compile'
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
                        sudo docker-compose down -v
                        sudo docker-compose up -d
                    '''
                }
            }
        }

        // Uncomment these stages as needed
        /*
        stage('Deploy to Nexus') {
            steps {
                sh 'mvn deploy -DskipTests -DaltDeploymentRepository=deploymentRepo::default::http://192.168.33.10:8081/repository/maven-releases/'
            }
        }
        
        stage('Scan') {
            steps {
                withSonarQubeEnv('sq1') {
                    sh 'mvn sonar:sonar'
                }
            }
        }
        */
    }
}
