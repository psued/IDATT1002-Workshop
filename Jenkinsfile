pipeline {
    agent any
    tools {
        maven 'M3'
    }
    stages {
        stage('Test') {
            steps{
                echo 'Test'
                sh 'docker-compose -f docker-compose-test.yaml up -d'
                withEnv(["DB_CHOICE=localhost"]) {
                    sh 'mvn clean test'
                }
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('Build') {
            steps{
                echo 'Build'
                sh 'mvn clean package -Dmaven.test.skip=true'
                sh 'docker build -t devops .'
            }
        }
        stage('Deploy') {
            steps{
                echo 'Deploy'
                sh 'docker-compose up -d'
            }
        }
    }
}