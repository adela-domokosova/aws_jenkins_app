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
                                    echo '🔑 Nastavuji práva pro mvnw...'
                                    sh 'chmod +x ../mvnw'

                                    echo '🧹 Odstraňuji Maven wrapper cache...'
                                    sh 'rm -rf ~/.m2/wrapper/'

                                    echo '📌 Ověřuji volné místo na disku...'
                                    sh 'df -h'

                                    echo '📌 Ověřuji paměť systému...'
                                    sh 'free -m'

                                    echo '📌 Čistím Maven cache a repositář...'
                                    sh 'rm -rf ~/.m2/repository/'

                                    echo '🔧 Nastavuji práva pro složku target/...'
                                    sh 'mkdir -p target && chmod -R 777 target'

                                    echo '🚀 Spouštím Maven build bez testů...'
                                    sh 'mvn clean package -B -DskipTests'

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