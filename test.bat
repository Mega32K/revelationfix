@echo off
echo 把你的jar拖到这里
set /p var=
"E:\CollapsingWorld\System-i\ideJdk17\bin\java.exe" -jar MinecraftDecompiler.jar -i %var% -m 1.20.1.srg -e forge-1.20.1-47.3.0.jar -c *all*