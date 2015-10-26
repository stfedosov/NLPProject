#!/bin/bash

JAVA_VER=$(java -version 2>&1 | sed 's/java version "\(.*\)\.\(.*\)\..*"/\1\2/; 1q')

CLASSPATH="${PWD}/../lib/*:${PWD}/lib/*:${PWD}/target/classes"

if [ "$JAVA_VER" -ge 18 ]; then
        echo "Ok, your Java is 1.8 or newer. Application started..."
        #java -jar out/artifacts/NLPProject_jar/NLPProject.jar "$@"
        java -cp "$CLASSPATH" Main "$@"
    else
        echo "Your Java Version should be at least 1.8! Application can't be executed!"
fi