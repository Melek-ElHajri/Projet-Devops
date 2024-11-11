pipeline {
    agent any
    
    environment {
        SMTP_USERNAME = 'rim.gabsi.zg@gmail.com'  // remplacez par votre adresse Gmail
        SMTP_PASSWORD = 'ufpt qsvd dvib kijw'    // remplacez par votre mot de passe d'application Gmail
    }
    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    stages {
        // Étape de récupération du code source
        stage('GIT') {
            steps {
                git branch: 'Gabsi-Rim',
                    url: 'https://github.com/Melek-ElHajri/Projet-Devops.git'
            }
        }

        // Étape de vérification du code source à partir du dépôt Git
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/Gabsi-Rim']], 
                          userRemoteConfigs: [[url: 'https://github.com/Melek-ElHajri/Projet-Devops.git']]])
            }
        }

        // Étape de compilation du projet
        stage('Compile Stage') {   
            steps {
                sh 'mvn clean compile'
            }
        }

        // Étape d'exécution des tests unitaires avec Mockito
        stage('Mockito Tests') {
            steps {
                sh 'mvn test' 
            }
        }

        // Étape d'analyse de la qualité du code avec SonarQube
        stage('Sonarqube') {
            steps {
                withSonarQubeEnv('sq1') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        // Étape de validation de la qualité du code avec Quality Gate
        stage("Quality Gate") {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true 
                }
            }
        }

        // Étape de scan de sécurité avec Nmap
        stage('Security Scan: Nmap') {
            steps {
                script {
                    echo "Démarrage du scan de sécurité Nmap..."
                    sh 'nmap -sT -p 1-65535 -v localhost'
                }
            }
        }

        // Étape de scan de sécurité avec Trivy (pour les images Docker)
        stage('Security Scan: Trivy') {
            steps {
                retry(3) {
                    echo "Scan des vulnérabilités dans l'image Docker avec Trivy..."
                    sh 'trivy image --no-progress --severity CRITICAL gabsirim/alpine:1.0.0'
                }
            }
        }

        // Étape de vérification de la sécurité système avec Lynis
        stage('System Security Check - Lynis') {
            steps {
                script {
                    // Exécution de l'audit de sécurité système avec Lynis
                    sh 'lynis audit system | tee lynis_audit_output.txt'
                    
                    // Archivage des résultats pour consultation ultérieure
                    archiveArtifacts artifacts: 'lynis_audit_output.txt', allowEmptyArchive: true
                }
            }
        }

        // Étape de création de l'image Docker
        stage('Build Docker Image') {
            steps {  
                sh "docker build -t gabsirim/alpine:1.0.0 ."
            }
        }

        // Étape de push de l'image Docker vers Docker Hub
        stage('Push Docker Image') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials-id', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        sh "echo \$DOCKER_PASSWORD | docker login -u \$DOCKER_USERNAME --password-stdin"
                    }
                    sh 'docker push gabsirim/alpine:1.0.0'
                }
            }
        }

        // Étape de déploiement du projet vers Nexus
        stage('Deploy to Nexus') {
            steps {
                sh 'mvn deploy -DskipTests -DaltDeploymentRepository=deploymentRepo::default::http://192.168.33.10:8081/repository/maven-releases/'
            }
        }

        // Étape de démarrage de Docker Compose pour la gestion des conteneurs
        stage('Run Docker Compose') {
            steps {
                script {
                    sh '''
                        pwd
                        ls -la
                        docker-compose down -v
                        docker-compose up -d
                        docker-compose ps
                    '''
                }
            }
        }

        // Étape d'envoi de notification par email à la fin du pipeline
        stage('Email Notification') {
            steps {
                mail bcc: '',
                     body: 'Rapport final : Le pipeline a été exécuté avec succès. Aucune action requise.',
                     cc: '',
                     from: '',
                     replyTo: '',
                     subject: 'Pipeline DevOps Project exécutée avec succès',
                     to: 'rim.gabsi.zg@gmail.com, rim.gabsi@esprit.tn'
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
        always {
            script {
                emailext (
                    subject: "Build Notification: ${currentBuild.fullDisplayName}",
                    body: "Consultez les détails du build à ${env.BUILD_URL}",
                    recipientProviders: [[$class: 'CulpritsRecipientProvider'], [$class: 'DevelopersRecipientProvider']],
                    to: 'rim.gabsi.zg@gmail.com, rim.gabsi@esprit.tn'
                )
            }
        }
    }
}
