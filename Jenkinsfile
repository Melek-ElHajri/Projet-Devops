pipeline {
    agent any
    
    environment {
        //SONAR_TOKEN = 'squ_65eb01a1246ad720ee511a4d5d0bce064014'
       // SONAR_TOKEN = credentials('SONAR_TEXT')
        dockerhub_token = credentials('dockerhub_token')
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

        // Quick Nmap Scan stage targeting google.com
        stage('Quick Nmap Scan') {
            steps {
                script {
                    def targetHost = 'http://192.168.23.133:8080'  // Scanning google.com
                    
                    echo "Running quick Nmap scan on ${targetHost}"
                    
                    // Quick scan (first 1000 ports)
                    sh "nmap -p 1-1000 -T4 -n -Pn ${targetHost} -oN nmap_quick_scan_report.txt"
                }
            }
            post {
                always {
                    // Archive the Nmap scan results as Jenkins artifacts
                    archiveArtifacts artifacts: 'nmap_quick_scan_report.txt', allowEmptyArchive: true
                    echo "Quick Nmap scan report has been archived."
                }
            }
        }


           stage("Generate Docker Image") {
            steps {
                //sudo chmod 666 /var/run/docker.sock
                sh 'docker build -t rh1337/tp-foyer:5.0.0 .'
            }
        }

        stage("Push Docker Image") {
            steps {
                sh "echo ${dockerhub_token} | docker login -u rh1337 --password-stdin" 
                sh "docker push rh1337/tp-foyer:5.0.0"
            }
        }

        stage('Docker Compose') {
            steps {
                sh 'docker compose up -d'
            }
        }
    

    
    }
}
