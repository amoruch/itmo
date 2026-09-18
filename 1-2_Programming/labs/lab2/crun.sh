#!/bin/sh
javac -cp ./Pokemon.jar:. -d . *.java
java -cp ./Pokemon.jar:. lab.Main

