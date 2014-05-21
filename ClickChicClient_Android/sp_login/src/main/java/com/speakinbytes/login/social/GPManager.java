package com.speakinbytes.login.social;

import java.io.IOException;
import java.text.BreakIterator;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusShare;
import com.speakinbytes.login.utils.Commons;
import com.speakinbytes.login.utils.Constants;
import com.speakinbytes.login.R;
import com.speakinbytes.utils.Dates;

public class GPManager implements PlusClient.ConnectionCallbacks,
		PlusClient.OnConnectionFailedListener,
		PlusClient.OnAccessRevokedListener {

	/**
	 * Etiqueta
	 */
	private final String TAG = GPManager.this.getClass().getSimpleName();
	/**
	 * Contexto de la aplicaci�n
	 */
	private Context mCtx;
	/**
	 * SharedPrefences
	 */
	private SharedPreferences mSharedPreferences;
	/**
	 * Manejador para el timer
	 */
	private Handler customHandler = null;
	/**
	 * Cliente para Google+
	 */
	private static PlusClient mPlusClient;
	/**
	 * Receptor de resultados de conexi�n con Google+
	 */
	private ConnectionResult mConnectionResult;
	/**
	 * Instancia del objeto
	 */
	private static GPManager _instance;
	/**
	 * Listener de completado
	 */
	private GPLoginListener mGpListener;
	/**
	 * Sem�foro para controlar que no se intente conectar m�s de una vez
	 */
	private boolean bConnecting;
	/**
	 * Control del refresco
	 */
	private boolean bActivity, bRefresh;

	private GPManager() {
	}

	public static GPManager newInstance() {
		if (_instance == null) {
			_instance = new GPManager();

		}
		return _instance;
	}

	/**
	 * 
	 * @param context Contexto de aplicaci�n
	 * @param listener GPLoginListener control de completado de login 
	 * @param sp SharedPreferences
	 * @return GPManager gestor de las conexiones con G+
	 */
	public GPManager initInstance(Context context, GPLoginListener listener,
			SharedPreferences sp) {
		mGpListener = listener;
		mCtx = context;

		mSharedPreferences = sp;
		mPlusClient = new PlusClient.Builder(mCtx, this, this)
				.setActions("http://schemas.google.com/AddActivity",
						"http://schemas.google.com/BuyActivity")
				.setScopes(Scopes.PLUS_LOGIN) // recommended login scope for
												// social features
				// .setScopes("profile") // alternative basic login scope
				.build();

		return this;

	}
	/**
	 * M�todo para revocar el acceso a los datos del usuario en G+
	 */
	public void removeAccess() {

		if (mPlusClient.isConnected()) {
			mPlusClient.clearDefaultAccount();

			mPlusClient.revokeAccessAndDisconnect(this);
		}

	}

	@Override
	public void onAccessRevoked(ConnectionResult status) {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putBoolean(Constants.PREF_KEY_GP_LOGIN, false);
		editor.commit();
		if (status.isSuccess()) {
			if (mGpListener != null)
				mGpListener.isRevokeStatus(true);

		} else {
			if (mGpListener != null)
				mGpListener.isRevokeStatus(false);
			mPlusClient.disconnect();
		}
		mPlusClient.connect();
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		updateToken();

	}

	@Override
	public void onDisconnected() {
		bConnecting = false;
		if (!bActivity)
			mGpListener.isLoginGP(true, null, null);
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (bConnecting) {
			// The user clicked the sign-in button already. Start to resolve
			// connection errors. Wait until onConnected() to dismiss the
			// connection diaif(Constants.debug) Log.
			if (result.hasResolution()) {
				try {
					result.startResolutionForResult((Activity) mCtx,
							Constants.REQUEST_CODE_RESOLVE_ERR);
				} catch (SendIntentException e) {
					mPlusClient.connect();
				}
			}
			bConnecting = false;
		}
		mConnectionResult = result;

	}

	public interface GPLoginListener {
		public void isLoginGP(boolean success, String name, String token);

		public void isRevokeStatus(boolean success);
	}

	public interface GPCommentListener {
		public void sendCommentRt(boolean success);

	}

	public void authorize(int requestCode, int responseCode) {
		if ((requestCode == Constants.REQUEST_CODE_RESOLVE_ERR || requestCode == Constants.REQUEST_CODE_SIGN_IN)
				&& responseCode == Constants.RESULT_OK) {
			mConnectionResult = null;
			if (!mPlusClient.isConnected() && !mPlusClient.isConnecting()) {
				// This time, connect should succeed.
				mPlusClient.connect();
			}
		}

	}

	/**
	 * Comprueba si el usuario est� logueado con G+
	 * @return true si est� logueado
	 * false si no est� logueado
	 */
	public boolean isGPLoggedInAlready() {
		boolean bLogged = false;

		try {
			bLogged = mSharedPreferences.getBoolean(
					Constants.PREF_KEY_GP_LOGIN, false);
			if (bLogged && !mPlusClient.isConnected())
				mPlusClient.connect();
		} catch (Exception e) {
			if (Constants.debug)
				Log.e(TAG, e.toString());
		}
		return bLogged;
	}

	/**
	 * M�todo de gesti�n del login 
	 */
	public void login() {
		bRefresh = false;
		if (!mPlusClient.isConnected() && !mPlusClient.isConnecting()) {
			if (mConnectionResult == null) {
				bConnecting = true;
				mPlusClient.connect();
			} else {
				try {
					mConnectionResult.startResolutionForResult((Activity) mCtx,
							Constants.REQUEST_CODE_RESOLVE_ERR);
				} catch (SendIntentException e) {
					// Try connecting again.
					mConnectionResult = null;
					mPlusClient.connect();
				}
			}

		}

	}


/*	public void connect() {
		if (mConnectionResult == null) {
			bConnecting = true;
			mPlusClient.connect();
			if (customHandler == null) {
				Looper.prepare();
				customHandler = new Handler();
				customHandler.postDelayed(updateTimerThread, 60 * 60 * 1000);
				Looper.loop();
			}
		}
	}*/

	/**
	 * Desconexi�n del cliente de G+
	 */
	public void disconnect() {
		if (mPlusClient != null && mPlusClient.isConnected()) {
			cancelTimers();
			mPlusClient.disconnect();
			bActivity = true;
		}
	}

	/**
	 * Logout con G+
	 */
	public void logout() {
		if (mPlusClient != null) {
			bActivity = false;
			cancelTimers();
			if (mPlusClient.isConnected())
				mPlusClient.clearDefaultAccount();
			mPlusClient.disconnect();
			// mPlusClient.connect();
			bConnecting = false;
			if (mGpListener != null)
				mGpListener.isLoginGP(true, null, null);

		}
	}

	/**
	 * Envio de comentarios. Crea el di�logo de comentar con los datos que se le pasan por par�metro
	 * 
	 * @param comment String con el comentario
	 * @param url Link del contenido 
	 */
	public void sendComment(String comment, String url) {
		// Launch the Google+ share dialog with attribution to your app.
		PlusShare.Builder builder = new PlusShare.Builder(mCtx).setType(
				"text/plain").setText(comment);
		if (url != null)
			builder.setContentUrl(Uri.parse(url));
		Intent shareIntent = builder.getIntent();

		((Activity) mCtx).startActivityForResult(shareIntent, 0);

	}

	/**
	 * Envio de comentarios con im�genes. Crea el di�logo de comentar con los datos que se le pasan por par�metro
	 * 
	 * @param comment String con el comentario
	 * @param url Link del contenido 
	 * @param mime tipo mime que indica que se est� enviando una imagen como contenido extra
	 * 
	 */
	public void sendCommentPicture(String comment, Uri url, String mime) {
		// Launch the Google+ share dialog with attribution to your app.
		PlusShare.Builder builder = new PlusShare.Builder(mCtx);
		builder.setText(comment);
		builder.addStream(url);
		builder.setType(mime);
		Intent shareIntent = builder.getIntent();

		((Activity) mCtx).startActivityForResult(shareIntent, 0);
	}

	/**
	 * Cancelaci�n del timer de refresco del token de G+
	 */
	public void cancelTimers() {
		if (customHandler != null)
			customHandler.removeCallbacks(updateTimerThread);
	}

	/**
	 * Refresco del token con G+, caduca cada hora por lo que habr� que pedirlo siempre que no haya pasado m�s de ese tiempo desde que se almacen�
	 */
	public void refreshToken() {
		bRefresh = true;
		//Comprueba si hay un token almacenado y en que fecha se almacen�
		String date = mSharedPreferences.getString("Fecha", null);
		if (date != null) {
			Date actual = new Date();
			Date saved = null;
			try {
				saved = Dates.StringToDate(date,
						com.speakinbytes.utils.constants.Constants.FULL);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			//Valida que haya pasado m�s de una hora antes de pedir de nuevo el token
			int dif = (int) (actual.getTime() - saved.getTime());
			if (dif > 3600000) {
				bActivity = false;
				if (!mPlusClient.isConnected())
					login();
				else {
					String token = mSharedPreferences.getString(
							"access_token_gp", "");
					AccountManager am = AccountManager.get(mCtx);
					am.invalidateAuthToken("com.google", token);
					updateToken();
				}

			}

		}

	}
	/**
	 * Actualizaci�n de la petici�n de token. Una vez que se ha logueado solicita los datos del usuario
	 */
	private void updateToken() {
		
		if (!bActivity) {
			if (mPlusClient!=null && mPlusClient.isConnected() && mPlusClient.getAccountName() != null) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
						
							if (mPlusClient.getCurrentPerson() != null
									&& mPlusClient.getCurrentPerson().getName() != null
									&& mPlusClient.getCurrentPerson().getName()
											.getFamilyName() != null
									&& mPlusClient.getCurrentPerson().getName()
											.getGivenName() != null) {
								Editor e = mSharedPreferences.edit();
								// Store login status - true
								e.putString(Constants.PREF_KEY_GP_NAME, mPlusClient.getCurrentPerson().getName().getFamilyName()
												+ " "+ mPlusClient.getCurrentPerson().getName().getGivenName());
								e.putString(Constants.PREF_KEY_GP_AVATAR,mPlusClient.getCurrentPerson().getImage().getUrl());
								e.commit(); // save changes
							}
							String token = GoogleAuthUtil.getToken(mCtx,mPlusClient.getAccountName() + "","oauth2:"+ Scopes.PLUS_LOGIN
											+ " " + Scopes.PROFILE
											+ " " + "https://www.googleapis.com/auth/userinfo.email");

							if (Constants.debug)
								Log.i(TAG, " g+ token " + token);

							if (mGpListener != null && !bRefresh) {
								if (mPlusClient.getCurrentPerson() != null&& token != null)
									mGpListener.isLoginGP(true, mPlusClient.getCurrentPerson().getDisplayName(), token);
								else if (token != null)
									mGpListener.isLoginGP(true,mCtx.getString(R.string.unknown),token);
							}

							bConnecting = false;
							if (customHandler == null) {
								Looper.prepare();
								customHandler = new Handler();
								customHandler.postDelayed(updateTimerThread,
										60 * 60 * 1000);
								Looper.loop();
							}
						} catch (UserRecoverableAuthException e) {
							((Activity) mCtx).startActivityForResult(e.getIntent(),	Constants.REQUEST_AUTHORIZATION);
							if (Constants.debug)
								Log.e(TAG, e.toString());
						} catch (IOException e) {
							if (mGpListener != null)
								mGpListener.isLoginGP(false, null, null);
							if (Constants.debug)
								Log.e(TAG, e.toString());
						} catch (GoogleAuthException e) {
							if (mGpListener != null)
								mGpListener.isLoginGP(false, null, null);
							if (Constants.debug)
								Log.e(TAG, e.toString());
						} catch (IllegalStateException e) {
							if (mGpListener != null)
								mGpListener.isLoginGP(false, null, null);
							if (Constants.debug)
								Log.e(TAG, e.toString());
						}
						catch(Exception e){
							if (mGpListener != null)
								mGpListener.isLoginGP(false, null, null);
							if (Constants.debug)
								Log.e(TAG, e.toString());
						}
					}

				}).start();

			}
		}

	}

	private Runnable updateTimerThread = new Runnable() {

		public void run() {
			refreshToken();
		}
	};

}
