pipeline {
    agent any
    
    environment {
        SMTP_USERNAME = 'rim.gabsi.zg@gmail.com' 
        SMTP_PASSWORD = 'ufpt qsvd dvib kijw'    
    }
    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    stages {
      
        stage('GIT') {
            steps {
                git branch: 'Gabsi-Rim',
                    url: 'https://github.com/Melek-ElHajri/Projet-Devops.git'
            }
        }

       
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/Gabsi-Rim']], 
                          userRemoteConfigs: [[url: 'https://github.com/Melek-ElHajri/Projet-Devops.git']]])
            }
        }

       
        stage('Compile Stage') {   
            steps {
                sh 'mvn clean compile'
            }
        }

     
        stage('Mockito Tests') {
            steps {
                sh 'mvn test' 
            }
        }

     
        stage('Deploy to Nexus') {
            steps {
                sh 'mvn deploy -DskipTests -DaltDeploymentRepository=deploymentRepo::default::http://192.168.33.10:8081/repository/maven-releases/'
            }
        }

      
        stage('Sonarqube') {
            steps {
                withSonarQubeEnv('sq1') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

       
        stage("Quality Gate") {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true 
                }
            }
        }

       
        stage('Security Scan: Nmap') {
            steps {
                script {
                    echo "Démarrage du scan de sécurité Nmap..."
                    sh 'nmap -sT -p 1-65535 -v 192.168.33.10'
                }
            }
        }

       
        stage('Security Scan: Trivy') {
            steps {
                retry(3) {
                    echo "Scan des vulnérabilités dans l'image Docker avec Trivy..."
                    sh 'trivy image --no-progress --severity CRITICAL gabsirim/alpine:1.0.0'
                }
            }
        }

      
        stage('System Security Check - Lynis') {
            steps {
                script {
                   
                    sh 'lynis audit system | tee lynis_audit_output.txt'
                    
                  
                    archiveArtifacts artifacts: 'lynis_audit_output.txt', allowEmptyArchive: true
                }
            }
        }

      
        stage('Build Docker Image') {
            steps {  
                sh "docker build -t gabsirim/alpine:1.0.0 ."
            }
        } 

       
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
/**
        // Étape de démarrage des conteneurs en surveillance
        stage('Start Monitoring Containers') {
            steps {
                sh 'docker start be79135ec1cc'
            }
        }**/

      
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
