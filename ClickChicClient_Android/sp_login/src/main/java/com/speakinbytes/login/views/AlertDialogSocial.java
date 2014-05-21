package com.speakinbytes.login.views;

import com.speakinbytes.login.fragments.LoginFragment;

import com.speakinbytes.login.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.inputmethod.InputMethodManager;

public class AlertDialogSocial {

	/**
	 * M�todo que muestra un AlertDialog para dar la opci�n al usuario de loguearse con la red social elegida
	 * 
	 * @param redSocial String del nombre de la red social con la que se quiere compartir y a la que se da la opci�n de loguear
	 * @param ctx Contexto de la aplicaci�n
	 * @param bDelegado boolean que indica si tiene que ser un login delegado o no
	 * @param redes array de booleans identificativos de las redes sociales incluidas en la aplicaci�n
	 */
	public static void showAlert(String redSocial, final Context ctx, final boolean bDelegado, final boolean[] redes,final String[] twKeys,final String facebook) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(
				String.format(ctx.getString(R.string.wish_connect), redSocial))
				.setCancelable(false)
				.setPositiveButton(ctx.getString(R.string.exit_si),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								try {
									InputMethodManager inputManager = (InputMethodManager) ctx
											.getSystemService(Context.INPUT_METHOD_SERVICE);
									inputManager.hideSoftInputFromWindow(
											((Activity) ctx).getCurrentFocus()
													.getWindowToken(),
											InputMethodManager.HIDE_NOT_ALWAYS);
								} catch (NullPointerException e) {
								}
								//Si quire conectarse se le muestra la pantalla de login
								showDialog((FragmentActivity) ctx, bDelegado,redes,twKeys,facebook);
								dialog.cancel();

							}
						})
				.setNegativeButton(ctx.getString(R.string.exit_no),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * M�todo que instancia al fragment de login, que contiene los botones de login
	 * 
	 * @param ctx Contexto de la aplicaci�n
	 * @param bDelegado boolean que indica si tiene que ser un login delegado o no
	 * @param redes array de booleans identificativos de las redes sociales incluidas en la aplicaci�n
	 */
	private static void showDialog(FragmentActivity ctx, boolean bDelegado, boolean[] redes, String[] twKeys, String facebook) {
		// DialogFragment.show() will take care of adding the fragment
		// in a transaction. We also want to remove any currently showing
		// dialog, so make our own transaction and take care of that here.
		FragmentTransaction ft = ctx.getSupportFragmentManager().beginTransaction();
		Fragment prev = ctx.getSupportFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		DialogFragment newFragment = LoginFragment.newInstance(2, bDelegado, redes,twKeys, facebook);
		newFragment.show(ft, "dialog");
	}

}
