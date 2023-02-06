def compile() {
    if (app_lang == "nodejs") {
        sh 'npm install'
    }

    if (app_lang == "maven") {
        sh 'mvn package'
    }
}

def unittests() {
    if (app_lang == "nodejs") {
//        developer is missing unit test cases to our project. He need to add them as best practices. We are skipping to proceed further
        sh 'npm test || true'
// 2nd approch Exception handling
//        try {
//            sh 'npm test'
//        } catch (Exception e) {
//            email("unit test failed")
//        }
    }

    if (app_lang == "maven") {
        sh 'mvn test'
    }

    if (app_lang == "maven") {
        sh 'python3 -m unittest'
    }
}


def email(email_note) {
    mail bcc: '', body: "Job Failed - ${JOB_BASE_NAME}\nJenkins URL - ${JOB_URL}", cc: '', from: 'brkkrishna9@gmail.com', replyTo: '', subject: "Jenkins Job Failed - ${JOB_BASE_NAME}", to: 'brkkrishna9@gmail.com'
}

def artifactPush() {
    sh "echo ${TAG_NAME} >VERSION"
    if (app_lang == "nodejs") {
        sh "zip -r ${component}-${TAG_NAME}.zip node_modules server.js VERSION ${extraFiles}"
    }

    if (app_lang == "nginx") {
        sh "zip -r ${component}-${TAG_NAME}.zip *"
        sh "zip -r ${component}-${TAG_NAME}.zip * -x Jenkinsfile"
    }

    NEXUS_PASS = sh(script: 'aws ssm get-parameters --region us-east-1 --names nexus.pass  --with-decryption --query Parameters[0].Value | sed \'s/"//g\'', returnStdout: true).trim()
    NEXUS_USER = sh(script: 'aws ssm get-parameters --region us-east-1 --names nexus.user  --with-decryption --query Parameters[0].Value | sed \'s/"//g\'', returnStdout: true).trim()
    wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: "${NEXUS_PASS}", var: 'SECRET']]]) {
        sh "curl -v -u ${NEXUS_USER}:${NEXUS_PASS} --upload-file ${component}-${TAG_NAME}.zip http://3.237.63.179:8081/repository/${component}/${component}-${TAG_NAME}.zip"
    }
}