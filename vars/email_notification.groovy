#!/usr/bin/env groovy

def call(buildStatus, emailRecipients, customBody="", hasApproval=false) {
  try {

  def icon = "✅"
  def statusSuccess = true
  def hasApproval   = true
  def logUrl        = env.BUILD_URL + 'consoleText'
  def content       = customBody

  if (content == "") {
    content = "\"${env.JOB_NAME}\" build: ${env.BUILD_NUMBER}\n\nView the log at:\n $logUrl"
  }

  if(buildStatus != "SUCCESSFUL") {
    icon = "❌"
    statusSuccess = false
    body = "Job Failed - $content"
  }
  else {
    if (hasApproval) {
      body = "Job Success - $content\n\nApprove this deployment:\n${env.RUN_DISPLAY_URL}"
    }
    else {
      body = "Job Success - $content"
    }
  }

  mail to: emailRecipients.join(","),
       subject: "${icon} [ ${env.JOB_NAME} ] [${env.BUILD_NUMBER}] - ${buildStatus} ",
       body: body

  } catch (e){
      println "ERROR SENDING EMAIL ${e}"
  }
}
