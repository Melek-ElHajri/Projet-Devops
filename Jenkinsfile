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
        
        stage('Compile Stage') {   // Move the compile stage before the scan
            steps {
                sh 'mvn clean compile'
            }
        }
        
        stage('Scan') {
            steps {
                withSonarQubeEnv('sq1') {
                    // Add sonar.java.binaries property to point to compiled classes
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Deploy to Nexus') {  // Add the deployment stage
            steps {
                sh 'mvn deploy -DskipTests'
            }
        }
    }
}
