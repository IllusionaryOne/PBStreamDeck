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

import java.util.Map;
import java.util.stream.Stream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Interfaces with the Elgato JSON configuration files to automaticelly
 * update items in the Stream Deck interface.
 * 
 * @author IllusionaryOne
 *
 */

/**
 * Notes:
 * 
 * Files are stored in:
 * AppData/Roaming/Elgato/StreamDeck/ProfilesV2/<hash>.sdProfile
 * 
 * Location is Column, Row
 * 
 * Format: { "Actions": { "2,2": { "Name": "Open", "Settings": {
 * "openInBrowser": true, "path": "D:\\pbs\\PBStreamDeck.exe d:\\pbs skipsong"
 * }, "State": 0, "States": [ { "Image": "state0.png", "Title": "Skipsong",
 * "TitleAlignment": "" } ], "UUID": "com.elgato.streamdeck.system.open" }, },
 * "DeviceUUID": "@(1)[4057/96/]", "Name": "Default Profile", "Version": "1.0" }
 * 
 */
public class ElgatoStreamDeckConfig {
	/* x,y -> True/False */
	private Map<String, Boolean> locationMap = new HashMap<String, Boolean>();
	
	/* UUID -> Description in file */
	private Map<String, String> uuidToNameMap = new HashMap<String, String>();
	
	/* UUID -> JSON */
	private Map<String, JSONObject> jsonMap = new HashMap<String, JSONObject>();

	/**
	 * Constructor.
	 */
	public ElgatoStreamDeckConfig() {
		initializeLocationMap();
	}

	/**
	 * Loads the profiles for the Stream Deck.
	 */
	public void loadProfiles(String elgatoDir) throws IOException {
		Stream<Path> jsonFiles = Files.find(Paths.get(elgatoDir), 10, (path, basicFileAttributes) -> {
			File file = path.toFile();
			return !file.isDirectory() && file.getName().contains("manifest.json");
		});

		jsonFiles.forEach((jsonFilePath) -> {
			try {
				byte[] fileData = Files.readAllBytes(jsonFilePath);
				JSONObject jsonObject = new JSONObject(new String(fileData, "UTF-8"));
		
				String[] filePathSplit = jsonFilePath.getParent().toString().split("\\\\");
				String uuid = filePathSplit[filePathSplit.length - 1];
				String[] uuidSplit = uuid.split("\\.");
				uuid = uuidSplit[0];
				
				String description = jsonObject.getString("Name");
			
				uuidToNameMap.put(uuid, description);
				jsonMap.put(uuid, jsonObject);
			} catch (IOException ex) {
				System.out.println("IOException: " + ex.getMessage());
			}
		});
		
		jsonFiles.close();
	}

	/**
	 * Initializes the locationMap HashMap.
	 */
	private void initializeLocationMap() {
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 3; y++) {
				String locationStr = Integer.toString(x) + "," + Integer.toString(y);
				locationMap.put(locationStr, Boolean.FALSE);
			}
		}
	}
}