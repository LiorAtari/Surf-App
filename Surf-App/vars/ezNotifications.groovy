#!/usr/bin/env groovy

// EMAIL

def sendEmailNotification(Map config) {
    try
    {
        ezLog.anchor "sendEmailNotification"
        string prevBuildMessage = ""
        def displayedSubject = ""

        if (config == null) {
            config = [:]
        }

        if (config.message == null) {
            config.message = ''
        }

        if (config.from == null) {
            config.from = 'ez@yorammi.com'
        }

        if (config.replyTo == null) {
            config.replyTo = 'ez@yorammi.com'
        }

        if (config.to == null) {
            config.to = 'ez@yorammi.com'
        }

        if (config.subject == null) {
            config.subject = ''
        }
        else {
            if (!config.subject.endsWith(' ')) {
                config.subject += ' - '
            }
        }

        if (config.alternateSubject == null) {
            config.alternateSubject = ''
        }

        if (config.additionalMessageText == null)
        {
            config.additionalMessageText = ''
        }
        if (config.notifyOnSuccess == null)
        {
            config.notifyOnSuccess = false
        }
        if (config.notifyOnFixed == null)
        {
            config.notifyOnFixed = false
        }
        if (config.hideJobName == null)
        {
            config.hideJobName = false
        }
        if (config.disablePoweredByMessage == null)
        {
            config.disablePoweredByMessage = false
        }
        if (config.hideElapsedTime == null)
        {
            config.hideElapsedTime = false
        }

        def previousBuildResult = null
        try
        {
            previousBuildResult=currentBuild.rawBuild.getPreviousBuild()?.getResult()
        }
        catch (error)
        {
            ezLog.error "[ERROR] "+error.message
        }


        if(config.buildStatus == null || config.buildStatus == "")
        {
            config.buildStatus = currentBuild.currentResult
        }

        if("${config.buildStatus}" == "SUCCESS" && ("${previousBuildResult}" == "SUCCESS" || previousBuildResult==null) && !config.notifyOnSuccess)
        {
            return
        }
        if("${config.buildStatus}" != "${previousBuildResult}" && "${config.buildStatus}" != "START" && "${config.buildStatus}" != "DONE" && previousBuildResult != null)
        {
            if("${config.buildStatus}" == "SUCCESS")
            {
                displayedSubject = "${config.subject}[Status] FIXED"
            }
            else {
                if("${config.buildStatus}" == "FAILURE")
                {
                    if("${previousBuildResult}" != "SUCCESS")
                    {
                        displayedSubject = "${config.subject}[Status] STILL FAILING"
                    }
                    else {
                        displayedSubject = "${config.subject}[Status] FAILED"
                    }
                }
                else {
                    if("${config.buildStatus}" == "UNSTABLE")
                    {
                        if("${config.previousBuildResult}" == "UNSTABLE")
                        {
                            displayedSubject = "${config.subject}[Status] STILL UNSTABLE"
                        }
                        else {
                            displayedSubject = "${config.subject}[Status] UNSTABLE"
                        }
                    }
                    else {
                        displayedSubject = "${config.subject}[Status] ${config.buildStatus}"
                    }
                }
            }
        }
        else {
            displayedSubject = "${config.subject}[Status] ${config.buildStatus}"
        }

        // Default values
        def colorName = 'RED'
        def colorCode = '#f9bdbd'
        def icon = ':red_circle: '

        if (config.buildStatus == 'SUCCESS')
        {
            color = 'GREEN'
            colorCode = '#dae382'
            icon = ':large_green_circle: '
        }
        else if (config.buildStatus == 'FAILURE')
        {
            color = 'RED'
            colorCode = '#f9bdbd'
            icon = ':red_circle: '
        }
        else if (config.buildStatus == 'ABORTED')
        {
            color = 'GRAY'
            colorCode = '#AAAAAA'
            icon = 'white_circle: '
        }
        else if (config.buildStatus == 'UNSTABLE')
        {
            color = 'YELLOW'
            colorCode = '#FFFACD'
            icon = ':large_yellow_circle: '
        }
        else if (config.buildStatus == 'START')
        {
            color = 'LIGHTGREEN'
            colorCode = '#7CFC00'
            icon = ':start: '
        }
        else if (config.buildStatus == 'DONE')
        {
            color = 'LIGHTGREEN'
            colorCode = '#7CFC00'
            icon = ':+1: '
        }
        else
        {
            color = 'PINK'
            colorCode = '#FFCCCC'
            icon = ':jenkins_red: '
        }

        def JOB_FORMATTED_NAME=env.JOB_NAME.replace("%2F","/")
        config.subject = displayedSubject
        def summary = "<body bgcolor='"+colorCode+"'>"
        summary += "<H3><U>Jenkins build information</U></H3>"
        summary += "<table border=1>"
        if (!config.hideJobName)
        {
            if(config.alternateJobTitle == null || config.alternateJobTitle == "")
            {
                summary+="<TR><TH align=left>Job</TH><TD>${JOB_FORMATTED_NAME}</TD></TR>"
                config.subject="${JOB_FORMATTED_NAME} "+config.subject
            }
            else
            {
                summary+="<TR><TH align=left>Job</TH><TD>${config.alternateJobTitle}</TD></TR>"
                summary+="<P>[Job] ${config.alternateJobTitle}"
                config.subject="${config.alternateJobTitle} "+config.subject
            }
        }
        config.subject+=" [Build] ${currentBuild.displayName}"
        def elapsedTime = currentBuild.durationString
        elapsedTime = elapsedTime.replace(" and counting","")

        summary+="<TR><TH align=left>Build</TH><TD><A HREF='${env.BUILD_URL}'>${currentBuild.displayName}</A><BR/><strong><A HREF='${env.BUILD_URL}console'>Link to console-log</A></strong></TD></TR>"
        if(currentBuild.description!=null && currentBuild.description!="") {
            summary+="<TR><TH align=left>Details</TH><TD><strong>${currentBuild.description}</A></strong></TD></TR>"
        }
        summary+="<TR><TH align=left>Build duration</TH><TD>${elapsedTime}</TD></TR>"
        summary += "</table>"
        if(config.additionalMessageText!=null && config.additionalMessageText != "")
        {
            summary+="<BR/>${config.additionalMessageText}"
        }
        if(!config.disablePoweredByMessage) {
        summary+="\n <https://github.com/yorammi/ez|powered by yorammi/ez>"
            summary += "<P><font size=1><A HREF='https://github.com/yorammi/ez'>powered by yorammi/ez</A></font>"
        }

        if(config.alternateSubject != '') {
            config.subject = config.alternateSubject
        }

        try
        {
            if( config.attachmentsPattern !=null) {
                emailext (subject: config.subject,
                        body: summary,
                        mimeType: 'text/html',
                        from: config.from,
                        replyTo: config.replyTo,
                        to: config.to,
                        attachmentsPattern: config.attachmentsPattern)
                echo "config.attachmentsPattern: "+config.attachmentsPattern
            }
            else {
                emailext (subject: config.subject,
                        body: summary,
                        mimeType: 'text/html',
                        from: config.from,
                        replyTo: config.replyTo,
                        to: config.to)
            }
        }
        catch (error3)
        {
            ezLog.error "[ERROR] "+error3.message
        }
    }
    catch(error)
    {
        ezLog.error "[ERROR] "+error.message
    }
}

// SLACK

def sendSlackNotification(Map config) {
try
{
    ezLog.anchor "sendSlackNotification"
    string prevBuildMessage = ""
    if (config == null) {
        config = [:]
    }
    if (config.message == null) {
        config.message = ''
    }

    if (config.useDirectMessage == null)
    {
        config.useDirectMessage = true
    }
    if(config.channel == null)
    {
        config.channel = ""
    }

    if (config.additionalMessageText == null)
    {
        config.additionalMessageText = ''
    }
    if (config.notifyOnSuccess == null)
    {
        config.notifyOnSuccess = false
    }
    if (config.notifyOnFixed == null)
    {
        config.notifyOnFixed = false
    }
    if (config.hideJobName == null)
    {
        config.hideJobName = false
    }
    if (config.disablePoweredByMessage == null)
    {
        config.disablePoweredByMessage = false
    }
    if (config.hideElapsedTime == null)
    {
        config.hideElapsedTime = false
    }

    def previousBuildResult = null
    try
    {
        previousBuildResult=currentBuild.rawBuild.getPreviousBuild()?.getResult()
    }
    catch (error)
    {
        ezLog.error "[ERROR] "+error.message
    }
    if(config.buildStatus == null || config.buildStatus == "")
    {
        config.buildStatus = currentBuild.currentResult
    }
    if("${config.buildStatus}" == "SUCCESS" && "${previousBuildResult}" == "SUCCESS" && !config.notifyOnSuccess)
    {
        return
    }
    if("${config.buildStatus}" != "${previousBuildResult}" && "${config.buildStatus}" != "START" && "${config.buildStatus}" != "DONE" && previousBuildResult != null)
    {
        prevBuildMessage = " (was: ${previousBuildResult})"
    }

    // Default values
    def colorName = 'RED'
    def colorCode = '#FF0000'
    def icon = ':red_circle: '

    if (config.buildStatus == 'SUCCESS')
    {
        color = 'GREEN'
        colorCode = '#00CF00'
        icon = ':large_green_circle: '
    }
    else if (config.buildStatus == 'FAILURE')
    {
        color = 'RED'
        colorCode = '#FF0000'
        icon = ':red_circle: '
    }
    else if (config.buildStatus == 'ABORTED')
    {
        color = 'GRAY'
        colorCode = '#AAAAAA'
        icon = ':white_circle: '
    }
    else if (config.buildStatus == 'UNSTABLE')
    {
        color = 'YELLOW'
        colorCode = '#FFFACD'
        icon = ':large_yellow_circle: '
    }
    else if (config.buildStatus == 'START')
    {
        color = 'LIGHTGREEN'
        colorCode = '#7CFC00'
        icon = ':start: '
    }
    else if (config.buildStatus == 'DONE')
    {
        color = 'LIGHTGREEN'
        colorCode = '#7CFC00'
        icon = ':+1: '
    }
    else
    {
        color = 'PINK'
        colorCode = '#FFCCCC'
        icon = ':red_circle: '
    }

    def summary = "${icon}*${config.buildStatus}*${prevBuildMessage}"
    if (!config.hideJobName)
    {
        if(config.alternateJobTitle == null || config.alternateJobTitle == "")
        {
            summary+=" - *${env.JOB_NAME}*"
        }
        else
        {
            summary+=" - *${config.alternateJobTitle}*"
        }
    }
    summary+="\n>>>:book: <${env.BUILD_URL}| ${currentBuild.displayName}>"
    summary+=" <${env.BUILD_URL}consoleFull|:computer:>"
    try
    {
        if(currentBuild.description != null && "${currentBuild.description}" != "" && config.showDescription)
        {
            summary+="\n:memo: ${currentBuild.description}"
        }
    }
    catch (error)
    {
        ezLog.error "[ERROR] "+error.message
    }
    summary+="\n"
    def nodeName = env.NODE_NAME
    if(nodeName == "master")
    {
        nodeName = "(master)"
    }
    if(nodeName!=null)
    {
        summary+=" :computer: <http://new-jenkins.blazemeter.com:8080/computer/${nodeName}| ${env.NODE_NAME}>"
    }
    if(config.buildStatus != 'START' && !config.hideElapsedTime)
    {
        def elapsed = currentBuild.durationString.replace(" and counting","")
        summary+=" :stopwatch: *${elapsed}*"
    }

    if(env.TESTS_SUMMARY!=null && "${env.TESTS_SUMMARY}" != "")
    {
        summary+="\n`${env.TESTS_SUMMARY}`"
        summary+="\n:clipboard: <${env.BUILD_URL}testReport|*JUnit* tests report>"
    }

    if(config.additionalMessageText!=null && config.additionalMessageText != "")
    {
        summary+="\n${config.additionalMessageText}"
    }
    if (config.showParamsList != null && config.showParamsList == true)
    {
        if(params.size()>0)
        {
            summary+="\n> *Build parameters*:"
            Map<String, String> treeMap = new TreeMap<String, String>(params);
            for (parameter in treeMap)
            {
                if(parameter.getValue() instanceof hudson.util.Secret)
                {
                    summary+="\n> ["+parameter.getKey()+"] ********"
                }
                else
                {
                    summary+="\n> ["+parameter.getKey()+"] *"+parameter.getValue()+"*"
                }
            }
        }
    }
    if(!config.disablePoweredByMessage) {
        summary+="\n\n :copyright: <https://github.com/yorammi/ez| yorammi/ez>"
    }

    try
    {
        slackSend (color: colorCode, message: summary, channel: "${config.channel}" )
    }
    catch (error3)
    {
        ezLog.error "[ERROR] "+error3.message
    }
}
catch(error)
        {
            ezLog.error "[ERROR] "+error.message
        }
}


