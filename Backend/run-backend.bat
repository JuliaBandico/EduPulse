@echo off
rem EduPulse backend launcher (Windows). Needs internet the first time only.
rem Uses an installed JDK if there is one; otherwise downloads a portable JDK 17 into .jdk\ (nothing is installed).
setlocal
cd /d "%~dp0"
set "PORTABLE=%~dp0.jdk\jdk"

if exist "%PORTABLE%\bin\javac.exe" ( set "JAVA_HOME=%PORTABLE%" & goto run )
if defined JAVA_HOME if exist "%JAVA_HOME%\bin\javac.exe" goto run
where javac >nul 2>nul
if not errorlevel 1 ( set "JAVA_HOME=" & goto run )

echo No JDK found. Downloading a portable JDK 17 into .jdk (about 180 MB, one time)...
if not exist "%~dp0.jdk" mkdir "%~dp0.jdk"
powershell -NoProfile -ExecutionPolicy Bypass -Command "$ProgressPreference='SilentlyContinue'; Invoke-WebRequest -UseBasicParsing 'https://api.adoptium.net/v3/binary/latest/17/ga/windows/x64/jdk/hotspot/normal/eclipse' -OutFile '%~dp0.jdk\jdk.zip'; Expand-Archive -Force '%~dp0.jdk\jdk.zip' '%~dp0.jdk\x'; $d = Get-ChildItem '%~dp0.jdk\x' -Directory | Select-Object -First 1; Move-Item $d.FullName '%~dp0.jdk\jdk'; Remove-Item '%~dp0.jdk\jdk.zip','%~dp0.jdk\x' -Recurse -Force"
if not exist "%PORTABLE%\bin\javac.exe" (
  echo Download failed. Check your internet connection and try again.
  exit /b 1
)
set "JAVA_HOME=%PORTABLE%"

:run
echo Using JAVA_HOME=%JAVA_HOME%
call mvnw.cmd -Dmaven.test.skip=true spring-boot:run
