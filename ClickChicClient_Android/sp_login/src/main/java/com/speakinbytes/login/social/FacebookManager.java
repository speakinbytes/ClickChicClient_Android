package com.speakinbytes.login.social;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.DialogListener;
import com.speakinbytes.login.models.FB_perf;
import com.speakinbytes.login.utils.Constants;

public class FacebookManager implements RequestListener, DialogListener {

	/**
	 * Etiqueta de la clase
	 */
	private static final String TAG = "FacebookManager";
	// Shared Preferences
	/**
	 * SharedPreferences
	 */
	private SharedPreferences mSharedPreferences;
	/**
	 * Asynctask interno de logueo a Facebook
	 */
	private AsyncFacebookRunner mAsyncRunner;
	/**
	 * Listener de completado del login con Facebook
	 */
	private static FbLoginListener mLoginListener;
	/**
	 * Listener de completado de envio de comentarios
	 */
	private static FbCommentListener mCommentListener;
	/**
	 * Contexto de aplicaci�n
	 */
	private Context mCtx;

	/**
	 * Your Facebook APP ID
	 */
	private String APP_ID;
	/**
	 *  Instance of Facebook Class
	 */
	private Facebook facebook;
	/**
	 * Tipo de operaci�n
	 */
	public int mState = -1;
	public static final int LOGIN = -1;
	public static final int LOGOUT = 0;
	public static final int PERFIL = 1;
	public static final int POST = 2;
	/**
	 * Instancia de la clase
	 */
	private static FacebookManager _instance;

	private FacebookManager() {
	}

	public static FacebookManager newInstance() {
		if (_instance == null) {
			_instance = new FacebookManager();
		}
		return _instance;
	}

	/**
	 * Inicializaci�n de par�metros
	 * @param context Context
	 * @param sp SharedPreferences
	 */
	public void initFb(Context context, SharedPreferences sp) {

		mCtx = context;
		mSharedPreferences = sp;

	}

	/**
	 * Inicializaci�n de par�metros
	 * 
	 * @param context Context
	 * @param key id de la aplicaci�n de facebook
	 * @param listener Listener del login con facebook
	 * @param sp SharedPreferences
	 * @return Facebook
	 */
	public Facebook initFb(Context context, String key,
			FbLoginListener listener, SharedPreferences sp) {
		mCtx = context;
		mSharedPreferences = sp;
		mLoginListener = listener;
		if (mAsyncRunner == null) {
			APP_ID = key;
			facebook = new Facebook(APP_ID);
			mAsyncRunner = new AsyncFacebookRunner(facebook);
			// APP_ID = "1395339327376463";
			APP_ID = key;
			logAutoFacebook();
		}
		return facebook;
	}

	/**
	 * Autorizaci�n de acceso a la aplicaci�n nativa de facebook
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void authorize(int requestCode, int resultCode, Intent data) {
		try {
			facebook.authorizeCallback(requestCode, resultCode, data);
		} catch (NullPointerException e) {
			if(Constants.debug)
			{
				Log.e(TAG, e.toString());
			}
		}
	}

	/**
	 * Function to Logout user from Facebook
	 * */
	public void logout() {
		mState = LOGOUT;
		mAsyncRunner.logout(mCtx, this);
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putBoolean(Constants.PREF_KEY_FB_LOGIN, false);
		editor.commit();
	}
	/**
	 * Function to Login user to Facebook with an known token
	 * */
	private void logAutoFacebook() {
		String access_token = null;
		try {
			access_token = mSharedPreferences.getString("access_token", null);
			//long expires = mSharedPreferences.getLong("access_expires", 0);

		} catch (NullPointerException e) {
			if (Constants.debug)
				Log.e(TAG, e.toString());
		}
		if (access_token != null)
			facebook.setAccessToken(access_token);

		facebook.setAccessExpires(0);

	}

	/**
	 * Function to login into facebook
	 * */
	public void login(String[] permisions) {
		logAutoFacebook();
		
		if (!facebook.isSessionValid()) {
			facebook.authorize((Activity) mCtx, permisions, this);

		} else {
			SharedPreferences.Editor editor = mSharedPreferences.edit();
			editor.putBoolean(Constants.PREF_KEY_FB_LOGIN, true);
			editor.commit();
			if (mLoginListener != null)
				mLoginListener.isLoginFb(true);

		}
	}

	/**
	 * Function to post to facebook wall
	 * */
	public void postToWall(final String comentario, final String url,
			final String urlImage, final String mime,
			final FbCommentListener comentListener) {
		// post on user's wall.

		new Thread(new Runnable() {

			@Override
			public void run() {
				mCommentListener = comentListener;
				try {
					mState = POST;
					if (facebook == null)
						return;

					mAsyncRunner = new AsyncFacebookRunner(facebook);

					Bundle parameters = new Bundle();

					if (url != null && !url.equals("")
							&& (urlImage == null || urlImage.equals(""))) {
						
						parameters.putString("link", url);
						parameters.putString("message", comentario);
						mAsyncRunner.request("me/feed", parameters, "POST",
								FacebookManager.this, null);
					}
					if (urlImage != null && !urlImage.equals("")) {
						// No acaba de funcionar
						Bitmap bmp = BitmapFactory.decodeStream(mCtx
								.getContentResolver().openInputStream(
										Uri.parse(urlImage)));
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
						byte[] byteArray = stream.toByteArray();
						parameters.putString("method", "photos.upload");
						parameters.putByteArray("picture", byteArray);
						parameters.putString("caption", comentario);
						mAsyncRunner.request(null, parameters, "POST",
								FacebookManager.this, null);

					}

				} catch (Exception e) {
				
					if (Constants.debug)
					{
						e.printStackTrace();
						Log.e(TAG, "Blank response");
					}
				}

			}
		}).start();

	}

	/**
	 * Get Profile information by making request to Facebook Graph API
	 * */
	public void getProfileInformation() {
		mState = PERFIL;
		mAsyncRunner.request("me", this);
	}

	@Override
	public void onCancel() {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putBoolean(Constants.PREF_KEY_FB_LOGIN, false);
		editor.putString("access_token", null);
		editor.commit();
		if (mLoginListener != null)
			mLoginListener.isLoginFb(false);
	}

	@Override
	public void onComplete(Bundle values) {
		// Function to handle complete event
		// Edit Preferences and update facebook acess_token
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putString("access_token", facebook.getAccessToken());
		editor.putLong("access_expires", 0);
		editor.commit();
		facebook.setAccessExpires(0);
		if (mLoginListener != null)
			mLoginListener.isLoginFb(true);

	}

	@Override
	public void onError(DialogError error) {
		// Function to handle error
		// if(Constants.debug) Log.e(FRAGMENT_NAME, error.toString());
		if (mLoginListener != null)
			mLoginListener.isLoginFb(false);

	}

	@Override
	public void onFacebookError(FacebookError fberror) {
		if (mLoginListener != null)
			mLoginListener.isLoginFb(false);
	}

	@Override
	public void onComplete(String response, Object state) {
		switch (mState) {
		case LOGOUT:
			SharedPreferences.Editor editor = mSharedPreferences.edit();
			editor.putString("access_token", null);
			editor.putLong("access_expires", 0);
			editor.putBoolean(Constants.PREF_KEY_FB_LOGIN, false);
			editor.commit();
			if (mLoginListener != null)
				mLoginListener.isLoginFb(true);
			break;

		case PERFIL:
			new FB_perf().parserFBData(response, mSharedPreferences);
			break;
		case POST:
			if (mCommentListener != null && response != null)
				mCommentListener
						.sendCommentFb((response.contains("error")) ? false
								: true);
			else
				mCommentListener.sendCommentFb(false);
			break;

		}

	}

	@Override
	public void onIOException(IOException e, Object state) {
	if(mState == LOGOUT && mLoginListener != null)
			mLoginListener.isLoginFb(false);
	
		if(Constants.debug)
			Log.i(TAG, e.toString() + " state "+state);
		if (mCommentListener != null)
			mCommentListener.error(mState, e);

	}

	@Override
	public void onFileNotFoundException(FileNotFoundException e, Object state) {
		if(Constants.debug)
			Log.i(TAG, e.toString() + " state "+state);
		if(mState == LOGOUT && mLoginListener != null)
			mLoginListener.isLoginFb(false);
		if (mCommentListener != null)
			mCommentListener.error(mState, e);
	}

	@Override
	public void onMalformedURLException(MalformedURLException e, Object state) {
		if(Constants.debug)
			Log.i(TAG, e.toString() + " state "+state);
		if(mState == LOGOUT && mLoginListener != null)
			mLoginListener.isLoginFb(false);
		if (mCommentListener != null)
			mCommentListener.error(mState, e);
	}

	@Override
	public void onFacebookError(FacebookError e, Object state) {
		if(Constants.debug)
			Log.i(TAG, e.toString() + " state "+state);
		if(mState == LOGOUT && mLoginListener != null)
			mLoginListener.isLoginFb(false);
		if (mCommentListener != null)
			mCommentListener.fbError(mState, e);

	}

	/**
	 * Check user already logged in your application using twitter Login flag is
	 * fetched from Shared Preferences
	 * */
	public boolean isFbLoggedInAlready() {
		boolean bLogged = false;
		
		try {
			bLogged = mSharedPreferences.getBoolean(
					Constants.PREF_KEY_FB_LOGIN, false);
		} catch (Exception e) {
			if (Constants.debug)
				Log.e(TAG, e.toString());
		}
		return bLogged;
	}

	public interface FbLoginListener {
		public void isLoginFb(boolean success);
	}

	public interface FbCommentListener {
		public void sendCommentFb(boolean success);

		public void error(int state, Exception e);

		public void fbError(int state, FacebookError e);
	}
}
