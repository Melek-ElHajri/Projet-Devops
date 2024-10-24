pipeline {
    agent any

    stages {
        stage('Checkout Git') {
            steps {
                // Checkout the code from the new repository
                git credentialsId: 'cred-github', 
                    branch: 'nourhene-chammakhi', // Change branch name
                    url: 'https://github.com/Melek-ElHajri/Projet-Devops.git' // New repository
            }
        }

        stage('Compiling') {
            steps {
                // Compile the project using Maven
                sh 'mvn clean compile'
            }
        }

        stage('SonarQube') {
            steps {
                // Run SonarQube analysis using Maven
                sh 'mvn sonar:sonar -Dsonar.login=admin -Dsonar.password=201JFT3926nourhene*'
            }
        }
    }
}
