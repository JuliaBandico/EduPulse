@echo off
rem EduPulse backend launcher (Windows). Uses system Java (JAVA_HOME or PATH).
rem Runs the Spring Boot application with Maven wrapper.
setlocal
cd /d "%~dp0"

rem Check for Java in priority order: JAVA_HOME > PATH > error
if defined JAVA_HOME (
    if exist "%JAVA_HOME%\bin\java.exe" (
        echo Using JAVA_HOME=%JAVA_HOME%
        goto run
    )
)

where java >nul 2>nul
if not errorlevel 1 (
    echo Using java from PATH
    set "JAVA_HOME="
    goto run
)

echo Error: No Java found.
echo Please install Java 17+ and ensure it's in PATH or set JAVA_HOME.
pause
exit /b 1

:run
echo Starting EduPulse backend...
echo Working directory: %~dp0
call "%~dp0mvnw.cmd" -Dmaven.test.skip=true spring-boot:run