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
        sh 'npm test'
    }

    if (app_lang == "maven") {
        sh 'mvn test'
    }

    if (app_lang == "maven") {
        sh 'python3 -m unittest'
    }
}