pipeline {
    agent any  // Utilisez soit "agent any" soit le bloc suivant si vous voulez spécifier un nœud avec un label :
    // agent {
    //     node {
    //         label 'build'
    //     }
    // }

    tools {
        maven 'M2_HOME'
    }

    options {
        // Timeout counter starts after agent is allocated
        timeout(time: 5, unit: 'MINUTES')
    }

    environment {
        APP_ENV = "DEV"
    }

    stages {
        stage('Code Checkout') {
            steps {
                git branch: 'Dhaoui-Badreddine',
                    url: 'https://github.com/Melek-ElHajri/Projet-Devops.git'
            }
        }

        stage('Code Build') {
            steps {
                sh 'mvn install -Dmaven.test.skip=true'
            }
        }
    }

    stage('Deploy to Nexus') {
            steps {
                // Check if the container is running, start it if not
                sh '''
                    if ! docker ps | grep a5b6a466786c > /dev/null; then
                        echo "Container is not running. Starting container..."
                        docker start a5b6a466786c
                        sleep 30  # Wait for the container to be fully up
                    else
                        echo "Container is already running."
                    fi
                '''
                
                sh 'mvn deploy -DskipTests -DaltDeploymentRepository=deploymentRepo::default::http://192.168.10.2:8081/repository/maven-releases/'
            }
        }

    post {
        always {
            echo "======always======"
        }
        success {
            echo "=====pipeline executed successfully ====="
        }
        failure {
            echo "======pipeline execution failed======"
        }
    }
}
