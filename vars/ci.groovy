def call()
{
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
                        echo 'unit tests'
                    }
        }
        stage('Quality control') {
            steps {
                echo 'Quality control'
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