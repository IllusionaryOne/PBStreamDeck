/*
 * Copyright (C) 2018 phantombot.tv
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tv.phantombot.pbstreamdeck;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;

import com.github.sarxos.winreg.HKey;
import com.github.sarxos.winreg.RegistryException;
import com.github.sarxos.winreg.WindowsRegistry;

/**
 * Main class for PBStreamDeck.  To run this without the application being fully
 * installed via InnoSetup run with the parameters of [execute key] [path to config]
 */
public class PBStreamDeck {
	
	/**
	 * Main function for PBStreamDeck.
	 * 
	 * @param args
	 *            Command line arguments.
	 */
	public static void main(String[] args) {
		String configDir = "";
		String elgatoConfigDir = "";
		String fileKey = "";
		PBStreamDeckProperties properties = new PBStreamDeckProperties();
		// ElgatoStreamDeckConfig streamDeck = new ElgatoStreamDeckConfig();

		if (args.length < 2) {
			try {
				WindowsRegistry registry = WindowsRegistry.getInstance();
				configDir = registry.readString(HKey.HKCU, "SOFTWARE\\PhantomBot\\StreamDeck", "ConfigDir");
				// elgatoConfigDir = registry.readString(HKey.HKCU, "SOFTWARE\\PhantomBot\\StreamDeck", "ElgatoConfigDir");
			} catch (RegistryException ex) {
				throwErrorGUI("error accessing registry: " + ex.getMessage());
				return;
			}
		} else {
			configDir = args[1];
		}
		
		/**
		 * Remove support for this for now.  Still a work in progress and need to release
		 * a patch for Let's Encrypt.
		 * 
		try {
			streamDeck.loadProfiles(elgatoConfigDir);
		} catch (IOException ex) {
			throwErrorGUI("error loading Elgato profiles: " + ex.getMessage());
			return;
		}
		**/
		
		if (args.length == 0) {
			throwErrorGUI("usage: PBStreamDeck [execute key]");
			return;
		}

		try {
			if (!properties.loadPropertiesFile(configDir)) {
				return;
			}
		} catch (SecurityException ex) {
			throwErrorGUI("Cannot access " + configDir + "/streamdeck.properties.txt due to security permissions.");
			return;
		} catch (FileNotFoundException ex) {
			throwErrorGUI("Properties file does not exist: " + configDir + "/streamdeck.properties.txt");
			return;
		} catch (IOException ex) {
			throwErrorGUI("Error loading properties file: " + ex.getMessage());
			return;
		}
		
		fileKey = args[0];
		String commandOrText = properties.getCommandOrText(fileKey);
		if (commandOrText == null) {
			throwErrorGUI("That key does not seem to exist in the properties file: " + fileKey);
			return;
		}

		PhantomBotRESTAPI restAPI = new PhantomBotRESTAPI(properties.getURL(), properties.getBotName(),
				properties.getAPIAuthKey(), properties.getSSLCACheck().equals("enable"));
		String restAPIResult = restAPI.callAPI(commandOrText);
		if (!restAPIResult.contains("event posted")) {
			throwErrorGUI(restAPIResult);
		}
	}

	/**
	 * Throws an error window up with an error message.
	 * 
	 * @param errorMessage
	 *            The error message to display in the window.
	 */
	private static void throwErrorGUI(String errorMessage) {
		JLabel label = new JLabel(errorMessage);
		label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 18));
		JOptionPane.showMessageDialog(null, label, "PBStreamDeck Error", JOptionPane.PLAIN_MESSAGE);
	}
}
