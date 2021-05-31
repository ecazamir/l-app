pipeline {


    agent {
        node {
            def appserver = [:]
            appserver.name = 'localhost'
            appserver.host = 'localhost'
            appserver.user = 'ubuntu'
            appserver.password = 'password'
            appserver.allowAnyHosts = true
        }
        docker {
            image 'maven:3.8.1-openjdk-8'
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

        stage('Deploy') {
            sshCommand remote: appserver, command: "ls -lrt"
        }

        stage('Clean') {
            steps {
                sh 'mvn clean'
            }
        }
    }
}

