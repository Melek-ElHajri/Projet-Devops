pipeline {
    agent any

    environment {
        // Définition des variables d'environnement
        dockerhub_token = credentials('dockerhub_token')  // Jeton DockerHub
        notify_token = credentials('NOTIFY_TOKEN')        // Jeton de notification
    }

    tools {
        // Définition des outils nécessaires pour le pipeline
        jdk 'JAVA_HOME'  // Configuration de JDK
        maven 'M2_HOME'  // Configuration de Maven
    }

    stages {
        stage('GIT') {
            steps {
                // Clonage du dépôt Git
                git branch: 'ElHedi-Melek-Elhajri',
                    url: 'https://github.com/Melek-ElHajri/Projet-Devops.git'
            }
        }
        
        stage('Build') {
            steps {
                // Exécution de la commande Maven pour nettoyer, compiler et construire le projet
                sh 'mvn clean install compile'
            }
        }

        stage('Pre-commit Security Hooks') {
            steps {
                script {
                    // Création du rapport de pre-commit
                    def reportFile = 'pre_commit_report.log'

                    def result = sh(script: """
                        // Vérification et installation de pre-commit
                        if ! command -v pre-commit &> /dev/null; then
                            echo "pre-commit is not installed, installing in a virtual environment..." >> ${reportFile}
                            python3 -m venv venv
                            . venv/bin/activate
                            pip install pre-commit
                        else
                            echo "pre-commit is already installed." >> ${reportFile}
                        fi
                        git config --unset-all core.hooksPath >> ${reportFile}
                        pre-commit install >> ${reportFile}
                        pre-commit run --all-files >> ${reportFile} 2>&1
                        deactivate >> ${reportFile}
                    """, returnStatus: true)

                    // Affichage des résultats du pre-commit
                    if (result != 0) {
                        echo "Pre-commit hooks did not pass, but continuing pipeline."
                    } else {
                        echo "Pre-commit hooks passed successfully."
                    }

                    // Archivage du rapport de pre-commit
                    archiveArtifacts artifacts: reportFile, allowEmptyArchive: true
                }
            }
        }

        stage('Testing - JUnit, Mockito, and JaCoCo Tests') {
            steps {
                // Exécution des tests JUnit et Mockito
                sh 'mvn test'
                // Vérification de la présence du rapport JaCoCo
                sh 'ls -R target/site/jacoco || echo "JaCoCo report directory not found"'
            }
        }

        stage('Testing - JaCoCo Report Generation') {
            steps {
                script {
                    // Génération du rapport JaCoCo
                    jacoco(
                        execPattern: '**/target/jacoco.exec',
                        classPattern: '**/target/classes',
                        sourcePattern: '**/src/main/java'
                    )
                }
            }
        }

        stage('Scan') {
            steps {
                // Démarrage du conteneur SonarQube si nécessaire
                sh '''
                    if ! docker ps | grep 656251e296fb > /dev/null; then
                        echo "SonarQube container is not running. Starting SonarQube container..."
                        docker start 656251e296fb
                        sleep 30  # Wait for the container to be fully up
                    else
                        echo "SonarQube container is already running."
                    fi
                '''
                // Lancement de l'analyse SonarQube
                withSonarQubeEnv('sq') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                // Démarrage du conteneur Nexus si nécessaire
                sh '''
                    if ! docker ps | grep a5b6a466786c > /dev/null; then
                        echo "Container is not running. Starting container..."
                        docker start a5b6a466786c
                        sleep 35  # Wait for the container to be fully up
                    else
                        echo "Container is already running."
                    fi
                '''
                // Déploiement sur Nexus
                sh 'mvn deploy -DskipTests -DaltDeploymentRepository=deploymentRepo::default::http://192.168.10.2:8081/repository/maven-releases/'
            }
        }

        stage('Generate Docker Image') {
            steps {
                // Génération de l'image Docker
                sh 'docker build -t m2l2k/tp-foyer:5.0.0 .'
            }
        }

        stage('Push Docker Image') {
            steps {
                // Connexion à DockerHub et push de l'image
                sh "echo ${dockerhub_token} | docker login -u m2l2k --password-stdin" 
                sh "docker push m2l2k/tp-foyer:5.0.0"
            }
        }

        stage('Docker Compose') {
            steps {
                // Fermeture des services Docker et démarrage via Docker Compose
                sh 'docker-compose down'
                sh 'docker compose up -d'
            }
        }

        stage('Nmap Scan') {
            steps {
                script {
                    // Exécution d'un scan rapide avec Nmap
                    def targetHost = '192.168.10.2'  
                    echo "Running quick Nmap scan on ${targetHost}:8089"
                    sh "nmap -p 8089 -T4 -n -Pn ${targetHost} -oN nmap_quick_scan_report.txt"
                }
            }
            post {
                always {
                    // Archivage du rapport Nmap
                    archiveArtifacts artifacts: 'nmap_quick_scan_report.txt', allowEmptyArchive: true
                    echo "Quick Nmap scan report has been archived."
                }
            }
        }

        stage('ZAP Baseline Scan') {
            steps {
                script {
                    // Exécution d'un scan de base avec ZAP
                    def result = sh(script: '''
                        docker run --rm -v /var/lib/jenkins/workspace/nmap/zap_results:/zap/wrk -t zaproxy/zap-stable zap-baseline.py -t http://192.168.10.2:8089/tpfoyer/etudiant/add-etudiant -g /zap/wrk/gen.conf -r /zap/wrk/baseline_scan_report.html
                        chmod -R 777 /var/lib/jenkins/workspace/nmap/zap_results
                    ''', returnStatus: true)

                    // Gestion des erreurs et succès de ZAP
                    if (result != 0) {
                        echo "ZAP Baseline Scan completed with warnings or errors."
                    } else {
                        echo "ZAP Baseline Scan completed successfully."
                    }
                }
            }
        }

        stage('Publish ZAP Reports') {
            steps {
                // Publication des rapports ZAP
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

        stage('SQL Injection Test (SQLmap)') {
            steps {
                script {
                    // Test d'injection SQL avec SQLmap
                    sh '''
                        python3 /var/lib/jenkins/workspace/nmap/gauntlt-attacks/sqlmap/sqlmap.py \
                        -u "http://192.168.10.2:8089/tpfoyer/etudiant/add-etudiant" \
                        --data="nomEtudiant=Robert&prenomEtudiant=Test&cinEtudiant=123456&dateNaissance=2000-01-01" \
                        --batch --level=5 --risk=3 --tamper=space2comment | tee sqlmap_output.txt
                    '''
                    // Archivage du rapport SQLmap
                    archiveArtifacts artifacts: 'sqlmap_output.txt', allowEmptyArchive: true
                }
            }
        }
    }

    /* 
    post {
        success {
            // Notification en cas de succès
            script {
                notifyEvents message: "<b>Build Success</b> - Job: ${env.JOB_NAME}, Build Number: ${env.BUILD_NUMBER}", 
                             token: env.notify_token
            }
        }

        failure {
            // Notification en cas d'échec
            script {
                notifyEvents message: "<b>Build Failed</b> - Job: ${env.JOB_NAME}, Build Number: ${env.BUILD_NUMBER}", 
                             token: env.notify_token
            }
        }
    }
    */
}
