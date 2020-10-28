#!/usr/bin/env groovy

def call(buildStatus, emailRecipients) {
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

  mail to: emailRecipients,
  subject: "Example Build: ${env.JOB_NAME} - Success",
  body: body,
}
