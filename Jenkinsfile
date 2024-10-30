pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
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

The automated build for ${JOB_NAME} has completed.

The build started on ${new Date().format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("UTC"))} for the project ${JOB_NAME}. Its status is ${currentBuild.result ?: 'SUCCESS'}.

If the build was successful, the latest code changes have been compiled and deployed without issues. If it has failed, please review the console output for specific error messages and details regarding the failure.

Your attention to these details is appreciated, and if you have any questions or need further assistance, feel free to reach out.

Thank you,
Jenkins Automation
"""
        FAILURE_SUBJECT = "Build Failure - ${JOB_NAME} #${BUILD_NUMBER}"
        FAILURE_BODY = """
Hello Team,

The automated build for ${JOB_NAME} encountered a failure.

Build Information:
- Job Name: ${JOB_NAME}
- Build Status: FAILURE
- Job Number: ${BUILD_NUMBER}
- Job URL: ${BUILD_URL}
- Failure Time: ${new Date().format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("UTC"))}

Failure Details:
The build has failed due to the following reasons:
- Please review the console output for specific error messages and details related to the failure. Common issues could include compilation errors, failed tests, or deployment issues.

Your prompt attention to these issues is crucial, and if you require further assistance, please do not hesitate to reach out.

Thank you,
Jenkins Automation
"""
        ERROR_SUBJECT = "Jenkinsfile Error - ${JOB_NAME} #${BUILD_NUMBER}"
        ERROR_BODY = """
Hello Team,

There was an error in the Jenkinsfile for the job ${JOB_NAME}.

Build Information:
- Job Name: ${JOB_NAME}
- Job Number: ${BUILD_NUMBER}
- Job URL: ${BUILD_URL}

Please check the Jenkins logs for details.

Thank you,
Jenkins Automation
"""
    }

    stages {
        stage('Error Handling') {
            steps {
                script {
                    try {
                        // Placeholder for pipeline execution
                        echo 'Pipeline execution begins...'
                    } catch (Exception e) {
                        // Send error email when there's an exception
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
                }
            }
        }

        stage('GIT') {
            steps {
                git branch: 'NouhaSedraoui',
                    url: 'https://github.com/Melek-ElHajri/Projet-Devops.git'
            }
        }

        stage('Compile Stage') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Deploy to Nexus') {
            steps {
                sh 'mvn deploy -DskipTests -DaltDeploymentRepository=deploymentRepo::default::http://192.168.33.10:8081/repository/maven-releases/'
            }
        }

        // Commented out the SonarQube stage
        // stage('Scan') {
        //     steps {
        //         withSonarQubeEnv('sq1') {
        //             sh 'mvn sonar:sonar'
        //         }
        //     }
        // }

        // Commented out the Docker image build stage
        // stage('Build Docker Image') {
        //     steps {
        //         sh 'sudo docker build -t rymasd29/tp-foyer:5.0.0 .'
        //     }
        // }

        // Commented out the Docker image push stage
        // stage('Push Docker Image to DockerHub') {
        //     steps {
        //         sh '''
        //            sudo docker login -u rymasd29 -p 223JFT4309
        //            sudo docker push rymasd29/tp-foyer:5.0.0
        //         '''
        //     }
        // }

        // Commented out the Docker Compose run stage
        // stage('Run Docker Compose') {
        //     steps {
        //         script {
        //             sh '''
        //                 sudo docker-compose down -v
        //                 sudo docker-compose up -d
        //             ''' 
        //         }
        //     }
        // }
    }

    post {
        always {
            script {
                mail(
                    to: "${EMAIL_RECIPIENTS}",
                    subject: "${POST_BUILD_SUBJECT}",
                    body: "${POST_BUILD_BODY.replace('SUCCESS', currentBuild.result)}"
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
            }
        }
    }
}
