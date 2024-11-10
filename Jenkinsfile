pipeline {
    agent any
  /*  
    environment {
        SONAR_TOKEN = 'squ_65eb01a1246ad720ee511a4d5d0bce064014'
       // SONAR_TOKEN = credentials('SONAR_TEXT')
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


                // Gauntlt Attack Stage
        stage('Gauntlt Attack Tests') {
            steps {
                script {
                    echo "Running Gauntlt security tests..."
                    
                    // Run Gauntlt against all attack files in gauntlt_attacks directory
                    sh 'gauntlt gauntlt_attacks/*.attack > gauntlt_report.txt'
                }
            }
            post {
                always {
                    // Archive Gauntlt report
                    archiveArtifacts artifacts: 'gauntlt_report.txt', allowEmptyArchive: true
                    echo "Gauntlt report has been archived."
                }
            }
        }
    

    
    }
}
