#!/bin/bash
export JAVA_HOME=/usr/lib/jvm/java-6-sun
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
export ANT_HOME=$DIR/tools/apache-ant-1.8.4
echo "Compilando Freemind..."
cd $DIR/freemind/freemind && ${ANT_HOME}/bin/ant
exit 0

