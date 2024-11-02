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
                sh 'mvn clean package'
            }}
        

       

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
}
        
    

   

