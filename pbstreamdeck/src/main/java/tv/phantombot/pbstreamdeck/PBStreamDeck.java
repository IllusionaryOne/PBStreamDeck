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

/**
 * Main class for PBStreamDeck.
 */
public class PBStreamDeck {
	/**
	 * Main function for PBStreamDeck.
	 * 
	 * @param args
	 *            Command line arguments.
	 */
	public static void main(String[] args) {
		PBStreamDeckProperties properties = new PBStreamDeckProperties();

		if (args.length != 2) {
			throwErrorGUI("usage: PBStreamDeck [path to config file] [execute key]");
			return;
		}

		try {
			if (!properties.loadPropertiesFile(args[0])) {
				return;
			}
		} catch (SecurityException ex) {
			throwErrorGUI("Cannot access streamdeck.properties due to security permissions.");
			return;
		} catch (FileNotFoundException ex) {
			throwErrorGUI("Properties file does not exist: " + args[0] + "/streamdeck.properties");
			return;
		} catch (IOException ex) {
			throwErrorGUI("Error loading properties file: " + ex.getMessage());
			return;
		}

		String commandOrText = properties.getCommandOrText(args[1]);
		if (commandOrText == null) {
			throwErrorGUI("That key does not seem to exist in the properties file: " + args[1]);
			return;
		}

		PhantomBotRESTAPI restAPI = new PhantomBotRESTAPI(properties.getURL(), properties.getBotName(),
				properties.getAPIAuthKey());
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
