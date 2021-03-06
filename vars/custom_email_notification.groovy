#!groovy

package workflowlibs.manager;

import groovy.text.StreamingTemplateEngine

/**
 * This method returns a string with the template filled with groovy variables
 */
def emailTemplate(params) {

    def fileName = "email.html.groovy"
    def fileContents = libraryResource(fileName)
    def engine = new StreamingTemplateEngine()

    return engine.createTemplate(fileContents).make(params).toString()
}

/**
 * This method send an email generated with data from Jenkins
 * @param buildStatus String with job result
 * @param emailRecipients Array with emails: emailRecipients = []
 */
def call(buildStatus, emailRecipients, hasApproval=false) {

    try {

        def icon = "✅"
        def statusSuccess = true

        if(buildStatus != "SUCCESSFUL") {
            icon = "❌"
            statusSuccess = false
            hasApproval   = false
        }

        def body = emailTemplate([
            "jenkinsText"   :   env.JOB_NAME,
            "jenkinsUrl"    :   env.BUILD_URL,
            "statusSuccess" :   statusSuccess,
            "hasApproval"   :   hasApproval,
            "apporovalUrl"  :   env.BUILD_URL + 'consoleText',
        ]);

        mail to: emailRecipients.join(","),
            from: "jenkins-noreply@example.com",
            subject: "${icon} [ ${env.JOB_NAME} ] [${env.BUILD_NUMBER}] - ${buildStatus} ",
            body: body,
            mimeType: 'text/html'


    } catch (e){
        println "ERROR SENDING EMAIL ${e}"
    }
}
