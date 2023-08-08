#!/usr/bin/env groovy

def wgetAndUnzip(String url) {

    sh "wget -O wgetAndUnzip.zip '${url.trim()}'"
    sh "unzip wgetAndUnzip.zip"
    sh "rm -f wgetAndUnzip.zip"
}

def isBuildStartedByUser() {
    try{
        def isStartedByUser = currentBuild.rawBuild.getCause(hudson.model.Cause$UserIdCause) != null
        return isStartedByUser
    }
    catch (Exception error)
    {
        return false
    }
}

def deleteWorkspace() {
    step([$class: 'WsCleanup'])
}

def saveConsoleLogToFile(Map config=null)
{
    try {
        if (config == null) {
            config = [:]
        }
        if (config.path == null) {
            config.path = 'Console-log.info.txt'
        }

        archiveArtifacts artifacts: '**/*.info.txt'
    }
    catch (error) {

    }
}

def validateServerIsUp(String ipAddress) {
    def pingExitCode = sh(script: 'ping -c 1 '+ipAddress,returnStatus:true)
    if(pingExitCode != 0) {
        echo "[ERROR] fail to ping ${ipAddress}"
        sh "exit -1"
    }
}

def parseYamlFile(String fileName) {
    try {
        if(fileExists(fileName)) {
            def yaml = readYaml file: fileName
        }
        return true
    }
    catch (error) {
        ezLog.error '[ERROR] parsing \''+fileName+'\':\n'+error.message
    }
}

def getUniqueBuildIdentifier(def type='buildNumber') {
    def id = ''
    try {
        switch (type) {
            case 'buildNumber':
                id = env.BUILD_NUMBER
                break
            case 'issueNumber':
                def branch_name = env.BRANCH_NAME
                id = getIssueNumberFromBranchName(branch_name)
                break
            default:
                id = "000"
        }
    }
    catch (error) {
        id = "00000"
        ezLog.error '[ERROR] get-unique-build-identifier function failed for '+type+':\n'+error.message
    }
    return id
}

def getIssueNumberFromBranchName(String branchName) {
    // Define a regular expression pattern to match the issue number
    def pattern = /(\d+)/

    // Match the pattern against the branch name
    def matcher = branchName =~ pattern

    // If a match is found, extract and return the issue number
    if (matcher.find()) {
        return matcher.group(1)
    }

    // Return null if no issue number is found
    return null
}

