@echo off
echo Creating executable for Access Control System...

REM Set variables
set JAR_FILE=app\build\libs\app-uber.jar
set EXE_NAME=AccessControlSystemCLI
set OUTPUT_DIR=app\build\exe

REM Create output directory if it doesn't exist
if not exist %OUTPUT_DIR% mkdir %OUTPUT_DIR%

REM Delete existing output directory if it exists
if exist %OUTPUT_DIR%\%EXE_NAME% rmdir /s /q %OUTPUT_DIR%\%EXE_NAME%

REM Check if jpackage is available (Java 14+)
where jpackage >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo Using jpackage to create the executable...
    jpackage --input app\build\libs --main-jar app-uber.jar --dest %OUTPUT_DIR% --name %EXE_NAME% --type app-image --main-class com.camt.dii.secure.App
    
    if %ERRORLEVEL% EQU 0 (
        echo Executable created successfully at %OUTPUT_DIR%\%EXE_NAME%\%EXE_NAME%.exe
        echo Creating shortcut on desktop...
        
        REM Create a shortcut on the desktop
        powershell "$WshShell = New-Object -ComObject WScript.Shell; $Shortcut = $WshShell.CreateShortcut([Environment]::GetFolderPath('Desktop') + '\Access Control System.lnk'); $Shortcut.TargetPath = '%CD%\%OUTPUT_DIR%\%EXE_NAME%\%EXE_NAME%.exe'; $Shortcut.WorkingDirectory = '%CD%\%OUTPUT_DIR%\%EXE_NAME%'; $Shortcut.Description = 'Access Control System CLI'; $Shortcut.Save()"
    ) else (
        echo Failed to create executable.
    )
) else (
    echo jpackage not found. Creating batch file wrapper...
    
    REM Create a simple wrapper script for the jar
    echo @echo off > %OUTPUT_DIR%\%EXE_NAME%.bat
    echo echo Starting Access Control System... >> %OUTPUT_DIR%\%EXE_NAME%.bat
    echo echo. >> %OUTPUT_DIR%\%EXE_NAME%.bat
    echo echo This window will remain open for command input. Type 'help' for available commands. >> %OUTPUT_DIR%\%EXE_NAME%.bat
    echo echo. >> %OUTPUT_DIR%\%EXE_NAME%.bat
    echo java -jar "%~dp0\..\libs\app-uber.jar" >> %OUTPUT_DIR%\%EXE_NAME%.bat
    echo echo. >> %OUTPUT_DIR%\%EXE_NAME%.bat
    echo echo Access Control System has terminated. >> %OUTPUT_DIR%\%EXE_NAME%.bat
    echo pause >> %OUTPUT_DIR%\%EXE_NAME%.bat
    
    echo Created a batch file wrapper at %OUTPUT_DIR%\%EXE_NAME%.bat
    echo Creating shortcut on desktop...
    
    REM Create a shortcut on the desktop
    powershell "$WshShell = New-Object -ComObject WScript.Shell; $Shortcut = $WshShell.CreateShortcut([Environment]::GetFolderPath('Desktop') + '\Access Control System.lnk'); $Shortcut.TargetPath = '%CD%\%OUTPUT_DIR%\%EXE_NAME%.bat'; $Shortcut.WorkingDirectory = '%CD%\%OUTPUT_DIR%'; $Shortcut.Description = 'Access Control System CLI'; $Shortcut.Save()"
    
    echo To convert to a true EXE, please install one of these tools:
    echo 1. Launch4j: http://launch4j.sourceforge.net/
    echo 2. jpackage: Included with Java 14+ JDK
)

echo Done!
