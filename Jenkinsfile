pipeline {
    agent any

    stages {
        stage('Checkout Git') {
            steps {
                // Checkout the code from the new repository
                git credentialsId: 'cred-github', 
                    branch: 'nourhene-chammakhi', // Change branch name
                    url: 'https://github.com/Melek-ElHajri/Projet-Devops.git' // New repository
            }
        }

        stage('Compiling') {
            steps {
                // Compile the project using Maven
                sh 'mvn clean compile'
            }
        }

        stage('SonarQube') {
            steps {
                // Run SonarQube analysis using Maven
                sh 'mvn sonar:sonar -Dsonar.login=admin -Dsonar.password=201JFT3926nourhene*'
            }
        }

        stage('Deploy to Nexus') {
            steps {
                // Deploy to Nexus repository
                sh 'mvn deploy'
            }
        }
         stage("Generate Docker Image") {
            steps {
                sh 'docker build -t m2l2k/tp-foyer:5.0.0 .'
            }
        }

        stage("Push Docker Image") {
            steps {
                sh "echo ${dockerhub_token} | docker login -u m2l2k --password-stdin" 
                sh "docker push m2l2k/tp-foyer:5.0.0"
            }
        }

        stage('Docker Compose') {
            steps {
                sh 'docker compose up -d'
            }
        }
        
    }
}
