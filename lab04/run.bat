@echo off
rmdir /s /q .\bin
javac -sourcepath src src\com\robotsim\Controlador.java -d bin
java -cp bin com.robotsim.Controlador