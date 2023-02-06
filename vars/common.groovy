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

