package com.speakinbytes.utils.network;

import com.speakinbytes.utils.constants.NetworkPreferences;
import com.speakinbytes.utils.constants.NetworkPreferences.CONNECTION_TYPE;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2) public class Networks {

	/**
	 * M�todo que valida si el dispositivo tiene conexi�n
	 * 
	 * @param contexto Context de la aplicaci�n
	 * @return true si tiene conexi�n
	 *         false si no tiene conexi�n
	 */
	public static boolean isOnline(Context contexto) {
	    ConnectivityManager connMgr = (ConnectivityManager) 
	    		(ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    return (networkInfo != null && networkInfo.isConnected());
	}  
	
	/**
	 * M�todo que valida el tipo de conexi�n del dispositivo
	 * 
	 * @param contexto Context de la aplicaci�n
	 * 
	 * @return WIFI si la conexi�n es a trav�s de redes Wi-Fi
	 *         3G si la conexi�n es a trav�s de redes m�viles
	 *         ETHERNET si la conexi�n es a trav�s de conexi�n directa Ethernet
	 *         null si no hay ning�n tipo de conexi�n disponible
	 */
	public static String checkTypeConnectionType(Context contexto) {
		ConnectivityManager connMgr = (ConnectivityManager) contexto
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		final NetworkInfo wifi = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		final NetworkInfo mobile = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		
		final NetworkInfo ethernet = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);

		if (wifi != null && wifi.isAvailable()) {
			return CONNECTION_TYPE._WIFI;
		} else if (mobile != null && mobile.isAvailable()) {
			return CONNECTION_TYPE._3G;
		} else if(ethernet !=null && ethernet.isAvailable())
		{
			return CONNECTION_TYPE._ETHERNET;
		}else
		{
			return null;
		}

	}
	
	
	 // Checks the network connection and sets the wifiConnected and mobileConnected
    // variables accordingly.
    public static void updateConnectedFlags(Context ctx) {
        ConnectivityManager connMgr =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            NetworkPreferences.wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            NetworkPreferences.mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
        	 NetworkPreferences.wifiConnected = false;
        	 NetworkPreferences.mobileConnected = false;
        }
    }

}
