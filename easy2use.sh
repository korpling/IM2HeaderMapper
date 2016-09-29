#!/bin/bash
#
#
CLASS_PATH='-classpath src/jdom-2.0.5/jdom-2.0.5.jar:src/jaxen-1.1.6/jaxen-1.1.6.jar:bin/'
EXEC_CLASS='Main'
PARAMS=$1 #'setInfo'
JAVA_PARAMS=$CLASS_PATH' -Dlogback.configurationFile=./conf/logback.xml '$EXEC_CLASS' '$PARAMS
java $JAVA_PARAMS
