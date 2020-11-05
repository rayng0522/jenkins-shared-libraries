#!/usr/bin/env groovy
def call() {
  def userInput
  try {
      userInput = input(
          id: 'Proceed1', message: "Approve \"${env.JOB_NAME}\" Build#${env.BUILD_NUMBER} ?", parameters: [
          [$class: 'BooleanParameterDefinition', defaultValue: true, description: '', name: 'Please confirm you agree with this']
          ])
  } catch(err) { // input false
      def user = err.getCauses()[0].getUser()
      userInput = false
      echo "Aborted by: [${user}]"
  }
}
