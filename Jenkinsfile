pipeline {
    agent any

  /*  environment {
        // Uncomment and use if necessary
        // SONAR_TOKEN = 'your-sonar-token'
        // dockerhub_token = credentials('dockerhub_token')
    }*/

    tools {
        jdk 'JAVA_HOME'  // Ensure JAVA_HOME is set in Jenkins
        maven 'M2_HOME'  // Ensure M2_HOME is set in Jenkins
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
                echo "Running Maven clean install and compile"
                sh 'mvn clean install compile'
            }
        }
        
        stage('JUnit/Mockito Tests') {
            steps {
                echo "Running JUnit/Mockito tests"
                sh 'mvn test' 
            }
        }

        // JaCoCo Code Coverage Stage
        stage('Code Coverage with JaCoCo') {
            steps {
                echo "Running JaCoCo code coverage"
                sh 'mvn jacoco:report'  // This will generate the JaCoCo report
            }
            post {
                always {
                    // Archive the JaCoCo report as Jenkins artifact
                    archiveArtifacts artifacts: 'target/site/jacoco/jacoco.xml', allowEmptyArchive: true
                    echo "JaCoCo report has been archived."
                }
            }
        }

        // Nmap Security Scan
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

        // FindSecurityBugs Scan
        stage('Security Scan with FindSecurityBugs') {
            steps {
                echo "Running security scan using FindSecurityBugs..."
                sh 'mvn spotbugs:check'  // Runs the SpotBugs plugin
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
}
