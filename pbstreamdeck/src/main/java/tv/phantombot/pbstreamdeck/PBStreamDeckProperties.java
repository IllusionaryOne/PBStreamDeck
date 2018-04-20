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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Handles the properties file for the application.  Responsible for
 * processing the properties file.
 * 
 * @author IllusionaryOne
 */
public class PBStreamDeckProperties {
	private Properties appProperties;
	
	/**
	 * Constructor.  Creates the Properties object.
	 */
	public PBStreamDeckProperties() {
		appProperties = new Properties();
	}
	
	/**
	 * Loads the configuration file, if one is not found, calls the
	 * skeleton creation method.
	 * 
	 * @return false on failure and true on success
	 */
	public boolean loadPropertiesFile(String pathToFile) throws SecurityException, FileNotFoundException, IOException {
		FileInputStream inputStream = new FileInputStream(pathToFile + "/streamdeck.properties.txt");
		appProperties.load(inputStream);
		inputStream.close();
		return true;
	}

	/**
	 * Returns the name of the PhantomBot from properties.
	 * 
	 * @return Name of the PhantomBot.
	 */
	public String getBotName() {
		return appProperties.getProperty("botname");
	}
	
	/**
	 * Returns the host that PhantomBot is running from.
	 * 
	 * @return The URL that the REST API is hosted on.
	 */
	public String getURL() {
		return appProperties.getProperty("boturl");
	}

	/**
	 * Returns the API authorization key for calling the REST API.
	 * 
	 * @return The API authorization key.
	 */
	public String getAPIAuthKey() {
		return appProperties.getProperty("botapiauthkey");
	}
	
	/**
	 * Returns the setting for disabling SSL CA verification.
	 * 
	 * @return Disable/enable SSL CA verification.
	 */
	public String getSSLCACheck() {
		return appProperties.getProperty("sslcacheck") == null ? "enable" : appProperties.getProperty("sslcacheck");
	}
	
	/**
	 * Returns the data to pass to the PhantomBot REST API.
	 * 
	 * @return The command or text to place into chat.
	 */
	public String getCommandOrText(String key) {
		return appProperties.getProperty(key);
	}
}
