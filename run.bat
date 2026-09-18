@echo off
echo.
echo  ============================
echo   MythBreak - Quick Run
echo  ============================
echo.

REM Check if JAR exists
if not exist "%~dp0target\mythbreak-1.0.0.jar" (
    echo  ERROR: JAR not found! Run setup-and-run.ps1 first.
    pause
    exit /b 1
)

echo  Starting MythBreak on http://localhost:8080
echo  Press Ctrl+C to stop.
echo.
java -jar "%~dp0target\mythbreak-1.0.0.jar"
