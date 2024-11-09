pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'  // Adjust if necessary
        maven 'M2_HOME'  // Adjust if necessary
    }

    environment {
        REPORT_PATH = '**/dependency-check-report.html'  // Use relative GLOB pattern for HTML report
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

        // OWASP Dependency Check Stage
        stage('OWASP Dependency Check') {
            steps {
                dependencyCheck additionalArguments: '--scan target/', odcInstallation: 'owasp'
            }
        }

        // Publish OWASP Dependency Check Report Stage
        stage('Publish OWASP Dependency Check Report') {
            steps {
                publishHTML(target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'target',   // Directory where the report is generated
                    reportFiles: 'dependency-check-report.html',  // Report file name
                    reportName: 'OWASP Dependency Check Report'
                ])
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
    }

    post {
        always {
            // Debugging: List files in the reports directory before publishing
            sh 'ls -R target'

            // Publish the Dependency-Check HTML report (not XML) in the post section
            publishHTML(target: [
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'target',
                reportFiles: 'dependency-check-report.html',  // Report file name
                reportName: 'OWASP Dependency Check HTML Report'
            ])
        }
    }
}
