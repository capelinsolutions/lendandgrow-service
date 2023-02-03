pipeline {
    agent any
    stages {
        stage('check maven version') {
            steps {
                echo "I am making build of Hard Money Lending."
                git url: "https://qaximalee@bitbucket.org/socolkarachi/backend.git",
                credentialsId: 'qasim-balti'
                withSonarQubeEnv:('squ_f8967728e99e9a40cb729052c42607bd9b9b13c0')
                bat "mvnw sonar:sonar"
                bat "mvnw clean"
                bat "mvnw install"
                bat "java -jar target/HardMoneyLending.jar"
            }
        }
    }
}