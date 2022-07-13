pipeline {

    agent any
    environment {
        registry = "grijeshsaini/local-services"
        registryCredential = 'dockerhub'
    }

    stages {
        stage('build & test') {
            agent { docker { image 'gradle:7.3.3-jdk17' } }
            steps {
                sh 'gradle build'
            }
        }

        stage('build & push docker image') {
            agent any
            steps {
                script {
                    def image = docker.build registry + ":CRICKET_CLUB_$BUILD_NUMBER"
                    image.push()
                }
            }
        }
    }
}
