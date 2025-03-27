pipeline {
    agent any

    environment {
            GIT_REPO = 'https://github.com/adela-domokosova/aws_jenkins_app.git'
        }

    stages {

        stage('Checkout') {
                    steps {
                        git branch: 'main', url: env.GIT_REPO
                        echo '📂 Pracovní adresář:'
                        sh 'pwd'
                    }
                }

        stage('Build Server') {
                    steps {
                        dir('server') {
                                    echo '📂 Pracovní adresář:'
                                    sh 'pwd'

                                    echo '📜 Obsah adresáře před změnami:'
                                    sh 'ls -l'

                                    echo '🔑 Nastavuji práva pro mvnw...'
                                    sh 'chmod +x ../mvnw'

                                    echo '📜 Kontrola práv souboru mvnw:'
                                    sh 'ls -l ../mvnw'

                                    echo '🧹 Odstraňuji Maven wrapper cache...'
                                    sh 'rm -rf ~/.m2/wrapper/'

                                    echo '🚀 Spouštím Maven build bez testů...'
                                    sh '../mvnw clean package -B -X -DskipTests'

                                    echo '📜 Obsah adresáře po buildu:'
                                    sh 'ls -l target/'
                                }

                    }
                }

                stage('Build Client') {
                    steps {
                        dir('client') {
                            echo '📂 Pracovní adresář:'
                             sh 'pwd'
                        }
                    }
                }

        stage('Test Server') {
            steps {
                dir('client') {
                    echo '📂 Pracovní adresář:'
                    sh 'pwd'
                   }
            }
        }


    stage('Test Client') {
        steps {
            dir('client') {
                echo '📂 Pracovní adresář:'
                sh 'pwd'
               }
        }
    }




    }




}