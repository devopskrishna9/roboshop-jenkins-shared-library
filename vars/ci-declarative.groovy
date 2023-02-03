def call() {
    try {
        node('workstation') {
            stage('Compile/Build') {
                common.compile()
            }

            stage('Unit tests') {
                common.unittests()
            }

            stage('Quality control') {
                SONAR_PASS = sh(script: 'aws ssm get-parameters --region us-east-1 --names sonarqube.pass --with-decryption --query Parameters[0].Value | sed \'s/"//g\'', returnStdout: true).trim()
                SONAR_USER = sh(script: 'aws ssm get-parameters --region us-east-1 --names sonarqube.user --with-decryption --query Parameters[0].Value | sed \'s/"//g\'', returnStdout: true).trim()
                wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: "${SONAR_PASS}", var: 'SECRET']]]) {
                    sh "sonar-scanner -Dsonar.host.url=http://172.31.12.1:9000 -Dsonar.login=${SONAR_USER} -Dsonar.password=${SONAR_PASS} -Dsonar.projectKey=cart"
                    // sh "echo Sonar Scan"
                }

            }

            stage('Upload Code to Centralized Place') {
                echo 'upload'
            }
        }
    } catch(Exception e) {
        common.email("Failed")
      }
 }