pipeline {

    agent {
        docker {
            image 'maven:3.8.1-openjdk-11'
            args '-v $HOME/.m2:/root/.m2'
        }
    }

    stages {

        stage('Compile') {
            steps {
                sh 'mvn compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*Tests.xml'
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package'
                archiveArtifacts artifacts: '**/target/*.war', fingerprint: true
            }
        }

        stage('Integration Test') {
            steps {
                sh 'mvn integration-test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*IT.xml'
                }
            }
        }

        stage('Clean') {
            steps {
                sh 'mvn clean'
            }
        }
    }
}

