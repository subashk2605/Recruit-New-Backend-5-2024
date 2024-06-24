pipeline {
    agent any
    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'BoseK', credentialsId: 'c7874b7f-40e7-469e-8dce-daf061a2b11a', url: 'https://github.com/subashk2605/Recruit-New-Backend-5-2024.git'
            }
        }
        stage('Build') {
            steps {
                bat 'mvn clean package'
            }
        }
    }
}
