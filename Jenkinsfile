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

        /*stage('Pre-commit Security Hooks') {
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
        }*/

        stage('Build') {
            steps {
                sh 'mvn clean install compile'
            }
        }

        /*stage('Quick Nmap Scan') {
            steps {
                script {
                    def targetHost = '192.168.10.2'  // Scanning 192.168.10.2

                    echo "Running quick Nmap scan on ${targetHost}:8089"

                    sh "nmap -p 8089 -T4 -n -Pn ${targetHost} -oN nmap_quick_scan_report.txt"
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: 'nmap_quick_scan_report.txt', allowEmptyArchive: true
                    echo "Quick Nmap scan report has been archived."
                }
            }
        }*/
        stage('JUnit/Mockito Tests') {
            steps {
                sh 'mvn test' 
            }
        }

        stage('Scan') {
            steps {
                withSonarQubeEnv('sq') {
                    sh 'mvn sonar:sonar -Dsonar.login=$SONAR_TOKEN'
                }
            }
        }
        
        /*stage('Deploy to Nexus') {
            steps {
                sh 'mvn deploy -DskipTests -DaltDeploymentRepository=deploymentRepo::default::http://192.168.10.2:8081/repository/maven-releases/'
            }
        }*/

        
        
        stage("Generate Docker Image") {
            steps {
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
        
        /*stage('Start Monitoring Containers') {
            steps {
                sh 'docker start 951fdb0907b5'
                sh 'docker start d8b1e80d7f3a'
            }
        }
    }*/
    }
}
