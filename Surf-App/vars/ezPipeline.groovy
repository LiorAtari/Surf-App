import yorammi.ez.ezEasy

def call(Map config) {
    def ezPipeline = null
    if (config == null) {
        config = [:]
    }
    if (config.ezSleep == null)
    {
        config.ezSleep = 0
    }
    if (config.ezQuietPeriod == null)
    {
        config.ezQuietPeriod = 5
    }
    if (config.ezNumToKeepStr == null)
    {
        config.ezNumToKeepStr = "5"
    }
    if (config.ezMainLabel == null)
    {
        config.ezNumToKeepStr = "5"
    }
    if (config.notifyOnSuccess == null)
    {
        config.notifyOnSuccess = false
    }
    if (config.disablePoweredByMessage == null)
    {
        config.disablePoweredByMessage = false
    }

    pipeline {
        agent any
        options {
            timestamps()
            buildDiscarder(logRotator(numToKeepStr: config.ezNumToKeepStr))
            ansiColor('xterm')
            skipDefaultCheckout()
            disableConcurrentBuilds()
            quietPeriod(config.ezQuietPeriod)
        }
        stages {
            stage ("[EZ]") {
                steps {
                    script {
                        try {
                            checkout scm
                        }
                        catch (error) {
                        }
                        ezPipeline = new yorammi.ez.ezEasy(this)
                        ezPipeline.config=config
                        ezPipeline.activate()
                    }
                }
            }
        }
        post {
            always {
                script {
                    def configuration = ezPipeline.yaml.configuration
                    if(configuration && configuration.notifications) {
                        if(configuration.notifications.disablePoweredByMessage) {
                            config.disablePoweredByMessage = true
                        }
                        if(configuration.notifications.successNotificationsOnMainBranches) {
                            if(configuration.notifications.mainBranches)
                            {
                                for(String item: configuration.notifications.mainBranches) {
                                    if(item == env.BRANCH_NAME) {
                                        config.notifyOnSuccess = true 
                                    }
                                }
                            }
                        }
                        if(configuration.notifications.emailNotifications) {
                            ezNotifications.sendEmailNotification(to:"ez@yorammi.com",notifyOnSuccess:config.notifyOnSuccess,disablePoweredByMessage:config.disablePoweredByMessage)
                        }
                        if(configuration.notifications.slackNotifications) {
                            if(configuration.notifications.slack) {
                                if(configuration.notifications.slack.channel) {
                                    ezLog.info("sending Slack message to channel ${configuration.notifications.slack.channel}")
                                    ezNotifications.sendSlackNotification(channel:configuration.notifications.slack.channel,notifyOnSuccess:config.notifyOnSuccess,disablePoweredByMessage:config.disablePoweredByMessage)
                                }
                                else{
                                    ezLog.info("sending Slack message to default channel")
                                    ezNotifications.sendSlackNotification(notifyOnSuccess:config.notifyOnSuccess,disablePoweredByMessage:config.disablePoweredByMessage)
                                }
                            }
                            else{
                                ezLog.info("sending Slack message to default channel")
                                ezNotifications.sendSlackNotification(notifyOnSuccess:config.notifyOnSuccess,disablePoweredByMessage:config.disablePoweredByMessage)
                            }
                        }
                        else {
                             ezLog.info("not sending slack message")
                        }
                    }
                    sleep (config.ezSleep)
                }
            }
        }
    }
}
