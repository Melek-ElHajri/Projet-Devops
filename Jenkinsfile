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

      //  stage('Deploy to Nexus') {
       //     steps {
       //         sh 'mvn deploy -DskipTests -DaltDeploymentRepository=deploymentRepo::default::http://192.168.33.10:8081/repository/maven-releases/'
        //    }
       // }

        stage('Success Notification') {
            steps {
                script {
                    if (currentBuild.result == 'SUCCESS') {
                        mail(
                            to: "${EMAIL_RECIPIENTS}",
                            subject: "${SUCCESS_SUBJECT}",
                            body: "${SUCCESS_BODY}"
                        )
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
            }
        }
    }
}
