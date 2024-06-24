pipeline {
    agent any  // Run on any available agent (optional)
       environment {
        MAVEN_HOME = tool name: 'Maven', type: 'maven'
        JAVA_HOME = 'C:\\Program Files\\OpenLogic\\jdk-17.0.11.9-hotspot'
        GIT_HOME = 'C:\\Program Files\\Git\\bin'
        PATH = "${env.PATH};C:\\Windows\\System32;${env.JAVA_HOME}\\bin;${env.GIT_HOME}"
    }
    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'BoseK', credentialsId: 'c7874b7f-40e7-469e-8dce-daf061a2b11a', url: 'https://github.com/subashk2605/Recruit-New-Backend-5-2024.git'
            }
        }
        stage('Build') {
            steps {
                // Option 1: Using full path for cmd 
              bat 'C:\\Windows\\System32\\cmd.exe /c mvn clean package'
                // Option 2: Using sh for shell script
                // sh 'mvn clean package'
            }
        }
    }
}
