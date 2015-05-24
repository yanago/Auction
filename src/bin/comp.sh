#!/bin/bash

CLASSPATH=.
jars=`find /home/vyordanov/akka-2.2.0 -name *.jar`
for file in $jars
do
CLASSPATH=$CLASSPATH:$file
done
echo $CLASSPATH
javac -classpath $CLASSPATH auction/*.java
