pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'  // Adjust if necessary
        maven 'M2_HOME'  // Adjust if necessary
    }

    stages {
        stage('Build Docker Image') {
             steps {
                sh 'sudo docker build -t rymasd29/tp-foyer:5.0.0 .' 
            }
         }
    }
}
