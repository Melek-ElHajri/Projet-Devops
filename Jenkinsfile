pipeline {
    agent any

    environment {
        dockerhub_token = credentials('dockerhub_token')
        notify_token = credentials('NOTIFY_TOKEN')
    }

    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    stages {
        stage('GIT') {
            steps {
                git branch: 'ElHedi-Melek-Elhajri',
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
        stage('Security Scan: OWASP Dependency-Check') {
            steps {
                script {
                    echo "Starting OWASP Dependency-Check..."
                    sh 'mvn org.owasp:dependency-check-maven:check'
                }
            }
        }
        stage('JaCoCo Report') {
            steps {
                sh 'mvn jacoco:report'
            }
        }

       stage('Testing - JaCoCo Report Generation') {
            steps {
                script {
                    jacoco(
                        execPattern: '**/target/jacoco.exec',
                        classPattern: '**/target/classes',
                        sourcePattern: '**/src/main/java'
                    )
                }
            }
        }


        stage('Scan') {
            steps {
                // Check if the SonarQube container is running, start it if not
                sh '''
                    if ! docker ps | grep 656251e296fb > /dev/null; then
                        echo "SonarQube container is not running. Starting SonarQube container..."
                        docker start 656251e296fb
                        sleep 30  # Wait for the container to be fully up
                    else
                        echo "SonarQube container is already running."
                    fi
                '''
                
                // Run the SonarQube scan
                withSonarQubeEnv('sq') {
                    sh 'mvn sonar:sonar'
                }
            }
        }
        
        stage('Deploy to Nexus') {
            steps {
                // Check if the container is running, start it if not
                sh '''
                    if ! docker ps | grep a5b6a466786c > /dev/null; then
                        echo "Container is not running. Starting container..."
                        docker start a5b6a466786c
                        sleep 35  # Wait for the container to be fully up
                    else
                        echo "Container is already running."
                    fi
                '''
                
                sh 'mvn deploy -DskipTests -DaltDeploymentRepository=deploymentRepo::default::http://192.168.10.2:8081/repository/maven-releases/'
            }
        }

        // Uncomment these stages if you want to generate and push a Docker image
        
        stage("Generate Docker Image") {
            steps {
                //sudo chmod 666 /var/run/docker.sock
                sh 'docker build -t m2l2k/tp-foyer:5.0.0 .'
            }
        }

        stage("Push Docker Image") {
            steps {
                sh "echo ${dockerhub_token} | docker login -u m2l2k --password-stdin" 
                sh "docker push m2l2k/tp-foyer:5.0.0"
            }
        }

        stage('Docker Compose') {
            steps {
                sh 'docker-compose down'
                sh 'docker compose up -d'
            }
        }
        
        stage('Start Monitoring Containers') {
            steps {
                sh 'docker start 4223e0421a91'
                sh 'docker start cf099f77ec8b'
            }
        }
    }

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
