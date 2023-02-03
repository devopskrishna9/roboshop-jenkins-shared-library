def call() {
    try {
        node('workstation') {
            stage('Cleanup') {
                cleanWS()
            }

            stage('Compile/Build') {
                common.compile()
            }

            stage('Unit tests') {
                common.unittests()
            }

            stage('Quality Control') {
                environment {
                    SONAR_USER = '$(aws ssm get-parameters --region us-east-1 --names sonarqube.user  --with-decryption --query Parameters[0].Value | sed \'s/"//g\')'
                    echo sonar user is ${SONAR_USER}
                    //SONAR_PASS = '$(aws ssm get-parameters --region us-east-1 --names sonarqube.pass  --with-decryption --query Parameters[0].Value | sed \'s/"//g\')'
                }
//                steps {
//                    script {
//                        SONAR_PASS = sh ( script: 'aws ssm get-parameters --region us-east-1 --names sonarqube.pass  --with-decryption --query Parameters[0].Value | sed \'s/"//g\'', returnStdout: true).trim()
//                        wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: "${SONAR_PASS}", var: 'SECRET']]]) {
//                            sh "sonar-scanner -Dsonar.host.url=http://172.31.11.33:9000 -Dsonar.login=${SONAR_USER} -Dsonar.password=${SONAR_PASS} -Dsonar.projectKey=cart"
//                        }
//                    }
//                }
            }

            stage('Upload Code to Centralized Place') {
                echo 'upload'
            }
        }
    } catch(Exception e) {
        common.email("Failed")
      }
 }