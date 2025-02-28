@echo off
echo === Testing Access Control System Encryption ===
echo.

echo Step 1: Building the latest Uber JAR...
call gradlew :app:uberJar
if %ERRORLEVEL% NEQ 0 (
    echo Failed to build the JAR file.
    exit /b %ERRORLEVEL%
)
echo JAR built successfully.
echo.

echo Step 2: Building the container image...
podman build -t access-control-system:latest -f Containerfile .
if %ERRORLEVEL% NEQ 0 (
    echo Failed to build the container image.
    exit /b %ERRORLEVEL%
)
echo Container image built successfully.
echo.

echo Step 3: Testing encryption functionality...
echo.

REM Create a temporary expect-style input file
echo test-encryption ADM-001 > container_input.txt
echo exit >> container_input.txt

REM Run the container with the input redirected
echo Running encryption test for card ADM-001...
podman run --rm -i access-control-system:latest < container_input.txt

REM Clean up
del container_input.txt

echo.
echo Encryption test completed.
