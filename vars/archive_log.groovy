#!/usr/bin/env groovy
def call(String name = 'jenkins') {
  script {
    def VAULT_ADDR = 'https://vault.pru.intranet.asia'
    def VAULT_APPROLE = 'apr-rtsre-all-admin-jenkinshcf'
    def configuration = [
        $class: 'VaultConfiguration',
        vaultUrl: "${VAULT_ADDR}",
        vaultCredentialId: "${VAULT_APPROLE}"
    ]
    def secrets = [
        [path: 'kv2/sgrtss/nprd/dev/t3stan/stasgrtssdevaz1t3stan001', engineVersion: 2, secretValues: [
            [envVar: 'AZURE_ACCOUNT_ID', vaultKey: 'storage_account_name'],
            [envVar: 'AZURE_STORAGE_KEY', vaultKey: 'primary_key']]]
    ]
    // inside this block your credentials will be available as env variables
    withVault([configuration: configuration, vaultSecrets: secrets]) {
      def storage_account_name = env.AZURE_ACCOUNT_ID.split('/')
      env.AZURE_STORAGE_ACCOUNT = storage_account_name.last()
      def log = currentBuild.rawBuild.getLog()
      writeFile(file: 'buildlog.txt', text: log)
      env.CONTAINER = name
      sh (script: 'az storage container create -n ${CONTAINER}', returnStdout: true)
      sh (script: 'az storage blob upload -c ${CONTAINER} -f ./buildlog.txt -n ${JOB_NAME}/${BUILD_NUMBER}.txt', returnStdout: true)
    }
  }
}
