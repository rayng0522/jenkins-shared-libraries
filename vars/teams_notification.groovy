#!/usr/bin/env groovy

def call(buildStatus, customBody="", hasApproval=false) {
  try {

  def icon = "✅"
  def message       = customBody
  def color         = "05b222"

  if(buildStatus != "SUCCESSFUL") {
    icon    = "❌"
    status  = "Job Failed"
    color   = "d00000"
  }
  else {
    if (hasApproval) {
      status  = "Job Success - Approve this deployment: [click me](${env.RUN_DISPLAY_URL})"
    }
    else {
      status  = "Job Success"
    }
  }

  if (message == "") {
    message = "${icon} started ${env.JOB_NAME} and Build# ${env.BUILD_NUMBER}"
  }

  office365ConnectorSend message: message,
                         status: status,
                         color: color,
                         webhookUrl: "${env.TEAMS_WEBHOOK_URL}"


  } catch (e){
      println "ERROR SENDING MESSAGE TO TEAMS ${e}"
  }
}
