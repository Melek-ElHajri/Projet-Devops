pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'  // Adjust if necessary
        maven 'M2_HOME'  // Adjust if necessary
    }
  environment {
        EMAIL_RECIPIENTS = 'nouha.sedraoui@esprit.tn'
        PRE_BUILD_SUBJECT = "Pre-Build Notification - ${JOB_NAME} #${BUILD_NUMBER}"
        PRE_BUILD_BODY = """
Hello Team,

The automated build for ${JOB_NAME} initiated by Jenkins is about to begin.

Build Information:
- Job Name: ${JOB_NAME}
- Build Number: #${BUILD_NUMBER}
- Build URL: ${BUILD_URL}
- Start Time: ${new Date().format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("UTC"))}

Thank you,
Jenkins Automation
"""
        POST_BUILD_SUBJECT = "Build Report - ${JOB_NAME} #${BUILD_NUMBER}"
        POST_BUILD_BODY = """
Hello Team,

We are committed to maintaining high standards of security throughout our development processes. As part of this commitment, we are sending you this email to keep you informed of the build activities associated with ${JOB_NAME}. 

The automated build for ${JOB_NAME} has completed, commencing at ${new Date().format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("UTC"))}. Its current status is ${currentBuild.result ?: 'SUCCESS'}.

If the build was successful, the latest code changes have been compiled and deployed without issues. However, in the event of a failure, please review the console output for specific error messages and details regarding the cause.

Your vigilance in monitoring these updates is essential for our continuous improvement and security compliance. Should you have any questions or require further assistance, feel free to reach out.

Thank you for your collaboration,
Jenkins Automation
"""
        SUCCESS_SUBJECT = "Build Success - ${JOB_NAME} #${BUILD_NUMBER}"
        SUCCESS_BODY = """
Hello Team,

We are pleased to inform you that the automated build for ${JOB_NAME} has completed successfully.

Build Information:
- Job Name: ${JOB_NAME}
- Build Status: SUCCESS
- Job Number: ${BUILD_NUMBER}
- Job URL: ${BUILD_URL}
- Completion Time: ${new Date().format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("UTC"))}

Thank you for your collaboration, and let's continue to maintain high standards in our projects.

Best regards,
Jenkins Automation
"""
        FAILURE_SUBJECT = "Build Failure - ${JOB_NAME} #${BUILD_NUMBER}"
        FAILURE_BODY = """
Hello Team,

In our ongoing effort to uphold security measures in compliance with industry standards, we are notifying you of a failure in the automated build for ${JOB_NAME}.

Build Information:
- Job Name: ${JOB_NAME}
- Build Status: FAILURE
- Job Number: ${BUILD_NUMBER}
- Job URL: ${BUILD_URL}
- Failure Time: ${new Date().format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("UTC"))}

Failure Details:
The build has failed due to the following reasons:
- Please review the console output for specific error messages and details related to the failure. Common issues could include compilation errors, failed tests, or deployment problems.

We appreciate your prompt attention to these issues, as your engagement is crucial for maintaining the quality and security of our projects. If you require further assistance, please do not hesitate to reach out.

Thank you,
Jenkins Automation
"""
        ERROR_SUBJECT = "Jenkinsfile Error - ${JOB_NAME} #${BUILD_NUMBER}"
        ERROR_BODY = """
Hello Team,

This email is part of our commitment to security compliance. 

An error has occurred in the Jenkinsfile for the job ${JOB_NAME}.

Build Information:
- Job Name: ${JOB_NAME}
- Job Number: ${BUILD_NUMBER}
- Job URL: ${BUILD_URL}

We urge you to check the Jenkins logs for detailed information about this issue.

Thank you for your attention,
Jenkins Automation
"""
        NOTIFY_TOKEN = 'xa_wiujkx3bfrasfrqnzopgeuag659gh' // Add your Notify.Events token here
    }

    stages {
         stage('Error Handling') {
            steps {
                script {
                    try {
                        echo 'Pipeline execution begins...'
                    } catch (Exception e) {
                        mail(
                            to: "${EMAIL_RECIPIENTS}",
                            subject: "${ERROR_SUBJECT}",
                            body: "${ERROR_BODY}"
                        )
                        error("Pipeline aborted due to syntax error or exception: ${e}")
                    }
                }
            }
        }

        stage('Pre-Build Notification') {
            steps {
                script {
                    mail(
                        to: "${EMAIL_RECIPIENTS}",
                        subject: "${PRE_BUILD_SUBJECT}",
                        body: "${PRE_BUILD_BODY}"
                    )
                    notifyEvents message: "<b>Pre-Build Notification</b> - Job: ${JOB_NAME}, Build Number: ${BUILD_NUMBER}", 
                                 token: "${NOTIFY_TOKEN}"
                }
            }
        }

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

        stage('Run Junit/Mockito|Jacoco Tests') {
            steps {
                sh 'mvn test'
                sh 'ls -R target/site/jacoco || echo "JaCoCo report directory not found"'
            }
        }

        stage('JaCoCo Report') {
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

       /* stage('Dependency Check') {
            steps {
                dependencyCheck additionalArguments: '--failOnCVSS 7 --out target/dependency-check-report --noupdate', 
                               odcInstallation: 'Dependency-Check'
            }
        }*/
    
        stage ('OWASP Dependency-Check Vulnerabilities') {
            steps {
                dependencyCheck additionalArguments: ''' 
                    -o "./" 
                    -s "./"
                    -f "ALL" 
                    --prettyPrint''', odcInstallation: 'Dependency-Check'

                dependencyCheckPublisher pattern: 'dependency-check-report.xml'
            }
        }     
    


        stage('Publish Dependency-Check Report') {
            steps {
                script {
                    publishHTML([ 
                        reportDir: 'Projet-Devops/target/dependency-check-report',
                        reportFiles: 'dependency-check-report.html',  // Ensure this matches the file generated
                        reportName: 'Dependency Check Report',
                        alwaysLinkToLastBuild: true,
                        keepAll: true
                    ])
                }
            }
        }
        
         stage('Sonar Analysis') {
            steps {
                withSonarQubeEnv('sq1') {
                    sh 'mvn sonar:sonar -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package'
            }
        }

       
 stage('Deploy to Nexus') {
            steps {
                sh 'mvn deploy -DskipTests -DaltDeploymentRepository=deploymentRepo::default::http://192.168.33.10:8081/repository/maven-releases/'
            }
        }

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


        stage('ZAP Baseline Scan') {
            steps {
                script {
                    // Run ZAP Baseline scan and set full permissions for the report
                    def result = sh(script: '''
                        sudo docker run --rm -v /var/lib/jenkins/workspace/sonar/zap_results:/zap/wrk -t zaproxy/zap-stable zap-baseline.py -t http://192.168.33.10:8089/tpfoyer/etudiant/add-etudiant -g /zap/wrk/gen.conf -r /zap/wrk/baseline_scan_report.html
                        sudo chmod -R 777 /var/lib/jenkins/workspace/sonar/zap_results
                    ''', returnStatus: true)

                    // Check the result of the ZAP scan
                    if (result != 0) {
                        echo "ZAP Baseline Scan completed with warnings or errors."
                    } else {
                        echo "ZAP Baseline Scan completed successfully."
                    }
                }
            }
        }

        stage('ZAP Active Scan') {
            steps {
                script {
                    // Run ZAP Active scan and set full permissions for the report
                    def result = sh(script: '''
                        sudo docker run --rm -v /var/lib/jenkins/workspace/sonar/zap_results:/zap/wrk -t zaproxy/zap-stable zap-full-scan.py -t http://192.168.33.10:8089/tpfoyer/etudiant/add-etudiant -g /zap/wrk/gen.conf -r /zap/wrk/active_scan_report.html
                        sudo chmod -R 777 /var/lib/jenkins/workspace/sonar/zap_results
                    ''', returnStatus: true)

                    // Check the result of the ZAP scan
                    if (result != 0) {
                        echo "ZAP Active Scan completed with warnings or errors."
                    } else {
                        echo "ZAP Active Scan completed successfully."
                    }
                }
            }
        }

        stage('Publish ZAP Reports') {
            steps {
                // Publish both the baseline and active scan reports
                publishHTML(target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: false,
                    keepAll: true,
                    reportDir: '/var/lib/jenkins/workspace/sonar/zap_results',
                    reportFiles: 'baseline_scan_report.html, active_scan_report.html',
                    reportName: 'ZAP Reports'
                ])
            }
        }

        stage('Nmap Scan Attack') {
            steps {
                script {
                    // Run Gauntlt Nmap attack from the correct directory and redirect output to a file
                    sh 'sudo gauntlt /var/lib/jenkins/workspace/sonar/gauntlt-attacks/nmap.attack > nmap_output.txt'
                    
                    // Archive the output file
                    archiveArtifacts artifacts: 'nmap_output.txt', allowEmptyArchive: true
                }
            }
        }

        stage('SQL Injection Attack (Gauntlt)') {
            steps {
                script {
                    // Run Gauntlt SQL Injection attack from the correct directory and redirect output to a file
                    sh 'gauntlt /var/lib/jenkins/workspace/sonar/gauntlt-attacks/sql_in.attack > sql_injection_output.txt'
                    
                    // Archive the output file
                    archiveArtifacts artifacts: 'sql_injection_output.txt', allowEmptyArchive: true
                }
            }
        }

        stage('SQL Injection Test (SQLmap)') {
            steps {
                script {
                    // Run SQLmap for deeper SQL injection testing and redirect output to a file
                    sh 'python3 /var/lib/jenkins/workspace/sonar/gauntlt-attacks/sqlmap/sqlmap.py -u "http://192.168.33.10:8089/tpfoyer/etudiant/add-etudiant" --data="nomEtudiant=Robert&prenomEtudiant=Test&cinEtudiant=123456&dateNaissance=2000-01-01" --batch --level=5 --risk=3 --tamper=space2comment > sqlmap_output.txt'
                    
                    // Archive the output file
                    archiveArtifacts artifacts: 'sqlmap_output.txt', allowEmptyArchive: true
                }
            }
        }
         stage('Success Notification') {
            steps {
                script {
                    if (currentBuild.result == 'SUCCESS') {
                        mail(
                            to: "${EMAIL_RECIPIENTS}",
                            subject: "${SUCCESS_SUBJECT}",
                            body: "${SUCCESS_BODY}"
                        )
                        notifyEvents message: "<b>Build Success</b> - Job: ${JOB_NAME}, Build Number: ${BUILD_NUMBER}", 
                                     token: "${NOTIFY_TOKEN}"
                    }
                }
            }
        }


    }
   post {
        always {
            script {
                mail(
                    to: "${EMAIL_RECIPIENTS}",
                    subject: "${POST_BUILD_SUBJECT}",
                    body: "${POST_BUILD_BODY.replace('SUCCESS', currentBuild.result)}"
                )
                notifyEvents message: "<b>Post-Build Notification</b> - Job: ${JOB_NAME}, Build Status: ${currentBuild.result ?: 'SUCCESS'}", 
                             token: "${NOTIFY_TOKEN}"
            }
        }
        success {
            script {
                mail(
                    to: "${EMAIL_RECIPIENTS}",
                    subject: "${SUCCESS_SUBJECT}",
                    body: "${SUCCESS_BODY}"
                )
            }
        }
        failure {
            script {
                mail(
                    to: "${EMAIL_RECIPIENTS}",
                    subject: "${FAILURE_SUBJECT}",
                    body: "${FAILURE_BODY}"
                )
                notifyEvents message: "<b>Build Failed</b> - Job: ${JOB_NAME}, Build Number: ${BUILD_NUMBER}", 
                             token: "${NOTIFY_TOKEN}"
            }
        }
    }
  
}
