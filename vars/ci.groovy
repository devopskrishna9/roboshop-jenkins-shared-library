def call() {
    try {
        pipeline {
            agent {
                label 'workstation'
            }
            stages {
                stage('compile/Build') {
                    steps {
                        script {
                            echo 'compile the code'
                            common.compile()
                        }
                    }
                }
                stage('unit test cases') {
                    steps {
                        script {
                            echo 'unit tests'
                            common.unittests()
                        }
                    }
                }
                stage('Quality control') {
                    steps {
                        echo 'Quality control'
                        SONAR_PASS = sh(script: 'aws ssm get-parameters --region us-east-1 --names sonarqube.pass  --with-decryption --query Parameters[0].Value | sed \'s/"//g\'', returnStdout: true).trim()
                        SONAR_USER = sh(script: 'aws ssm get-parameters --region us-east-1 --names sonarqube.user  --with-decryption --query Parameters[0].Value | sed \'s/"//g\'', returnStdout: true).trim()
                        wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: "${SONAR_PASS}", var: 'SECRET']]]) {
                            sh "sonar-scanner -Dsonar.host.url=http://172.31.12.1:9000 -Dsonar.login=${SONAR_USER} -Dsonar.password=${SONAR_PASS} -Dsonar.projectKey=${component} "
                            sh "echo Sonar Scan"
                        }
                    }
                }
                    stage('upload code to centralized place') {
                        steps {
                            echo 'upload code'
                        }
                    }

                }
            }
        }
    } catch (Exception e) {
        common.email("failed")
    }
}
