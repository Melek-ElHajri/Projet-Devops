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
               withCredentials([string(credentialsId: 'sq1', variable: 'SONAR_TOKEN')]) {
                    sh "mvn sonar:sonar -Dsonar.projectKey=JenkinsFile -Dsonar.host.url=http://192.168.33.10:9000 -Dsonar.login=$SONAR_TOKEN"
                }
            }
        }
    }
}
