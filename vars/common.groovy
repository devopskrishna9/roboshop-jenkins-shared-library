def compile()
{
    if (app_lang == "nginx") {
        sh 'npm install'
    }
}