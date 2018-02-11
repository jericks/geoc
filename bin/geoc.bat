@echo off
setlocal enableextensions enabledelayedexpansion

if not "%JAVA_HOME%" == "" goto java_home_set

echo.
echo ERROR: JAVA_HOME not found in your environment.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto error

:java_home_set

if exist "%JAVA_HOME%\bin\java.exe" goto java_home_ok

echo.
echo ERROR: JAVA_HOME is set to an invalid directory.
echo JAVA_HOME = "%JAVA_HOME%"
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto error

:java_home_ok
set JAVA="%JAVA_HOME%\bin\java.exe"

@REM change directory to the lib directory
set CWD=%CD%
cd %~dp0

@REM build up the classpath
set CLASSPATH=%CD%\..\lib\geocommands-${project.version}.jar

@REM set native library path
if not "%GEOC_GDAL_HOME%" == "" (
    set "PATH=%GEOC_GDAL_HOME%;%PATH%"
)

cd %CWD%
%JAVA% org.geocommands.App %*
goto end

:error
     set ERROR_CODE=1

:end

