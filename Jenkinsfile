pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'  // Adjust i0
        maven 'M2_HOME'  // Adjust if nec
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

      /*
        stage('Package') {
            steps {
                sh 'mvn package'
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
                         sudo docker-compose up -d
                     ''' 
                 }
             }
         }*/
    }


}
