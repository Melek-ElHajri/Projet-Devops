pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'  // Adjust if necessary
        maven 'M2_HOME'  // Adjust if necessary
    }

    environment {
        REPORT_PATH = '/var/lib/jenkins/workspace/sonar/reports/dependency-check-report.xml'
    }

    stages {
        stage('GIT') {
            steps {
                git branch: 'NouhaSedraoui', url: 'https://github.com/Melek-ElHajri/Projet-Devops.git'
            }
        }

        stage('Clean') {
            steps {
                sh 'mvn clean'
            }
        }

        stage('Compile') {
            steps {
                sh 'mvn compile'
            }
        }

        stage('Run Tests') {
            steps {
                sh 'mvn test'
                sh 'ls -R target/site/jacoco || echo "JaCoCo report directory not found"'
            }
        }

        stage('JaCoCo Report') {
            steps {
                script {
                    // Publish the JaCoCo code coverage report to Jenkins
                    jacoco(
                        execPattern: '**/target/jacoco.exec', // Path to JaCoCo exec file
                        classPattern: '**/target/classes',    // Path to compiled classes
                        sourcePattern: '**/src/main/java'     // Path to source code
                    )
                }
            }
        }

        stage('Dependency Check') {
            steps {
                // Run Dependency-Check analysis
                dependencyCheck additionalArguments: '--failOnCVSS 7 --out /var/lib/jenkins/workspace/sonar/reports/ --noupdate', 
                               odcInstallation: 'Dependency-Check'

                // Debugging: List the files in the reports directory to ensure the report is generated
                sh 'ls -R /var/lib/jenkins/workspace/sonar/reports'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package'
            }
        }

        stage('Sonar') {
            steps {
                withSonarQubeEnv('sq1') {
                    sh 'mvn sonar:sonar -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
                }
            }
        }

        // Uncomment the following stages if needed for Docker operations
        /*
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
                        sudo docker-compose down 
                        sudo docker-compose up -d
                    '''
                }
            }
        }

        stage('Check and Start Prometheus') {
            steps {
                script {
                    def prometheusRunning = sh(script: 'docker ps -q -f name=prometheus', returnStdout: true).trim()
                    if (prometheusRunning) {
                        echo 'Prometheus is already running.'
                    } else {
                        echo 'Starting Prometheus container...'
                        sh 'docker start prometheus'
                    }
                }
            }
        }

        stage('Check and Start Grafana') {
            steps {
                script {
                    def grafanaRunning = sh(script: 'docker ps -q -f name=grafana', returnStdout: true).trim()
                    if (grafanaRunning) {
                        echo 'Grafana is already running.'
                    } else {
                        echo 'Starting Grafana container...'
                        sh 'docker start grafana'
                    }
                }
            }
        }

        stage('Validate Setup') {
            steps {
                script {
                    echo 'Validating Prometheus and Grafana setup...'
                    sh 'curl -f http://localhost:9090/ || echo "Prometheus is not accessible"'
                    sh 'curl -f http://localhost:3000/ || echo "Grafana is not accessible"'
                }
            }
        }
        */
    }

    post {
        always {
            // Debugging: List files again before trying to publish to ensure the report exists
            sh 'ls -R /var/lib/jenkins/workspace/sonar/reports'
            
            // Publish the Dependency-Check results using the environment variable
            dependencyCheckPublisher(
                pattern: "${REPORT_PATH}",  // Use the REPORT_PATH environment variable
                unstableTotalLow: '5',       // Threshold for low vulnerabilities
                unstableNewHigh: '3'         // Threshold for new high vulnerabilities
            )
        }
    }
}
