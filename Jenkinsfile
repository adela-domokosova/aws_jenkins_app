pipeline {
    agent any

    environment {
            GIT_REPO = 'https://github.com/adela-domokosova/aws_jenkins_app.git'
            DOCKER_IMAGE_SERVER = "server:latest"
            DOCKER_IMAGE_CLIENT = "client:latest"
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
                                    sh 'chmod +x ../mvnw'

                                    sh 'mkdir -p target && chmod -R 777 target'

                                    sh 'mvn clean package -B -DskipTests'

                                }

                    }
                }

                stage('Build Client') {
                    steps {
                        dir('client') {
                              sh 'chmod +x ../mvnw'

                              sh 'mkdir -p target && chmod -R 777 target'

                              sh 'mvn clean package -B -DskipTests'

                        }
                    }
                }


    stage('Build Docker Images') {
                     steps {
                         script {
                             sh 'docker build -t $DOCKER_IMAGE_SERVER -f ./server/Dockerfile ./server'
                             sh 'docker build -t $DOCKER_IMAGE_CLIENT -f ./server/Dockerfile ./client'
                         }
                     }
                 }

                 stage('Run Containers') {
                     steps {
                         script {
                             sh 'docker run -d --name server-container -p 8080:5000 $DOCKER_IMAGE_SERVER'
                             sh 'docker run -d --name client-container -p 8081:5001 $DOCKER_IMAGE_CLIENT'
                         }
                     }
                 }

    }




}