#!/usr/bin/env groovy

def archive_log() {
  script {
    withCredentials([usernamePassword(credentialsId: 'azure-storage', usernameVariable: 'AZURE_STORAGE_ACCOUNT', passwordVariable: 'AZURE_STORAGE_KEY')]) {
      def log = currentBuild.rawBuild.getLog()
      writeFile(file: 'buildlog.txt', text: log)
      def data = readFile(file: 'buildlog.txt')
      sh (script: 'az storage container create -n jenkinsarchivelogs', returnStdout: true)
      sh (script: 'az storage blob upload -c jenkinsarchivelogs -f ./buildlog.txt -n ${JOB_NAME}/${BUILD_NUMBER}.txt', returnStdout: true)
    }
  }
}
