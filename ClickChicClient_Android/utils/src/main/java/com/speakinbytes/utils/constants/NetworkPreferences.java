package com.speakinbytes.utils.constants;

public class NetworkPreferences {
	// NETWORK
	// Conection
	// Whether there is a Wi-Fi connection.
	public static boolean wifiConnected = false;
	// Whether there is a mobile connection.
	public static boolean mobileConnected = false;
	// Whether the display should be refreshed.
	public static boolean refreshDisplay = true;

	// The user's current network preference setting.
	public static String sPref = null;
	public static final String WIFI = "Wi-Fi";
	public static final String ANY = "Any";

			

	public interface CONNECTION_TYPE {
		public static final String _3G = "3G";
		public static final String _ETHERNET = "ETHERNET";
		public static final String _WIFI="WIFI";
	}
}
