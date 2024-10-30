pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    stages {
        stage('GIT') {
            steps {
                git branch: 'Gabsi-Rim',
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
                sh 'mvn deploy'
            }
        }
        stage('Scan') {
            steps {
                withSonarQubeEnv('rim-sonarqube') {
                    // Add sonar.java.binaries property to point to compiled classes
                    sh 'mvn sonar:sonar'
                }
            }
        }

        
    }
}
