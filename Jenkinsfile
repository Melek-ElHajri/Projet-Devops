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
        POST_BUILD_BODY_SUCCESS = """
Hello Team,

The automated build for ${JOB_NAME} has completed successfully.

The build started on ${new Date().format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("UTC"))} for the project ${JOB_NAME}.
Its status is SUCCESS.

- Git Branch: ${env.GIT_BRANCH ?: 'Unknown'}
- Triggered By: ${env.BUILD_USER_ID ?: 'Unknown'}

Thank you for your attention, and if you have any questions or need further assistance, feel free to reach out.

Best regards,
Jenkins Automation
"""
        POST_BUILD_BODY_FAILURE = """
Hello Team,

The automated build for ${JOB_NAME} has encountered a failure.

The build started on ${new Date().format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("UTC"))} for the project ${JOB_NAME}.
Its status is FAILURE.

- Git Branch: ${env.GIT_BRANCH ?: 'Unknown'}
- Triggered By: ${env.BUILD_USER_ID ?: 'Unknown'}

Please review the console output for specific error messages and details regarding the failure.

Your attention to these details is appreciated, and if you have any questions or need further assistance, feel free to reach out.

Best regards,
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
- Please review the console output for specific error messages and details related to the failure. 

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

        // Add your other stages as necessary
    }

    post {
        always {
            script {
                // Prepare the email body based on the build status
                def emailBody = currentBuild.result == 'SUCCESS' ? POST_BUILD_BODY_SUCCESS : POST_BUILD_BODY_FAILURE
                mail(
                    to: "${EMAIL_RECIPIENTS}",
                    subject: "${POST_BUILD_SUBJECT}",
                    body: emailBody
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
