#!/usr/bin/env groovy

def debug(String text) {
    consolelog("DEBUG",text)
    addToDescription(text)
}

def exception(String text) {
    consolelog("EXCEPTION",text)
    addToDescription(text,'RED')
    throw new Exception("[ERROR] ${text}")
}

def error(String text) {
    consolelog("ERROR",text)
    addToDescription(text, 'PURPLE')
    currentBuild.result = "FAILURE"
}

def info(String text) {
    consolelog("INFO",text)
}

def anchor(String text) {
    echo "\u001B[32m{"+text+"}\u001B[0m"
}

def consolelog(String type, String text) {
    def colorCode="32"
    def colorName="green"
    if(type=='DEBUG') {
        colorCode="34"
        colorName="blue"
    }
    else {
        if(type=='ERROR') {
            colorCode="31"
            colorName="red"
        }
        else {
            if(type=='EXCEPTION') {
                colorCode="31"
                colorName="red"
            }
            else {
                if(type=='INFO') {
                    colorCode="94"
                    colorName="blue"
                }
            }
        }
    }
    echo "\u001B[90m["+type+"]\u001B["+colorCode+"m\u001B[1m ${text}\u001B[0m"
}

def addToDescription(String text, String color='BLUE') {
    if(currentBuild.description==null) {
        currentBuild.description = ""
    }
    else {
        currentBuild.description += "<BR/>"
    }
    currentBuild.description += "<FONT color=${color}>${text}</FONT>"
}


