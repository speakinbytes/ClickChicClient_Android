package com.speakinbytes.utils.constants;

import com.speakinbytes.utils.R;
import java.util.UUID;
import android.content.Context;
import android.content.SharedPreferences;

public class CustomPreferences {
	
	private static final String PREF_NAME = "SB_PREF";
	/**
	 * M�todo que guarda las fechas de publicaci�n del primer y �ltimo elemento
	 * de la lista. Dichas fechas ser�n consultadas para hacer las b�squedas en
	 * el API
	 * 
	 * @param context
	 * @param lastDate
	 *            fecha del primer elemento de la lista
	 * @param firstDate
	 *            fecha del �ltimo elemento de la lista
	 */
	public static void setDates(Context context, String lastDate,
			String firstDate) {
		SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		// --------modify the value
		pref.edit().putString("DATE_FIRST", null).commit();
		pref.edit().putString("DATE_LAST", null).commit();

	}

	/**
	 * Obtiene la fecha del primer elemento de la lista
	 * 
	 * @param context
	 * @return
	 */
	public static void setLastDate(Context context, String lastDate) {
		SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		pref.edit().putString("DATE_LAST", lastDate).commit();
	}

	/**
	 * Obtiene la fecha del primer elemento de la lista
	 * 
	 * @param context
	 * @return
	 */
	public static void setFirstDate(Context context, String firstDate) {
		SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		pref.edit().putString("DATE_FIRST", firstDate).commit();
	}

	/**
	 * Obtiene la fecha del primer elemento de la lista
	 * 
	 * @param context
	 * @return
	 */
	public static String getDateLast(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		// -------get a value from them
		return pref.getString("DATE_LAST", null);
	}

	/**
	 * Obtiene la fecha del �ltimo elemento de la lista
	 * 
	 * @param context
	 * @return
	 */
	public static String getDateFirst(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		// -------get a value from them
		return pref.getString("DATE_FIRST", null);
	}

	public static long getCustomPreferences(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		// -------get a value from them
		return pref.getLong("TIME_REFRESH", 120000);
	}
	
	public static void setCustomPreferences(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
		// --------modify the value
		pref.edit().putLong("TIME_REFRESH",	Long.parseLong(context.getString(R.string.time))).commit();

	}

	public static void setPreferencesAnalytics(Context context, long datos[]) {
		SharedPreferences pref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
		// --------modify the value
		pref.edit().putLong("START_SESSION", datos[0]).commit();
		pref.edit().putLong("START_USER", datos[1]).commit();
		pref.edit().putLong("NUMBER_SESSIONS", datos[2]).commit();
		pref.edit().putLong("ID_USER", datos[3]).commit();

	}

	public static long[] getPreferencesAnalytics(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		// --------modify the value
		long datos[] = new long[4];
		datos[0] = pref.getLong("START_SESSION", 0);
		datos[1] = pref.getLong("START_USER", 0);
		datos[2] = pref.getLong("NUMBER_SESSIONS", 0);
		datos[3] = pref.getLong("ID_USER", 0);

		return datos;

	}
	
	public static void setPreferencesUIID(Context context)
	{
		SharedPreferences prefs = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		if (prefs.getString("IDEST", null) == null) {
			UUID uuid = UUID.randomUUID();
			prefs.edit().putString("IDEST", uuid.toString()).commit();
		}
	}
	
	public static String getPreferencesUUID(Context context)
	{
		SharedPreferences prefs = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		String temp = prefs.getString("IDEST", "IDNULL");
		return temp;
	}
	
	public static void setPreferencesDate(Context context, String date) {
		SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		// --------modify the value
		pref.edit().putString("DATE", date).commit();
	}

	public static String getPreferencesDate(Context context) {
		if(context!=null)
		{
			SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
					Context.MODE_PRIVATE);
			// --------modify the value
			String date;
			date = pref.getString("DATE", null);
			return date;
		}
		return null;

	}

}
