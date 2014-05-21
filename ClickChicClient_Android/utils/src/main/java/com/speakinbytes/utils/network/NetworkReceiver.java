package com.speakinbytes.utils.network;

import com.speakinbytes.utils.constants.NetworkPreferences;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager conn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conn.getActiveNetworkInfo();

		// Checks the user prefs and the network connection. Based on the
		// result, decides whether
		// to refresh the display or keep the current display.
		// If the userpref is Wi-Fi only, checks to see if the device has a
		// Wi-Fi connection.
		if (NetworkPreferences.WIFI.equals(NetworkPreferences.sPref) && networkInfo != null
				&& networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			// If device has its Wi-Fi connection, sets refreshDisplay
			// to true. This causes the display to be refreshed when the user
			// returns to the app.
			NetworkPreferences.refreshDisplay = true;
			// If the setting is ANY network and there is a network connection
			// (which by process of elimination would be mobile), sets
			// refreshDisplay to true.
		} else if (NetworkPreferences.ANY.equals(NetworkPreferences.sPref) && networkInfo != null) {
			NetworkPreferences.refreshDisplay = true;
			// Otherwise, the app can't download content--either because there
			// is no network
			// connection (mobile or Wi-Fi), or because the pref setting is
			// WIFI, and there
			// is no Wi-Fi connection.
			// Sets refreshDisplay to false.
		} else {
			NetworkPreferences.refreshDisplay = false;
		}
		//stateConection(NetworkPreferences.refreshDisplay);
	}
	
}
