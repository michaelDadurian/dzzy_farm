INTELLIJ SETTINGS

Project Settings -> Modules

Paths tab

	Check "Use module compile output path"
	Set Output path to C:\Users\<username>\AppData\Roaming\.tribot\bin
	Same value for Test output path
	Check "Exclude output paths"

Dependencies tab
	java sdk should already be there, add it if its not
	Add .tribot jar found in C:\Users\<username>\AppData\Roaming\.tribot\dependancies

When you rebuild your scripts, this should 
result in your .class files to show up in the correct directory for tribot to pick them up