pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'  
        maven 'M2_HOME' 
    }

    stages {
        stage('GIT') {
            steps {
                git branch: 'NouhaSedraouii', url: 'https://github.com/Melek-ElHajri/Projet-Devops.git'
            }
        }
          stage('Development - Clean') {
            steps {
                sh 'mvn clean'
            }
        }

        stage('Development - Compile') {
            steps {
                sh 'mvn compile'
            }
        }
         stage('Deployment - Package') {
            steps {
                sh 'mvn package'
            }
        }
       
        stage('Testing - JUnit, Mockito, and JaCoCo Tests') {
            steps {
                sh 'mvn test'
                sh 'ls -R target/site/jacoco || echo "JaCoCo report directory not found"'
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
         stage('Testing - OWASP Dependency-Check Vulnerabilities') {
            steps {
                    dependencyCheck additionalArguments: '--failOnCVSS 7 --out target/dependency-check-report --noupdate', 
                               odcInstallation: 'Dependency-Check'
                    dependencyCheckPublisher pattern: 'target/dependency-check-report/dependency-check-report.xml'
    }
}

      stage('Testing - Publish Dependency-Check Report') {
    steps {
        script {
            publishHTML([ 
                reportDir: '.',                     // Current directory (DevSecOps)
                reportFiles: 'dependency-check-report.html',  // The report file
                reportName: 'Dependency Check Report',        // Title of the report
                alwaysLinkToLastBuild: true,       // Always link to the last build
                keepAll: true                      // Keep all reports
            ])
        }
    }
}


         stage('Testing - Sonar Analysis') {
            steps {
                withSonarQubeEnv('sq1') {
                    sh 'mvn sonar:sonar -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
                }
            }
        }

         stage('Deployment - Deploy to Nexus') {
            steps {
                // Deploy to Nexus repository
                sh 'mvn deploy -DskipTests -Dautoupdate=false -DaltDeploymentRepository=deploymentRepo::default::http://192.168.33.10:8081/repository/maven-releases/'
            }
        }
        stage('Build Docker Image') {
            steps {
                sh 'sudo docker build -t rymasd29/tp-foyers:1.0.0 .'
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

         stage('Operate: Monitor - Check and Start Prometheus') {
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
        stage('Operate: Monitor - Check and Start Grafana') {
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

        stage('Operate: Monitor - Validate Setup') {
            steps {
                script {
                    echo 'Validating Prometheus and Grafana setup...'
                    sh 'curl -f http://localhost:9090/ || echo "Prometheus is not accessible"'
                    sh 'curl -f http://localhost:3000/ || echo "Grafana is not accessible"'
                }
            }
        }
post {
        success {
            // Send SMS on successful build
            twilioSend(
                message: "Build SUCCESSFUL! Job: ${env.JOB_NAME}, Build: ${env.BUILD_NUMBER}",
                to: '+21692395932'
            )
        }
        failure {
            // Send SMS on failed build
            twilioSend(
                message: "Build FAILED. Job: ${env.JOB_NAME}, Build: ${env.BUILD_NUMBER}",
                to: '+21692395932'
            )
        }
    }

    }
  

}
