pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    stages {
        stage('GIT') {
            steps {
                git branch: 'nourhene-chammakhi',
                    url: 'https://github.com/Melek-ElHajri/Projet-Devops.git'
            }
        }

        stage('Compile Stage') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Deploy to Nexus') {
            steps {
                sh 'mvn deploy -DskipTests -DaltDeploymentRepository=deploymentRepo::default::http://192.168.56.10:8081/repository/maven-releases/'
            }
        }

        stage('SonarQube') {
            steps {
                // Run SonarQube analysis using Maven
                sh 'mvn sonar:sonar -Dsonar.login=admin -Dsonar.password=201JFT3926nourhene*'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package' // This will create the JAR in the target directory
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t nourhenenc/alpine:1.0.0 .'
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials-id', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        sh "echo \$DOCKER_PASSWORD | docker login -u \$DOCKER_USERNAME --password-stdin"
                    }
                    sh 'docker push nourhenenc/alpine:1.0.0'
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                script {
                    sh 'ls -la' // Confirm files are in the right directory
                    sh 'docker compose -f ./docker-compose.yml up -d'
                }
            }
        }

        // Optional: Stage to expose Prometheus metrics if needed
        stage('Expose Prometheus Metrics') {
            steps {
                script {
                    // Prometheus already scrapes Jenkins metrics at http://192.168.56.10:8080/prometheus
                    // You can expose other additional metrics here if required
                    // For example, you can add a custom Prometheus endpoint, but this depends on your Jenkins setup
                    sh 'curl http://192.168.56.10:8080/prometheus' // Verify if Prometheus endpoint is accessible
                }
            }
        }
    }
}
