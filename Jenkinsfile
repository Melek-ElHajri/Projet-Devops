pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'  // Adjust if necessary
        maven 'M2_HOME'  // Adjust if necessary
    }

    stages {
        stage('GIT') {
            steps {
                git branch: 'NouhaSedraoui', url: 'https://github.com/Melek-ElHajri/Projet-Devops.git'
            }
        }

         stage('ZAP Baseline Scan') {
            steps {
                script {
                    // Run ZAP Baseline scan and set full permissions for the report
                    sh '''
                    sudo docker run --rm -v /var/lib/jenkins/workspace/sonar/zap_results:/zap/wrk --user $(id -u jenkins):$(id -g jenkins) -t zaproxy/zap-stable zap-baseline.py -t http://192.168.33.10:8089/tpfoyer/etudiant/add-etudiant -g /zap/wrk/gen.conf -r /zap/wrk/baseline_scan_report.html
                    sudo chmod -R 777 /var/lib/jenkins/workspace/sonar/zap_results
                    '''
                }
            }
        }

        stage('ZAP Active Scan') {
            steps {
                script {
                    // Run ZAP Active scan and set full permissions for the report
                    sh '''
                    sudo docker run --rm -v /var/lib/jenkins/workspace/sonar/zap_results:/zap/wrk --user $(id -u jenkins):$(id -g jenkins) -t zaproxy/zap-stable zap-full-scan.py -t http://192.168.33.10:8089/tpfoyer/etudiant/add-etudiant -g /zap/wrk/gen.conf -r /zap/wrk/active_scan_report.html
                    sudo chmod -R 777 /var/lib/jenkins/workspace/sonar/zap_results
                    '''
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
                    // Run Gauntlt Nmap attack from the correct directory, display output to console and save it to a file
                    sh 'sudo gauntlt /var/lib/jenkins/workspace/sonar/gauntlt-attacks/nmap.attack | tee nmap_output.txt'
                    
                    // Archive the output file
                    archiveArtifacts artifacts: 'nmap_output.txt', allowEmptyArchive: true
                }
            }
        }

        stage('SQL Injection Attack (Gauntlt)') {
            steps {
                script {
                    // Run Gauntlt SQL Injection attack from the correct directory, display output to console and save it to a file
                    sh 'gauntlt /var/lib/jenkins/workspace/sonar/gauntlt-attacks/sql_in.attack | tee sql_injection_output.txt'
                    
                    // Archive the output file
                    archiveArtifacts artifacts: 'sql_injection_output.txt', allowEmptyArchive: true
                }
            }
        }

        stage('SQL Injection Test (SQLmap)') {
            steps {
                script {
                    // Run SQLmap for deeper SQL injection testing, display output to console and save it to a file
                    sh 'python3 /var/lib/jenkins/workspace/sonar/gauntlt-attacks/sqlmap/sqlmap.py -u "http://192.168.33.10:8089/tpfoyer/etudiant/add-etudiant" --data="nomEtudiant=Robert&prenomEtudiant=Test&cinEtudiant=123456&dateNaissance=2000-01-01" --batch --level=5 --risk=3 --tamper=space2comment | tee sqlmap_output.txt'
                    
                    // Archive the output file
                    archiveArtifacts artifacts: 'sqlmap_output.txt', allowEmptyArchive: true
                }
            }
        }
    }
}
