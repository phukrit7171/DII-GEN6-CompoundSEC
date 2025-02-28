@echo off
echo === Building and Running Containerized Access Control System ===
echo.
echo === DOCUMENTATION ===
echo This script builds and runs the Access Control System in a Podman container.
echo Podman is used to provide consistent execution across different environments.
echo.
echo - Authentication is secured with time-based encryption
echo - Facade IDs provide additional security layers
echo - The app runs completely inside a container for isolation
echo.
echo === INSTRUCTIONS ===
echo.

echo Step 1: Building the Uber JAR...
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

echo Step 3: Running the containerized application...
echo.
echo You can exit the application by pressing Ctrl+C
echo.

podman run --rm -it access-control-system:latest

echo.
echo Container execution completed.
