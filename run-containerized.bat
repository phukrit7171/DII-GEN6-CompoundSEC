@echo off
echo === Building and Running Containerized Access Control System ===
echo.
echo === DOCUMENTATION ===
echo This script builds and runs the Access Control System in a container,
echo supporting both Docker and Podman.
echo Podman or Docker is used to provide consistent execution across different environments.
echo.
echo - Authentication is secured with time-based encryption
echo - Facade IDs provide additional security layers
echo - The app runs completely inside a container for isolation
echo.
echo === INSTRUCTIONS ===
echo.

echo Step 1: Building the Uber JAR...
call gradlew :app:shadowJar
if %ERRORLEVEL% NEQ 0 (
    echo Failed to build the JAR file.
    exit /b %ERRORLEVEL%
)
echo JAR built successfully.
echo.

echo Step 2: Checking for Podman...
where podman ^>nul 2^>^&1
if %ERRORLEVEL% EQU 0 (
    echo Podman detected. Using Podman for container operations.
    set CONTAINER_COMMAND=podman
) else (
    echo Podman not found. Using Docker for container operations.
    set CONTAINER_COMMAND=docker
)
echo.

echo Step 3: Building the container image using %CONTAINER_COMMAND%...
%CONTAINER_COMMAND% build -t access-control-system:latest -f Containerfile .
if %ERRORLEVEL% NEQ 0 (
    echo Failed to build the container image using %CONTAINER_COMMAND%.
    exit /b %ERRORLEVEL%
)
echo Container image built successfully using %CONTAINER_COMMAND%.
echo.

echo Step 4: Running the containerized application using %CONTAINER_COMMAND%...
echo.
echo You can exit the application by pressing Ctrl+C
echo.

%CONTAINER_COMMAND% run --rm -it access-control-system:latest

echo.
echo Container execution completed.
