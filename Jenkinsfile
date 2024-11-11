pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'  // Adjust if necessary
        maven 'M2_HOME'  // Adjust if necessary
    }

   

    stages {
       
        stage('Development - GIT Checkout') {
            steps {
                git branch: 'NouhaSedraoui', url: 'https://github.com/Melek-ElHajri/Projet-Devops.git'
            }
        }

       stage('Development - Clean') {
            steps {
                sh 'mvn clean'
            }
        }

        stage('Development - Compile') {
            steps {
                sh 'mvn compile'
            }
        }

        stage('Testing - JUnit, Mockito, and JaCoCo Tests') {
            steps {
                sh 'mvn test'
                sh 'ls -R target/site/jacoco || echo "JaCoCo report directory not found"'
            }
        }

        stage('Testing - JaCoCo Report Generation') {
            steps {
                script {
                    jacoco(
                        execPattern: '**/target/jacoco.exec',
                        classPattern: '**/target/classes',
                        sourcePattern: '**/src/main/java'
                    )
                }
            }
        }

       /* stage('Testing - OWASP Dependency-Check Vulnerabilities') {
            steps {
                dependencyCheck additionalArguments: ''' 
                    -o "./" 
                    -s "./"
                    -f "ALL" 
                    --prettyPrint''', odcInstallation: 'Dependency-Check'

                dependencyCheckPublisher pattern: 'dependency-check-report.xml'
            }
        }*/
       /* stage('Testing - OWASP Dependency-Check Vulnerabilities') {
            steps {
                dependencyCheck additionalArguments: '--failOnCVSS 7 --out target/dependency-check-report --noupdate', 
                               odcInstallation: 'Dependency-Check'
            }
        }*/
        stage('Testing - OWASP Dependency-Check Vulnerabilities') {
            steps {
                    dependencyCheck additionalArguments: '--failOnCVSS 7 --out target/dependency-check-report --noupdate', 
                               odcInstallation: 'Dependency-Check'
                    dependencyCheckPublisher pattern: 'target/dependency-check-report/dependency-check-report.xml'
    }
}

        stage('Testing - Publish Dependency-Check Report') {
            steps {
                script {
                    publishHTML([ 
                        reportDir: 'Projet-Devops/target/dependency-check-report',
                        reportFiles: 'dependency-check-report.html',  // Ensure this matches the file generated
                        reportName: 'Dependency Check Report',
                        alwaysLinkToLastBuild: true,
                        keepAll: true
                    ])
                }
            }
        }

        stage('Testing - Sonar Analysis') {
            steps {
                withSonarQubeEnv('sq1') {
                    sh 'mvn sonar:sonar -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
                }
            }
        }

        stage('Deployment - Package') {
            steps {
                sh 'mvn package'
            }
        }

       stage('Deployment - Deploy to Nexus') {
            steps {
                // Deploy to Nexus repository
                sh 'mvn deploy -DskipTests -Dautoupdate=false -DaltDeploymentRepository=deploymentRepo::default::http://192.168.33.10:8081/repository/maven-releases/'
            }
        }

        stage('Deployment - Build Docker Image') {
            steps {
                sh 'sudo docker build -t rymasd29/tp-foyer:5.0.0 .'
            }
        }

        stage('Deployment - Push Docker Image to DockerHub') {
            steps {
                sh '''
                    sudo docker login -u rymasd29 -p 223JFT4309
                    sudo docker push rymasd29/tp-foyer:5.0.0
                '''
            }
        }

        stage('Deployment - Run Docker Compose') {
            steps {
                script {
                    sh '''
                        sudo docker-compose down 
                        sudo docker-compose up -d
                    '''
                }
            }
        }

        // Operate: Monitor Phase
        stage('Operate: Monitor - Check and Start Prometheus') {
            steps {
                script {
                    def prometheusRunning = sh(script: 'docker ps -q -f name=prometheus', returnStdout: true).trim()
                    if (prometheusRunning) {
                        echo 'Prometheus is already running.'
                    } else {
                        echo 'Starting Prometheus container...'
                        sh 'docker start prometheus'
                    }
                }
            }
        }

        stage('Operate: Monitor - Check and Start Grafana') {
            steps {
                script {
                    def grafanaRunning = sh(script: 'docker ps -q -f name=grafana', returnStdout: true).trim()
                    if (grafanaRunning) {
                        echo 'Grafana is already running.'
                    } else {
                        echo 'Starting Grafana container...'
                        sh 'docker start grafana'
                    }
                }
            }
        }

        stage('Operate: Monitor - Validate Setup') {
            steps {
                script {
                    echo 'Validating Prometheus and Grafana setup...'
                    sh 'curl -f http://localhost:9090/ || echo "Prometheus is not accessible"'
                    sh 'curl -f http://localhost:3000/ || echo "Grafana is not accessible"'
                }
            }
        }

        stage('Security Testing - ZAP Baseline Scan') {
            steps {
                script {
                    def result = sh(script: '''
                        sudo docker run --rm -v /var/lib/jenkins/workspace/sonar/zap_results:/zap/wrk -t zaproxy/zap-stable zap-baseline.py -t http://192.168.33.10:8089/tpfoyer/etudiant/add-etudiant -g /zap/wrk/gen.conf -r /zap/wrk/baseline_scan_report.html
                        sudo chmod -R 777 /var/lib/jenkins/workspace/sonar/zap_results
                    ''', returnStatus: true)

                    if (result != 0) {
                        echo "ZAP Baseline Scan completed with warnings or errors."
                    } else {
                        echo "ZAP Baseline Scan completed successfully."
                    }
                }
            }
        }

        stage('Security Testing - ZAP Active Scan') {
            steps {
                script {
                    def result = sh(script: '''
                        sudo docker run --rm -v /var/lib/jenkins/workspace/sonar/zap_results:/zap/wrk -t zaproxy/zap-stable zap-full-scan.py -t http://192.168.33.10:8089/tpfoyer/etudiant/add-etudiant -g /zap/wrk/gen.conf -r /zap/wrk/active_scan_report.html
                        sudo chmod -R 777 /var/lib/jenkins/workspace/sonar/zap_results
                    ''', returnStatus: true)

                    if (result != 0) {
                        echo "ZAP Active Scan completed with warnings or errors."
                    } else {
                        echo "ZAP Active Scan completed successfully."
                    }
                }
            }
        }

        stage('Security Testing - Publish ZAP Reports') {
            steps {
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

        stage('Security Testing - Nmap Scan Attack') {
            steps {
                script {
                    sh 'sudo gauntlt /var/lib/jenkins/workspace/sonar/gauntlt-attacks/nmap.attack > nmap_output.txt'
                    archiveArtifacts artifacts: 'nmap_output.txt', allowEmptyArchive: true
                }
            }
        }

        stage('Security Testing - SQL Injection Attack (Gauntlt)') {
            steps {
                script {
                    sh 'gauntlt /var/lib/jenkins/workspace/sonar/gauntlt-attacks/sql_in.attack > sql_injection_output.txt'
                    archiveArtifacts artifacts: 'sql_injection_output.txt', allowEmptyArchive: true
                }
            }
        }

        stage('Security Testing - SQL Injection Test (SQLmap)') {
            steps {
                script {
                    sh 'python3 /var/lib/jenkins/workspace/sonar/gauntlt-attacks/sqlmap/sqlmap.py -u "http://192.168.33.10:8089/tpfoyer/etudiant/add-etudiant" --data="nomEtudiant=Robert&prenomEtudiant=Test&cinEtudiant=123456&dateNaissance=2000-01-01" --batch --level=5 --risk=3 --tamper=space2comment > sqlmap_output.txt'
                    archiveArtifacts artifacts: 'sqlmap_output.txt', allowEmptyArchive: true
                }
            }
        }

       
    }

    
}
