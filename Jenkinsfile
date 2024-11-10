pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'  // Adjust if necessary
        maven 'M2_HOME'  // Adjust if necessary
    }

    stages {
        stage('GIT') {
            steps {
                git branch: 'NouhaSedraoui', url: 'https://github.com/Melek-ElHajri/Projet-Devops.git'
            }
        }

       
        
       stage('Nmap Scan Attack') {
            steps {
                script {
                    // Run Gauntlt Nmap attack and redirect output to a file
                    sh 'gauntlt nmap.attack > nmap_output.txt'
                    
                    // Archive the output file
                    archiveArtifacts artifacts: 'nmap_output.txt', allowEmptyArchive: true
                }
            }
        }

        stage('SQL Injection Attack (Gauntlt)') {
            steps {
                script {
                    // Run Gauntlt SQL Injection attack and redirect output to a file
                    sh 'gauntlt sql_in.attack > sql_injection_output.txt'
                    
                    // Archive the output file
                    archiveArtifacts artifacts: 'sql_injection_output.txt', allowEmptyArchive: true
                }
            }
        }

        stage('SQL Injection Test (SQLmap)') {
            steps {
                script {
                    // Run SQLmap for deeper SQL injection testing and redirect output to a file
                    sh 'python3 /path/to/sqlmap/sqlmap.py -u "http://192.168.33.10:8089/tpfoyer/etudiant/add-etudiant" --data="nomEtudiant=Robert&prenomEtudiant=Test&cinEtudiant=123456&dateNaissance=2000-01-01" --batch --level=5 --risk=3 --tamper=space2comment > sqlmap_output.txt'
                    
                    // Archive the output file
                    archiveArtifacts artifacts: 'sqlmap_output.txt', allowEmptyArchive: true
                }
            }
        }
    }
  

}
