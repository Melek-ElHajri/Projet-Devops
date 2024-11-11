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



       stage('Deployment - Deploy to Nexus') {
            steps {
                // Deploy to Nexus repository
                sh 'mvn deploy -DskipTests -Dautoupdate=false -DaltDeploymentRepository=deploymentRepo::default::http://192.168.33.10:8081/repository/maven-releases/'
            }
        }

    }}
