pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    stages {
        stage('GIT') {
            steps {
                git branch: 'nourhene',
                    url: 'https://github.com/Melek-ElHajri/Projet-Devops.git'
            }
        }
        stage('Scan') {
            steps {
                withSonarQubeEnv('nourhene-sq') {
                    sh 'mvn sonar:sonar' 
                }
            }
        }
        stage('Compile Stage') {
            steps {
                sh 'mvn clean compile'
            }
        }
    }
}
