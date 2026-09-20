javac -cp ".:libs/gson-2.13.2.jar" -d build Main.java
java -cp "build:libs/gson-2.13.2.jar" Main dumpData.json
