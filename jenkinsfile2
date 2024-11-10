pipeline {
    agent any
    
    environment {
        SONAR_TOKEN = credentials('SONAR_TOKEN')
        dockerhub_token = credentials('dockerhub_token')
    }
    
    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    stages {
        stage('GIT') {
            steps {
                git branch: 'Dhaoui-Badreddine',
                    url: 'https://github.com/Melek-ElHajri/Projet-Devops.git'
            }
        }

        stage('Pre-commit Security Hooks') {
            steps {
                script {
                    def result = sh(script: '''
                        if ! command -v pre-commit &> /dev/null; then
                            echo "pre-commit is not installed, installing in a virtual environment..."
                            python3 -m venv venv
                            . venv/bin/activate
                            pip install pre-commit
                        else
                            echo "pre-commit is already installed."
                        fi
                        git config --unset-all core.hooksPath
                        pre-commit install
                        pre-commit run --all-files
                        deactivate
                    ''', returnStatus: true)

                    if (result != 0) {
                        echo "Pre-commit hooks did not pass, but continuing pipeline."
                    } else {
                        echo "Pre-commit hooks passed successfully."
                    }
                }
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install compile'
            }
        }

        stage('Quick Nmap Scan') {
            steps {
                script {
                    def targetHost = '192.168.10.2'  // Scanning 192.168.10.2

                    echo "Running quick Nmap scan on ${targetHost}:8089"

                    // Quick scan on the specified IP and port 8089
                    sh "nmap -p 8089 -T4 -n -Pn ${targetHost} -oN nmap_quick_scan_report.txt"
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
        
        /*stage('Build') {
            steps {
                sh 'mvn clean install compile'
            }
        }*/
        stage('JUnit/Mockito Tests') {
            steps {
                sh 'mvn test' 
            }
        }

       /* stage('Scan') {
            steps {
                // Check if the SonarQube container is running, start it if not
                sh '''
                    if ! docker ps | grep 5dc45f66b119 > /dev/null; then
                        echo "SonarQube container is not running. Starting SonarQube container..."
                        docker start 5dc45f66b119
                        sleep 20  # Wait for the container to be fully up
                    else
                        echo "SonarQube container is already running."
                    fi
                '''
                
                // Run the SonarQube scan
                withSonarQubeEnv('sq') {
                    sh 'mvn sonar:sonar -Dsonar.login=$SONAR_TOKEN'
                }
            }
        }*/
        
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
        
        /*stage("Generate Docker Image") {
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
        }*/
        
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
