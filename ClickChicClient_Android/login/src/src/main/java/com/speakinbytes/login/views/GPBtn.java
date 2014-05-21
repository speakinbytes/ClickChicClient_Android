package com.speakinbytes.login.views;

import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.speakinbytes.login.social.GPManager;
import com.speakinbytes.login.social.GPManager.GPLoginListener;
import com.speakinbytes.login.utils.Commons;
import com.speakinbytes.login.utils.Constants;
import com.speakinbytes.login.utils.TextListener;
import com.speakinbytes.login.R;
import com.speakinbytes.utils.Dates;

import android.view.View.OnClickListener;

public class GPBtn extends ImageButton implements GPLoginListener, OnClickListener {

	/**
	 * SharedPreferences que contiene la referencias a las conexiones
	 */
	private SharedPreferences mSharedPreferences;
	/**
	 * Etiqueta
	 */
	private final String TAG = "GP_BTN";
	/**
	 * Contexto de aplicaci�n
	 */
	private Context mCtx;
	/**
	 * Boolean que indica si es login o logout
	 */
	private boolean bLogin;
	/**
	 * ProgressDialog para dar feedback al usuario  
	 */
	private static ProgressDialog mConnectionProgressDialog;
	/**
	 * Momento en el que se inicia el proceso de login
	 */
	private long start;
	/**
	 * Es login delegado o no
	 */
	private boolean bDelegado;

	private TextListener mTL;

	/**
	 * Indica si el token se pide como refresco o como el login
	 */
	private boolean bRefresh;

	public GPBtn(Context context, AttributeSet attrs) {
		super(context, attrs);
		mSharedPreferences = context.getSharedPreferences(
				Constants.PREFERENCIAS, Context.MODE_PRIVATE);
		mCtx = context;
		GPBtn.this.setOnClickListener(this);
		Commons.getHttpContext(mCtx);

	}

	/**
	 * Asigna el listener del login delegado
	 */
	public void setRTVELogin(TextListener tl) {

		mTL = tl;
	}
	/**
	 * M�todo que inicia los datos del manejador de las conexiones nativas con facebook y el estado del toogle en funci�n de si el usuario est� previamente logueado con esa red social
	 *
	 */
	public void initData(boolean bDelegado) {
		GPBtn.this.setOnClickListener(this);
		(GPManager.newInstance()).initInstance(mCtx, this,
				mSharedPreferences);
		this.bDelegado = bDelegado;
		if(Constants.debug)
			Log.i(TAG, "is Delegado "+bDelegado);
		boolean isConnected = (GPManager.newInstance()).isGPLoggedInAlready();

		if (isConnected) {
			bLogin = true;
			bRefresh = true;
			(GPManager.newInstance()).refreshToken();
		}

		setSelected(isConnected);
	

	}
	/**
	 * M�todo que gestiona la actualizaci�n de la interfaz despu�s del login nativo de google plus y lanza el login delegado en los siguientes casos:
	 * 1. Cuando el boolean de isDelegado es true
	 * 2. Cuando el usuario no se est� logueado con RTVE
	 */
	public synchronized void onClick(View v) {
		// Progress bar to be displayed if the connection failure is not
		// resolved.
		bRefresh = false;
		start = System.currentTimeMillis();
		if (!(GPManager.newInstance()).isGPLoggedInAlready()) {
			bLogin = true;
			(GPManager.newInstance()).initInstance(mCtx, this,
					mSharedPreferences).login();
			mConnectionProgressDialog = new ProgressDialog(mCtx);
			mConnectionProgressDialog.setMessage(bLogin ? mCtx
					.getString(R.string.login) : mCtx
					.getString(R.string.logout));
			mConnectionProgressDialog.show();
		} else {
			bLogin = false;
			(GPManager.newInstance()).initInstance(mCtx, this,
					mSharedPreferences).logout();
		}

	}

	/**
	 * M�todo de gesti�n de google plus nativo
	 * @param id
	 * @return
	 */
	protected Dialog onCreateDialog(int id) {
		int available = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(mCtx);
		if (available == ConnectionResult.SUCCESS) {
			return null;
		}
		if (GooglePlayServicesUtil.isUserRecoverableError(available)) {
			return GooglePlayServicesUtil.getErrorDialog(available,
					(Activity) mCtx,
					Constants.REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES);
		}
		
		return new AlertDialog.Builder(mCtx).setMessage(R.string.error)
				.setCancelable(true).create();
	}

	/**
	 * M�todo que gestiona la actualizaci�n de la interfaz despu�s del login nativo de google plus y lanza el login delegado en los siguientes casos:
	 * 1. Cuando el boolean de isDelegado es true
	 * 2. Cuando el usuario no se est� logueado con RTVE
	 */
	@Override
	public void isLoginGP(final boolean success, String currentPersonName,
			String token) {
		long value = (System.currentTimeMillis() - start) / 1000;

		if(mTL!=null)
			mTL.updateText( bLogin ? success:!success, Constants.GP);
		if (mConnectionProgressDialog != null) {
			mConnectionProgressDialog.dismiss();
			mConnectionProgressDialog = null;
		}

		if (currentPersonName != null && success && bLogin) {
			if(Constants.debug)
				Log.i(TAG, currentPersonName);

			if (success) {

					// isLoginRTVE(success, Constants.GP);
					((Activity) mCtx).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							GPBtn.this.setSelected(success);
							if (!bRefresh) {
								String message = success ? mCtx
										.getString(R.string.login_gp) : mCtx
										.getString(R.string.error_login_gp);
								Toast.makeText(mCtx, message,
										Toast.LENGTH_SHORT).show();
							}
						}
					});

			}
			SharedPreferences.Editor editor = mSharedPreferences.edit();
			editor.putBoolean(Constants.PREF_KEY_GP_LOGIN, success);
			editor.putString("Fecha", Dates.dateToString(new Date(),
					com.speakinbytes.utils.constants.Constants.FULL));
			editor.commit();
		} else if (!bLogin) {
			((Activity) mCtx).runOnUiThread(new Runnable() {

				@Override
				public void run() {

					setSelected(!success);
					if (!bRefresh) {
						String message = success ? mCtx
								.getString(R.string.closed) : mCtx
								.getString(R.string.error_logout_gp);
						Toast.makeText(mCtx, message, Toast.LENGTH_SHORT)
								.show();
					}

				}
			});

			SharedPreferences.Editor editor = mSharedPreferences.edit();
			editor.putString("access_token_gp", null);
			editor.putBoolean(Constants.PREF_KEY_GP_LOGIN, false);
			editor.putString("Fecha", "");
			editor.commit();

		}

	}

	@Override
	public void isRevokeStatus(boolean success) {

	}

}
