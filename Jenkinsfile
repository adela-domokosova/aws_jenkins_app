pipeline {
    agent any

    environment {
            GIT_REPO = 'https://github.com/adela-domokosova/aws_jenkins_app.git'
        }

    stages {

        stage('Checkout') {
                    steps {
                        git branch: 'main', url: env.GIT_REPO
                    }
                }

        stage('Build Server') {
                    steps {
                        dir('server') {
                            sh '../../mvnw clean package'
                        }
                    }
                }

                stage('Build Client') {
                    steps {
                        dir('client') {
                            sh '../../mvnw clean package'
                        }
                    }
                }

        stage('Test Server') {
            steps {
                dir('server') { // Jdeme do složky `server`
                    sh '../../mvnw test' // Spouštíme testy
                }
            }
        }


    stage('Test Client') {
        steps {
            dir('client') { // Jdeme do složky `client`
                sh '../../mvnw test' // Spouštíme testy
            }
        }
    }

    stage('Build Docker Images') {
                steps {
                    script {
                        sh 'docker build -t $DOCKER_IMAGE_SERVER ./server'
                        sh 'docker build -t $DOCKER_IMAGE_CLIENT ./client'
                    }
                }
            }

            stage('Run Containers') {
                steps {
                    script {
                        sh 'docker run -d --name server-container -p 8081:8080 $DOCKER_IMAGE_SERVER'
                        sh 'docker run -d --name client-container -p 8082:8080 $DOCKER_IMAGE_CLIENT'
                    }
                }
            }



    }




}