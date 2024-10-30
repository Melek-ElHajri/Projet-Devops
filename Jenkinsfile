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

Build Information:
- Job Name: ${JOB_NAME}
- Build Status: ${currentBuild.currentResult}
- Job Number: ${BUILD_NUMBER}
- Job URL: ${BUILD_URL}
- Completion Time: ${new Date().format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("UTC"))}

Thank you,
Jenkins Automation
"""
        FAILURE_SUBJECT = "Build Failure - ${JOB_NAME} #${BUILD_NUMBER}"
        FAILURE_BODY = """
Hello Team,

The automated build for ${JOB_NAME} encountered a failure.

Build Information:
- Job Name: ${JOB_NAME}
- Build Status: ${currentBuild.currentResult}
- Job Number: ${BUILD_NUMBER}
- Job URL: ${BUILD_URL}
- Failure Time: ${new Date().format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("UTC"))}

Thank you,
Jenkins Automation
"""
    }

    stages {
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

        // stage('Scan') {
        //     steps {
        //         withSonarQubeEnv('sq1') {
        //             sh 'mvn sonar:sonar'
        //         }
        //     }
        // }
        
        // stage('Build Docker Image') {
        //     steps {
        //         sh 'sudo docker build -t rymasd29/tp-foyer:5.0.0 .' 
        //     }
        // }

        // stage('Push Docker Image to DockerHub') {
        //     steps {
        //         sh '''
        //            sudo docker login -u rymasd29 -p 223JFT4309
        //            sudo docker push rymasd29/tp-foyer:5.0.0
        //         '''
        //     }
        // }

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
                    body: "${POST_BUILD_BODY}"
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
