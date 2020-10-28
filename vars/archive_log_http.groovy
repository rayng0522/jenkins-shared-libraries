#!/usr/bin/env groovy

def call(String name = 'jenkins') {
  script {
    withCredentials([usernamePassword(credentialsId: 'azure-storage', usernameVariable: 'AZURE_STORAGE_ACCOUNT', passwordVariable: 'AZURE_STORAGE_KEY')]) {
      def logUrl = 'https://jenkins.aks-lb1-sgrtss-prd-az1-iqxcpu.pru.intranet.asia/job/RT-SRE/job/NPRD/job/users/job/servian/job/jenkins-agent-image-factory/41/consoleText'
      def cmd = 'curl -u ${JENKINS_AUTH} -sk -x --request GET ' + logUrl
      echo logUrl
      def log = sh(returnStdout: true, script: cmd)
      writeFile(file: 'buildlog.txt', text: log)
      env.CONTAINER = name
      sh (script: 'az storage container create -n ${CONTAINER}', returnStdout: true)
      sh (script: 'az storage blob upload -c ${CONTAINER} -f ./buildlog.txt -n ${JOB_NAME}/${BUILD_NUMBER}.txt', returnStdout: true)
    }
  }
}
