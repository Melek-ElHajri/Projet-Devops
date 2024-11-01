pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'  // Adjust if necessary
        maven 'M2_HOME'  // Adjust if necessary
    }

    stages {
        stage('GIT') {
            steps {
                git branch: 'NouhaSedraoui',
                    url: 'https://github.com/Melek-ElHajri/Projet-Devops.git'
            }
        }

        stage('Compile') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('List Workspace') {
            steps {
                sh 'ls -l'
                sh 'ls -l target'
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

        stage('Run Docker Compose for Application') {
            steps {
                script {
                    sh '''
                        sudo docker-compose down -v
                        sudo docker-compose up -d
                    '''
                }
            }
        }

        stage('Run Docker Compose for ELK Stack') {
            steps {
                script {
                    sh '''
                        sudo docker-compose -f docker-compose-elk.yml down -v
                        sudo docker-compose -f docker-compose-elk.yml up -d
                    '''
                }
            }
        }
    }

    post {
        always {
            script {
                // Clean up both stacks after the pipeline run
                sh 'sudo docker-compose down -v'
                sh 'sudo docker-compose -f ocker-compose-elk.yml down -v'
            }
        }
    }
}
