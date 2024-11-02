pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }
/**
    stages {
        stage('GIT') {
            steps {
                git branch: 'Gabsi-Rim',
                    url: 'https://github.com/Melek-ElHajri/Projet-Devops.git'
            }
        }
**/
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/Gabsi-Rim']], 
                          userRemoteConfigs: [[url: 'https://github.com/Melek-ElHajri/Projet-Devops.git']]])
            }
        }
    
        stage('Compile Stage') {   
            steps {
                sh 'mvn clean compile'
            }
        }
        
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
        stage("Quality Gate") {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true 
                }
            }
        }
    

        stage('Build') {
            steps {
                sh 'mvn clean package'
                sh 'ls target'
            }
        }

        stage('Build Docker Image') {
            steps {  
                sh "docker build -t gabsirim/alpine:1.0.0 ."
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials-id', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        sh "echo \$DOCKER_PASSWORD | docker login -u \$DOCKER_USERNAME --password-stdin"
                    }
                    sh 'docker push gabsirim/alpine:1.0.0'
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                script {
                    sh 'ls -la'
                    sh 'docker compose -f ./docker-compose.yml up -d'
                }
            } 
        }
    }
}
