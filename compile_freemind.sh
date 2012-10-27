#!/bin/bash
export JAVA_HOME=/usr/lib/jvm/java-6-sun
export ANT_HOME=/home/ppires/TPs/ManutencaoEvolucaoSoftware/TP/apache-ant-1.8.4
export PATH=${ANT_HOME}/bin:$PATH

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
SRC=$DIR/freemind/freemind

echo "Compilando Freemind..."
cd $SRC && ant

exit 0

