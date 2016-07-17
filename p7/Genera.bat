@echo off
cd src
java -cp ..\tools\JFlex.jar JFlex.Main -d lexico ..\lexico.flex
cd ..\tools
yacc -J -v -Jpackage=sintactico -Jsemantic=Object ..\sinj.y
move Parser.java ..\src\sintactico