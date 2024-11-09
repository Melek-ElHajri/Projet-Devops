pipeline {
    agent any

    environment {
        dockerhub_token = credentials('dockerhub_token') 
        notify_token = credentials('NOTIFY_TOKEN')
    }

    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    stages {
        stage('GIT') {
            steps {
                git branch: 'ElHedi-Melek-Elhajri', 
                url: 'https://github.com/Melek-ElHajri/Projet-Devops.git'
            }
        } 
    stage('Clean') {
            steps {
                sh 'mvn clean'
            }
        }
    stage('Install') {
            steps {
                // Install dependencies and compile project
                sh 'mvn install'
            }
        }

    stage('Compile') {
            steps {
                sh 'mvn compile'
            }
        }
        /*

        stage('Compile Stage') {
            steps {
                sh 'mvn clean compile'
            }
        }
 /*
        stage('Scan') {
            steps {
                withSonarQubeEnv('sq') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                sh 'mvn deploy -DskipTests -DaltDeploymentRepository=deploymentRepo::default::http://192.168.10.2:8081/repository/maven-releases/'
            }
        }*/

        stage("Generate Docker Image") {
            steps {
                sh 'sudo chmod 666 /var/run/docker.sock'
                sh 'docker build -t m2l2k/tp-foyer:5.0.0 .'
            }
        }

        stage("Push Docker Image") {
            steps {
                sh "echo ${dockerhub_token} | docker login -u m2l2k --password-stdin" 
                sh "docker push m2l2k/tp-foyer:5.0.0"
            }
        }

        stage('Docker Compose') {
            steps {
                sh 'docker compose up -d'
            }
        }
    }

    // Uncomment the post block if you want notifications
    /*
    post {
        success {
            script {
                notifyEvents message: "<b>Build Success</b> - Job: ${env.JOB_NAME}, Build Number: ${env.BUILD_NUMBER}", 
                             token: env.notify_token
            }
        }
        
        failure {
            script {
                notifyEvents message: "<b>Build Failed</b> - Job: ${env.JOB_NAME}, Build Number: ${env.BUILD_NUMBER}", 
                             token: env.notify_token
            }
        }
    }
    */
}
