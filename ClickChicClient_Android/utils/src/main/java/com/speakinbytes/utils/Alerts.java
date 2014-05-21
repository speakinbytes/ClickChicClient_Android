package com.speakinbytes.utils;

import com.speakinbytes.utils.R;
import com.speakinbytes.utils.constants.Constants;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

public class Alerts {
	private static final String TAG="ALERTS";
	/**
	 * M�todo que muestra un di�logo de error.
	 * 
	 * @param context contexto de aplicaci�n
	 */
	public static void showAlertDialog(Context context) {

		try {
			showAlertDialog(context, null);
		} catch (Exception e) {
			if(Constants.debug)
				Log.e(TAG,e.toString());
		}
	}

	/**
	 * M�todo que muestra un di�logo con un texto en concreto con la �nica opci�n de aceptar.
	 * 
	 * @param context Context de la aplicaci�n
	 * @param msg String con el mensaje que se quiere mostrar al usuario
	 */
	public static void showAlertDialog(Context context, String msg) {
		try {
			String error;
			if (msg != null && !msg.equalsIgnoreCase("")) {
				error = msg;
			} else {
				error = context.getString(R.string.error);
			}

			final AlertDialog ad = new AlertDialog.Builder(context).create();
			ad.setMessage(error);
			ad.setButton(AlertDialog.BUTTON_POSITIVE,
					(CharSequence) context.getString(R.string.aceptar),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							ad.dismiss();
						}
					});
			ad.show();
		} catch (Exception e) {
			if(Constants.debug)
				Log.e(TAG,e.toString());
		}
	}
	
	/**
	 * M�todo que muestra un di�logo con un texto en concreto con la �nica opci�n de aceptar.
	 * 
	 * @param context Context de la aplicaci�n
	 * @param msg String con el mensaje que se quiere mostrar al usuario
	 */
	public static void showToast(Context context, String msg) {
		try {
			String error;
			if (msg != null && !msg.equalsIgnoreCase("")) {
				error = msg;
			} else {
				error = context.getString(R.string.error);
			}

			Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
			
		} catch (Exception e) {
			Log.e(TAG,e.toString());
		}
	}
	

}
