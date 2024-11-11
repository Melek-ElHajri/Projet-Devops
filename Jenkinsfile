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
             stage('Dependency Check') {
            steps {
                dependencyCheck additionalArguments: '--failOnCVSS 7 --out target/dependency-check-report --noupdate', 
                               odcInstallation: 'Dependency-Check'
            }
        }
        }}
