@echo off
echo Creating executable for Access Control System...

REM Set variables
set JAR_FILE=app\build\libs\app-uber.jar
set EXE_NAME=AccessControlSystem
set OUTPUT_DIR=app\build\exe

REM Create output directory if it doesn't exist
if not exist %OUTPUT_DIR% mkdir %OUTPUT_DIR%

REM Check if jpackage is available (Java 14+)
where jpackage >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo Using jpackage to create the executable...
    jpackage --input app\build\libs --main-jar app-uber.jar --dest %OUTPUT_DIR% --name %EXE_NAME% --type app-image --main-class com.camt.dii.secure.App
) else (
    echo jpackage not found. Using jar2exe method...
    
    REM Create a simple wrapper script for the jar
    echo @echo off > %OUTPUT_DIR%\%EXE_NAME%.bat
    echo java -jar "%~dp0\%JAR_FILE%" >> %OUTPUT_DIR%\%EXE_NAME%.bat
    
    echo Created a batch file wrapper instead at %OUTPUT_DIR%\%EXE_NAME%.bat
    echo To convert to a true EXE, please install one of these tools:
    echo 1. Launch4j: http://launch4j.sourceforge.net/
    echo 2. jpackage: Included with Java 14+ JDK
)

echo Done!
