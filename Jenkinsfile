pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'  
        maven 'M2_HOME' 
    }

    stages {
        stage('Pre-Build Notification') {
            steps {
                script {
                    // Pre-build message (e.g., build start)
                    sh """
                        curl -X POST 'https://api.twilio.com/2010-04-01/Accounts/ACcf0b93794273e3d6a04def864f3447b7/Messages.json' \
                        --data-urlencode 'To=+21692395932' \
                        --data-urlencode 'From=+19292961290' \
                        --data-urlencode 'Body=Starting build process... Job: ${env.JOB_NAME}, Build: ${env.BUILD_NUMBER}' \
                        -u ACcf0b93794273e3d6a04def864f3447b7:5f7ebacbd05a57fc1691712dc1e16bcf
                    """
                }
            }
        }
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
                        reportDir: '.', 
                        reportFiles: 'dependency-check-report.html', 
                        reportName: 'Dependency Check Report', 
                        alwaysLinkToLastBuild: true, 
                        keepAll: true 
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
    }
  post {
    success {
        script {
            // Success message
            sh """
                curl -X POST 'https://api.twilio.com/2010-04-01/Accounts/ACcf0b93794273e3d6a04def864f3447b7/Messages.json' \
                --data-urlencode 'To=+21692395932' \
                --data-urlencode 'From=+19292961290' \
                --data-urlencode 'Body=Congratulations! Build #${env.BUILD_NUMBER} was successful! Let's proceed to the next step in the pipeline. Job: ${env.JOB_NAME}' \
                -u 'ACcf0b93794273e3d6a04def864f3447b7:5f7ebacbd05a57fc1691712dc1e16bcf'
            """
        }
    }
    failure {
        script {
            // Failure message
            sh """
                curl -X POST 'https://api.twilio.com/2010-04-01/Accounts/ACcf0b93794273e3d6a04def864f3447b7/Messages.json' \
                --data-urlencode 'To=+21692395932' \
                --data-urlencode 'From=+19292961290' \
                --data-urlencode 'Body=Oops! An error occurred during build #${env.BUILD_NUMBER}. Please verify the code or check the logs for details. Job: ${env.JOB_NAME}' \
                -u 'ACcf0b93794273e3d6a04def864f3447b7:5f7ebacbd05a57fc1691712dc1e16bcf'
            """
        }
    }
    always {
        script {
            // Get the current time in a readable format
            def currentTime = new Date().format('yyyy-MM-dd HH:mm:ss')

            // Build status report (whether successful or failed)
            sh """
                curl -X POST 'https://api.twilio.com/2010-04-01/Accounts/ACcf0b93794273e3d6a04def864f3447b7/Messages.json' \
                --data-urlencode 'To=+21692395932' \
                --data-urlencode 'From=+19292961290' \
                --data-urlencode 'Body=Build Report: Job: ${env.JOB_NAME}, Build: ${env.BUILD_NUMBER}, Status: ${currentBuild.currentResult}, Duration: ${currentBuild.durationString}, Timestamp: ${currentTime}' \
                -u 'ACcf0b93794273e3d6a04def864f3447b7:5f7ebacbd05a57fc1691712dc1e16bcf'
            """
        }
    }
}
    }
}
