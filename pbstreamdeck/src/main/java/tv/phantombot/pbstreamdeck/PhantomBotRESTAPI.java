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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Responsible for interacting with the PhantomBot REST API.
 * 
 * @author IllusionaryOne
 */
public class PhantomBotRESTAPI {
	private String botURL;
	private String botName;
	private String botAPIAuthKey;
	private Boolean sslCACheck = Boolean.TRUE;

	/**
	 * Constructor for PhantomBotRESTAPI
	 * 
	 * @param botURL
	 *            The URL that PhantomBot is hosting the REST API service on.
	 * @param botName
	 *            The name of the PhantomBot instance, will be used as the actor.
	 * @param botAPIAuthKey
	 *            The webauth key from botlogin.txt.
	 */
	public PhantomBotRESTAPI(String botURL, String botName, String botAPIAuthKey, Boolean sslCACheck) {
		this.botURL = botURL;
		this.botName = botName;
		this.botAPIAuthKey = botAPIAuthKey;
		this.sslCACheck = sslCACheck;
	}

	/**
	 * Calls the PhantomBot REST API
	 * 
	 * @param message
	 *            The message/command to send to the REST API.
	 * @return A String object that contains the value back from REST or another
	 *         error message.
	 */
	public String callAPI(String message) {
		URL url;
		InputStream inputStream = null;
		HttpsURLConnection httpsUrlConn;
		HttpURLConnection httpUrlConn;

		/**
		 * Disable the CA verification on SSL certificates.  Some certificates, such as Let's 
		 * Encrypt, can have problems with Java.  While this is not recommended for accessing
		 * sites in general, the assumption is that the operator of this program trusts the
		 * certificate and server that PhantomBot is running on.
		 */
		if (sslCACheck == Boolean.FALSE) {
			TrustManager[] trustManager = new TrustManager[] {
				new X509TrustManager() {
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return new java.security.cert.X509Certificate[] {};
					}
					@Override
					public void checkClientTrusted(X509Certificate[] chain,
							String authType) throws CertificateException {
					}
					@Override
					public void checkServerTrusted(X509Certificate[] chain,
							String authType) throws CertificateException {
					}
				}
			};
			
			try {
				SSLContext sc = SSLContext.getInstance("TLS");
				sc.init(null, trustManager, new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			} catch (Exception ex) {
				return "exception trying to override SSL CA check: " + ex.getMessage();
			}
			
	        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
	            public boolean verify(String hostname, SSLSession session) {
	                return true;
	            }
	        };
	        
	        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
		}

		try {
			url = new URL(botURL);
			if (botURL.startsWith("https://")) {
				httpsUrlConn = (HttpsURLConnection) url.openConnection();
				httpsUrlConn.setDoInput(true);
				httpsUrlConn.setDoOutput(true);
				httpsUrlConn.setRequestMethod("PUT");

				httpsUrlConn.addRequestProperty("webauth", botAPIAuthKey);
				httpsUrlConn.addRequestProperty("user", botName);
				httpsUrlConn.addRequestProperty("message", message);
				httpsUrlConn.connect();

				if (httpsUrlConn.getResponseCode() == 200) {
					inputStream = httpsUrlConn.getInputStream();
				} else {
					inputStream = httpsUrlConn.getErrorStream();
				}

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream, Charset.forName("UTF-8")));
				return "API Returned: " + readAll(reader);
			} else {
				httpUrlConn = (HttpURLConnection) url.openConnection();
				httpUrlConn.setDoInput(true);
				httpUrlConn.setDoOutput(true);
				httpUrlConn.setRequestMethod("PUT");

				httpUrlConn.addRequestProperty("webauth", botAPIAuthKey);
				httpUrlConn.addRequestProperty("user", botName);
				httpUrlConn.addRequestProperty("message", message);
				httpUrlConn.connect();

				if (httpUrlConn.getResponseCode() == 200) {
					inputStream = httpUrlConn.getInputStream();
				} else {
					inputStream = httpUrlConn.getErrorStream();
				}

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream, Charset.forName("UTF-8")));
				return "API Returned: " + readAll(reader);
			}
		} catch (UnsupportedEncodingException ex) {
			return "Failed to decode data from API.";
		} catch (NullPointerException ex) {
			return "Null pointer encounted.";
		} catch (MalformedURLException ex) {
			return "Bad URL was provided.";
		} catch (SocketTimeoutException ex) {
			return "Timed out trying to access API.";
		} catch (IOException ex) {
			return "An IO exception has occurred: " + ex.getMessage();
		} catch (Exception ex) {
			return "A general exception has occurred: " + ex.getMessage();
		}
	}

	/**
	 * Reads data from a stream.
	 * 
	 * @return A String representing the data read from the stream.
	 */
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

}
