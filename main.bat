javac -sourcepath ./src -d out/production src\com\company\Main.java
java -classpath ./out/production; Main
javac -classpath lib/junit-4.12.jar; -sourcepath ./src -d out/production src\test\MainTest.java
java -classpath lib/junit-4.12.jar;lib/hamcrest-core-1.3.jar;./out/production org.junit.runner.JUnitCore test.MainTest
jar cvf main.jar -C out/production/ .
jar cef Main main.jar -C out/production/ .
