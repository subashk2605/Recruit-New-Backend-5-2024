pipeline {
    agent any

       environment {
        MAVEN_HOME = tool name: 'Maven', type: 'maven'
        JAVA_HOME = 'C:\\Program Files\\OpenLogic\\jdk-17.0.11.9-hotspot'
        GIT_HOME = 'C:\\Program Files\\Git\\bin'
        PATH = "${env.PATH};C:\\Windows\\System32;${env.JAVA_HOME}\\bin;${env.GIT_HOME}"
    }

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
