pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    stages {
        stage('Package') {
            steps {
                sh 'mvn package'
            }
        }

}
}
