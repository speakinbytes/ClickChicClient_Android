package com.speakinbytes.utils;

import java.util.List;

import com.speakinbytes.utils.constants.Constants;

import android.util.Log;

public class Basic {
	
	private static final String TAG="BASIC";
	
	/**
	 * M�todo que duerme el hilo principal de la aplicaci�n durante un tiempo determinado
	 * 
	 * @param time tiempo en milisegundos que se quiere dormir el hilo.
	 */
	public static void sleep(long time) {

		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			if(Constants.debug)
				Log.e(TAG, "Sleep "+ e.toString());
		}
	}

	/**
	 * M�todo que valida si una lista est� vacia
	 * 
	 * @param lista List que se quiere comprobar
	 * 
	 * @return true si la lista no es null y contiene alg�n elemento
	 *         false si la lista es null o no contiene ning�n elemento
	 */
	public static boolean isEmptyList(List<?> lista) {

		if (lista != null && lista.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * M�todo que valida si un string se corresponde con una URL 
	 * 
	 * @param str String que se quiere validar
	 * 
	 * @return true si comienza por http://
	 *         false si no es asi 
	 */
	public static boolean isUrl(String str) {
		if (str.contains("http://")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * M�todo que valida si un string se corresponde con una integer 
	 * 
	 * @param str String que se quiere validar
	 * 
	 * @return true si se corresponde con un integer
	 *         false si no es asi 
	 */
	public static boolean isInteger(String str) {

		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}

	}

}
