mkdir -p ./out
echo "Compiling program..."
javac -classpath "./lib/image4j-0.7.jar:./lib/jdeli-trial.jar:./lib/lombok-1.18.20.jar" src/main/java/engine/*.java src/main/java/Main.java src/main/java/interfaces/*.java src/main/java/models/*.java src/main/java/utils/*.java -d ./out
echo "Done!"
