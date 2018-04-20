To setup the application, open the following in the Start Menu and follow the
notes within the file:

	Start > PBStreamDeck > Edit Properties File.

In StreamDeck, add a new System > Open button.  In App / File use the three-dots
button to point to the executable in the folder you installed it into during
the setup process.  By default this will be:

	C:\Program Files (x86)\PBStreamDeck\PBStreamDeck.exe

In StreamDeck put a parameter to the executable that indicates the command/chat
key that you want to execute.  Using the runmods example:

	C:\Program Files (x86)\PBStreamDeck\PBStreamDeck.exe runmods
	
The file pblogo.png is included to use in Stream Deck as a replacement image
for the default one provided for the button.  This is located in the
folder with the executable.

If you run this program and receive the following error:

	unable to find valid certification path to requested target
	
Then Java does not trust the SSL certificate that you use with your PhantomBot
instance.  You will either need to manually add that certificate to the Java
cacerts store (please use Google to determine how to do this) or you may
set the following when you are editing your properties file:

	sslcacheck=disable

For more information, please visit:

	https://community.phantombot.tv/t/phantombot-streamdeck-interface/3999