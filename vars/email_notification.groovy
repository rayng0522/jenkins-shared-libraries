#!/usr/bin/env groovy

def call(buildStatus, emailRecipients) {
  try {

  def icon = "✅"
  def statusSuccess = true
  def hasApproval   = true
  def logUrl        = env.BUILD_URL + 'consoleText'
  def body = "Job Success - \"${env.JOB_NAME}\" build: ${env.BUILD_NUMBER}\n\nView the log at:\n $logUrl\n\nApprove this deployment:\n${env.RUN_DISPLAY_URL}"

  if(buildStatus != "SUCCESSFUL") {
    icon = "❌"
    statusSuccess = false
    body = "Job Failed - \"${env.JOB_NAME}\" build: ${env.BUILD_NUMBER}\n\nView the log at:\n $logUrl"
  }

  mail to: emailRecipients.join(","),
       subject: "${icon} [ ${env.JOB_NAME} ] [${env.BUILD_NUMBER}] - ${buildStatus} ",
       body: body

  } catch (e){
      println "ERROR SENDING EMAIL ${e}"
  }
}
