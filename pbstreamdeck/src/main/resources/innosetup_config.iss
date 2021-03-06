; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

#define MyAppName "PBStreamDeck"
#define MyAppVersion "1.0.5.0"
#define MyAppPublisher "PhantomBot"
#define MyAppURL "https://phantombot.tv"
#define MyAppExeName "PBStreamDeck.exe"
#define LocalConfigDir "{localappdata}\PhantomBot\StreamDeck"
#define ElgatoConfigDir "{userappdata}\Elgato\StreamDeck"

[Setup]
; NOTE: The value of AppId uniquely identifies this application.
; Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{3237B857-1691-4453-9B84-76914AB9E434}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
;AppVerName={#MyAppName} {#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DefaultDirName={pf}\{#MyAppName}
DefaultGroupName={#MyAppName}
AllowNoIcons=yes
InfoAfterFile=C:\Users\illus\git\PBStreamDeck\pbstreamdeck\target\README.txt
OutputBaseFilename=PBStreamDeckSetup
SetupIconFile=C:\Users\illus\git\PBStreamDeck\pbstreamdeck\src\main\resources\pblogo.ico
Compression=lzma
SolidCompression=yes
DisableDirPage=no

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Files]
Source: "C:\Users\illus\git\PBStreamDeck\pbstreamdeck\target\PBStreamDeck.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "C:\Users\illus\git\PBStreamDeck\pbstreamdeck\target\README.txt"; DestDir: "{app}"; Flags: ignoreversion
Source: "C:\Users\illus\git\PBStreamDeck\pbstreamdeck\target\streamdeck.properties.txt"; DestDir: "{#LocalConfigDir}"; Flags: onlyifdoesntexist
Source: "C:\Users\illus\git\PBStreamDeck\pbstreamdeck\target\pblogo.png"; DestDir: "{app}"; Flags: ignoreversion
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{group}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{group}\View README File"; Filename: "{app}\README.txt"
Name: "{group}\Edit Properties File"; Filename: "{#LocalConfigDir}\streamdeck.properties.txt"
Name: "{group}\{cm:UninstallProgram,{#MyAppName}}"; Filename: "{uninstallexe}"

[Registry]
Root: HKCU; Subkey: "Software\PhantomBot\StreamDeck"; Flags: uninsdeletekey
Root: HKCU; Subkey: "Software\PhantomBot\StreamDeck"; ValueType: string; ValueName: "ConfigDir"; ValueData: "{#LocalConfigDir}"
Root: HKCU; Subkey: "Software\PhantomBot\StreamDeck"; ValueType: string; ValueName: "ElgatoConfigDir"; ValueData: "{#ElgatoConfigDir}"