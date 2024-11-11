pipeline {
    agent any
    
    environment {
       
        SONAR_TOKEN = credentials('SONAR_TEXT')
        dockerhub_token = credentials('dockerhub_token')
    }
    
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

        
        stage('Quick Nmap Scan') {
            steps {
                script {
                    def targetHost = 'http://192.168.23.133:8089' 
                    
                    echo "Running quick Nmap scan on ${targetHost}"
                    
                    
                    sh "nmap -p 1-1000 -T4 -n -Pn ${targetHost} -oN nmap_quick_scan_report.txt"
                }
            }
            post {
                always {
                   
                    archiveArtifacts artifacts: 'nmap_quick_scan_report.txt', allowEmptyArchive: true
                    echo "Quick Nmap scan report has been archived."
                }
            }
        }


           stage("Generate Docker Image") {
            steps {
               
                sh 'docker build -t rh1337/tp-foyer:5.0.0 .'
            }
        }

        stage("Push Docker Image") {
            steps {
                sh "echo ${dockerhub_token} | docker login -u rh1337 --password-stdin" 
                sh "docker push rh1337/tp-foyer:5.0.0"
            }
        }

        stage('Docker Compose') {
            steps {
                sh 'docker compose up -d'
            }
        }
            
        
        
        stage('SQL Injection Test (SQLmap)') {
            steps {
                script {
                    /
                    sh '''
                        python3 /var/lib/jenkins/workspace/nmap/gauntlt-attacks/sqlmap/sqlmap.py \
                        -u "http://192.168.23.133:8089/tpfoyer/etudiant/add-etudiant" \
                        --data="nomEtudiant=Robert&prenomEtudiant=Test&cinEtudiant=123456&dateNaissance=2000-01-01" \
                        --batch --level=5 --risk=3 --tamper=space2comment | tee sqlmap_output.txt
                    '''
                    
                    
                    archiveArtifacts artifacts: 'sqlmap_output.txt', allowEmptyArchive: true
                }
            }
        }


stage('ZAP Baseline Scan') {
            steps {
                script {
                    
                    def result = sh(script: '''
                        docker run --rm -v /var/lib/jenkins/workspace/nmap/zap_results:/zap/wrk -t zaproxy/zap-stable zap-baseline.py -t http://192.168.23.133:8089/tpfoyer/etudiant/add-etudiant -g /zap/wrk/gen.conf -r /zap/wrk/baseline_scan_report.html
                        chmod -R 777 /var/lib/jenkins/workspace/nmap/zap_results
                    ''', returnStatus: true)

             
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
                   
                    def result = sh(script: '''
                        docker run --rm -v /var/lib/jenkins/workspace/nmap/zap_results:/zap/wrk -t zaproxy/zap-stable zap-full-scan.py -t http://192.168.23.133:8089/tpfoyer/etudiant/add-etudiant -g /zap/wrk/gen.conf -r /zap/wrk/active_scan_report.html
                        chmod -R 777 /var/lib/jenkins/workspace/nmap/zap_results
                    ''', returnStatus: true)

                   
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
   
                publishHTML(target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: false,
                    keepAll: true,
                    reportDir: '/var/lib/jenkins/workspace/nmap/zap_results',  
                    reportFiles: 'baseline_scan_report.html,active_scan_report.html',  
                    reportName: 'ZAP Reports'
                ])
            }
        }



        
stage('Scan') {
            steps {
               
                sh '''
                    if ! docker ps | grep 055ccab75690 > /dev/null; then
                        echo "SonarQube container is not running. Starting SonarQube container..."
                        docker start 055ccab75690
                        sleep 20  # Wait for the container to be fully up
                    else
                        echo "SonarQube container is already running."
                    fi
                '''
                
               
                withSonarQubeEnv('snrq') {
                    sh 'mvn sonar:sonar -Dsonar.login=$SONAR_TOKEN'
                }
            }
    }
    }
        
        

    
    }
}
