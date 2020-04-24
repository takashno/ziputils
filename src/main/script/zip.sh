#!/bin/sh
SCRIPT_DIR=$(cd $(dirname $0); pwd)
$JAVA_HOME/bin/java -jar $SCRIPT_DIR/../libs/ziputils.jar -m zip "$@"