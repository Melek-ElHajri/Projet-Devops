pipeline {
    agent any
  /*  
    environment {
        SONAR_TOKEN = 'squ_65eb01a1246ad720ee511a4d5d0bce064014'
       // SONAR_TOKEN = credentials('SONAR_TEXT')
       // dockerhub_token = credentials('dockerhub_token')
    }*/
    
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

        // Nmap Scan stage targeting google.com
        stage('Nmap Scan') {
            steps {
                script {
                    def targetHost = 'google.com'  // Scanning google.com
                    
                    echo "Running Nmap scan on ${targetHost}"
                    
                    // Basic Nmap port scan across all ports
                    sh "nmap -p 1-65535 ${targetHost} -oN nmap_scan_report.txt"
                    
                    // Detailed Nmap scan (service version and OS detection)
                    sh "nmap -sV -O ${targetHost} -oN nmap_detailed_report.txt"
                }
            }
            post {
                always {
                    // Archive the Nmap scan results as Jenkins artifacts
                    archiveArtifacts artifacts: 'nmap_*.txt', allowEmptyArchive: true
                    echo "Nmap scan reports have been archived."
                }
            }
        }
     /*   stage('Scan') {
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
        
        /*stage('Deploy to Nexus') {
            steps {
                // Check if the container is running, start it if not
                sh '''
                    if ! docker ps | grep 4f5ed7dc04f8 > /dev/null; then
                        echo "Container is not running. Starting container..."
                        docker start 4f5ed7dc04f8
                        sleep 30  # Wait for the container to be fully up
                    else
                        echo "Container is already running."
                    fi
                '''
                
                sh 'mvn deploy -DskipTests -DaltDeploymentRepository=deploymentRepo::default::http://192.168.10.2:8081/repository/maven-releases/'
            }
        }*/

        // Uncomment these stages if you want to generate and push a Docker image
     /*   
        stage("Generate Docker Image") {
            steps {
                //sudo chmod 666 /var/run/docker.sock
                sh 'docker build -t badredinedhaoui/tp-foyer:5.0.0 .'
            }
        }

        stage("Push Docker Image") {
            steps {
                sh "echo ${dockerhub_token} | docker login -u badredinedhaoui --password-stdin" 
                sh "docker push badredinedhaoui/tp-foyer:5.0.0"
            }
        }

        stage('Docker Compose') {
            steps {
                sh 'docker compose up -d'
            }
        }
        */
        /*stage('Start Monitoring Containers') {
            steps {
                sh 'docker start 4223e0421a91'
                sh 'docker start cf099f77ec8b'
            }
        }
    }*/

    // Uncomment the post block if you want notifications
    /*
    post {
        success {
            script {
                notifyEvents message: "<b>Build Success</b> - Job: ${env.JOB_NAME}, Build Number: ${env.BUILD_NUMBER}", 
                             token: env.notify_token
            }
        }
        
        failure {
            script {
                notifyEvents message: "<b>Build Failed</b> - Job: ${env.JOB_NAME}, Build Number: ${env.BUILD_NUMBER}", 
                             token: env.notify_token
            }
        }
    }
    */
    }
}
