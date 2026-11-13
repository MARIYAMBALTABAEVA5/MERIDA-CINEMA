@echo off
cd /d "%~dp0"
if not exist out mkdir out
javac -encoding UTF-8 -d out src\cinema\model\*.java src\cinema\factory\*.java src\cinema\builder\*.java src\cinema\strategy\*.java src\cinema\decorator\*.java src\cinema\observer\*.java src\cinema\facade\*.java src\cinema\ui\*.java
if errorlevel 1 (
  echo Compilation failed. Check that JDK 17 or newer is installed.
  pause
  exit /b 1
)
java -cp "out;resources" cinema.ui.CinemaApp
