#!/usr/bin/env groovy

def call(buildStatus, emailRecipients, customBody="") {
  try {

  def icon = "✅"
  def statusSuccess = true
  def hasApproval   = true
  def logUrl        = env.BUILD_URL + 'consoleText'

  if (customBody != "") {
    def content = customBody
  }
  else {
    def content = "\"${env.JOB_NAME}\" build: ${env.BUILD_NUMBER}\n\nView the log at:\n $logUrl"
  }

  if(buildStatus != "SUCCESSFUL") {
    icon = "❌"
    statusSuccess = false
    body = "Job Failed - $content"
  }
  else {
    body = "Job Success - $content\n\nApprove this deployment:\n${env.RUN_DISPLAY_URL}"
  }

  mail to: emailRecipients.join(","),
       subject: "${icon} [ ${env.JOB_NAME} ] [${env.BUILD_NUMBER}] - ${buildStatus} ",
       body: body

  } catch (e){
      println "ERROR SENDING EMAIL ${e}"
  }
}
