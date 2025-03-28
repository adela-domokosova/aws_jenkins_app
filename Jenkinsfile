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
                             sh 'docker build -t $DOCKER_IMAGE_CLIENT -f ./client/Dockerfile ./client'
                         }
                     }
                 }

                 stage('Run Containers') {
                     steps {
                         script {
                             sh 'docker run -d --name server-container -p 8081:8081 $DOCKER_IMAGE_SERVER'
                             sh 'docker run -d --name client-container -p 8082:8082 $DOCKER_IMAGE_CLIENT'
                         }
                     }
                 }

    }




}