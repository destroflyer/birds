node {
    try {
        stage('Checkout') {
            checkout scm
        }
        stage('Build') {
            sh 'mvn clean install'
        }
    } finally {
        step([$class: 'GitHubCommitStatusSetter'])
    }
}