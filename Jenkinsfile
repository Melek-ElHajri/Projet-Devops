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

         stage('Testing - OWASP Dependency-Check Vulnerabilities') {
            steps {
                    dependencyCheck additionalArguments: '--failOnCVSS 7 --out target/dependency-check-report --noupdate', 
                               odcInstallation: 'Dependency-Check'
                    dependencyCheckPublisher pattern: 'target/dependency-check-report/dependency-check-report.xml'
    }
}


       stage('Deployment - Deploy to Nexus') {
            steps {
                // Deploy to Nexus repository
                sh 'mvn deploy -DskipTests -Dautoupdate=false -DaltDeploymentRepository=deploymentRepo::default::http://192.168.33.10:8081/repository/maven-releases/'
            }
        }

    }}
