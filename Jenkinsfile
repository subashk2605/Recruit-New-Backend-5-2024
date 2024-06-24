pipeline {
    agent { label 'any' }
    stages {
        stage('Checkout') {
            steps {
                bat 'git checkout BoseK'
                bat 'git pull'
            }
        }
        stage('Build') {
            steps {
                dir('Recruit New Backend 5-2024') {
                    bat 'mvn clean package'
                }
            }
        }
     
     
        }
    }
