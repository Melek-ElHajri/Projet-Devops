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
                script {
                    // Check out the latest code from the branch again before compiling
                    checkout scm
                }
                sh 'mvn clean compile'
            }
        }

        stage('Scan') {
            steps {
                script {
                    // Check out the latest code from the branch again before compiling
                    checkout scm
                }
                withSonarQubeEnv('sq1') {
                    sh 'mvn sonar:sonar'
                }
            }
        }
    }
}
