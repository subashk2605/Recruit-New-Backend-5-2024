pipeline {
    agent any  // Run on any available agent (optional)

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'BoseK', credentialsId: 'c7874b7f-40e7-469e-8dce-daf061a2b11a', url: 'https://github.com/subashk2605/Recruit-New-Backend-5-2024.git'
            }
        }
        stage('Build') {
            steps {
                // Option 1: Using full path for cmd 
                bat 'C:\Windows\System32\cmd.exe /c mvn clean package'  
                // Option 2: Using sh for shell script
                // sh 'mvn clean package'
            }
        }
    }
}
