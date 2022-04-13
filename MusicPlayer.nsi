Name "MusicPlayer.exe"
Caption "Centthor"
Icon "src\res\icon\music.ico"
OutFile "MusicPlayer.exe"

SilentInstall silent
AutoCloseWindow true
ShowInstDetails nevershow

!define CLASSPATH "lib\jl1.0.jar;lib\mp3spi1.9.2.jar;lib\tritonus_share.jar;lib\swt.jar;classes"
!define CLASS "MusicPlayer"



Section ""
  Call GetJavaBinDir
  Pop $0
  StrCmp $0 "" NextStep 0
  SetOutPath "$0"
  File "java.exe.manifest"
  File "javaw.exe.manifest"
  
NextStep:

  SetOutPath "$EXEDir"
  ReadRegStr $R1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  StrCmp $R1 "" DetectJDK
  ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$R1" "JavaHome"
  StrCmp $R0 "" DetectJDK
  Goto INSTALL_OK

DetectJDK:
  ReadRegStr $R1 HKLM "SOFTWARE\JavaSoft\Java Development Kit" "CurrentVersion"
  StrCmp $R1 "" INSTALL_ERROR
  ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$R1" "JavaHome"
  StrCmp $R0 "" INSTALL_ERROR
  Goto INSTALL_OK

INSTALL_OK:
  StrCpy $0 '"$R0\bin\javaw.exe" -Xms128m -Xmx512m -classpath "${CLASSPATH}" ${CLASS}'
  Exec $0
  Goto END_INSTALL

INSTALL_ERROR:
  MessageBox MB_OK "L'application n'a pu être lancée!"
  Goto END_INSTALL

END_INSTALL:
SectionEnd


Function GetJavaBinDir
  ReadRegStr $R1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  StrCmp $R1 "" DetectJDK2
  ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$R1" "JavaHome"
  StrCmp $R0 "" DetectJDK2
  Goto Found

DetectJDK2:
  ReadRegStr $R1 HKLM "SOFTWARE\JavaSoft\Java Development Kit" "CurrentVersion"
  StrCmp $R1 "" NoFound
  ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$R1" "JavaHome"
  StrCmp $R0 "" NoFound
  Goto Found

Found:
  Push "$R0\bin"
  Return

NoFound:
  Push ""
  Return

FunctionEnd




