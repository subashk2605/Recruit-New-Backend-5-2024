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
                bat 'C:\\Windows\\System32\\cmd.exe /c mvn clean package'
            }
        }
 
  stage('Stop Container') {
            steps {
                script {
                    try {
                        def oldContainerName = "recruit-springboot-app"

                        echo "Stopping and removing old container: ${oldContainerName}"
                        bat "docker stop ${oldContainerName} || echo 'Container ${oldContainerName} is not running'"
                        bat "docker rm ${oldContainerName} || echo 'Container ${oldContainerName} does not exist'"

                        echo "Removing old image: recruit-app"
                        bat "docker rmi recruit-app || echo 'Image recruit-app does not exist'"
                    } catch (Exception e) {
                        echo "Error occurred: ${e.message}"
                    }
                }
            }
        }
       stage('Docker Build and Run') {
            steps {
                script {
                    def containerName = 'recruit-springboot-app'
                    def imageName = 'recruit-app'
                    def imageTag = 'latest'
                    def portMapping = '9091:9091' // Adjust the port mapping as needed

                    // Build Docker image
                    bat "docker build -t ${imageName}:${imageTag} ."
                    
                    // Run the new container with environment variables for Spring Boot application
                    bat "docker run -d --name ${containerName} -p ${portMapping} -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/recruit_app -e SPRING_DATASOURCE_USERNAME=root -e SPRING_DATASOURCE_PASSWORD=343592 ${imageName}:${imageTag}"
                }
            }
        }
    
    }

