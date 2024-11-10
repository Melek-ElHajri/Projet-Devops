pipeline {
    agent any

    /*environment {
        // Uncomment and set the necessary environment variables if needed
        // SONAR_TOKEN = 'squ_65eb01a1246ad720ee511a4d5d0bce064014'
        // dockerhub_token = credentials('dockerhub_token')
    }*/
    
    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    stages {
        stage('GIT') {
            steps {
                git branch: 'Rjeibi-Hazem',
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

        // Quick Nmap Scan stage targeting google.com
        stage('Quick Nmap Scan') {
            steps {
                script {
                    def targetHost = 'google.com'  // Scanning google.com
                    
                    echo "Running quick Nmap scan on ${targetHost}"
                    
                    // Quick scan (first 1000 ports)
                    sh "nmap -p 1-1000 -T4 -n -Pn ${targetHost} -oN nmap_quick_scan_report.txt"
                }
            }
            post {
                always {
                    // Archive the Nmap scan results as Jenkins artifacts
                    archiveArtifacts artifacts: 'nmap_quick_scan_report.txt', allowEmptyArchive: true
                    echo "Quick Nmap scan report has been archived."
                }
            }
        }

        // Security Scan with FindSecurityBugs (Only one instance needed)
        stage('Security Scan with FindSecurityBugs') {
            steps {
                echo "Running security scan using FindSecurityBugs..."
                sh 'mvn clean compile spotbugs:check'  // Runs the FindSecurityBugs plugin
            }
            post {
                always {
                    // Archive FindSecurityBugs report as Jenkins artifacts
                    archiveArtifacts artifacts: 'target/spotbugsXml.xml', allowEmptyArchive: true
                    echo "FindSecurityBugs report has been archived."
                }
            }
        }
    }

    post {
        success {
            script {
                emailext (
                    subject: "Build Success: ${currentBuild.fullDisplayName}",
                    body: "Le build a réussi ! Consultez les détails à ${env.BUILD_URL}",
                    recipientProviders: [[$class: 'CulpritsRecipientProvider'], [$class: 'DevelopersRecipientProvider']],
                    to: 'rim.gabsi.zg@gmail.com, rim.gabsi@esprit.tn'
                )
            }
        }
        failure {
            script {
                emailext (
                    subject: "Build Failure: ${currentBuild.fullDisplayName}",
                    body: "Le build a échoué ! Vérifiez les détails à ${env.BUILD_URL}",
                    recipientProviders: [[$class: 'CulpritsRecipientProvider'], [$class: 'DevelopersRecipientProvider']],
                    to: 'rim.gabsi.zg@gmail.com, rim.gabsi@esprit.tn'
                )
            }
        }
    }
}
