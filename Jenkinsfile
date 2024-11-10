pipeline {
    agent any
    
    environment {
        SONAR_TOKEN = 'squ_65eb01a1246ad720ee511a4d5d0bce064014'
    }
    
    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    stages {
        stage('GIT') {
            steps {
                git branch: 'Rjeibi-Hazem',
                    url: 'https://github.com/Melek-ElHajri/Projet-Devops.git'
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean install compile'
            }
        }
        stage('JUnit/Mockito Tests') {
            steps {
                sh 'mvn test' 
            }
        }

        stage('Scan') {
            steps {
                // Check if the SonarQube container is running, start it if not
                sh '''
                    if ! docker ps | grep 055ccab75690 > /dev/null; then
                        echo "SonarQube container is not running. Starting SonarQube container..."
                        docker start 055ccab75690
                        sleep 20  # Wait for the container to be fully up
                    else
                        echo "SonarQube container is already running."
                    fi
                '''
                
                // Run the SonarQube scan
                withSonarQubeEnv('snrq') {
                    sh 'mvn sonar:sonar -Dsonar.login=$SONAR_TOKEN'
                }
            }
        }
