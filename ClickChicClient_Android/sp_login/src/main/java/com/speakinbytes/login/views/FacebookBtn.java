package com.speakinbytes.login.views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

import com.facebook.android.Facebook;
import com.speakinbytes.login.social.FacebookManager;

import com.speakinbytes.login.social.FacebookManager.FbLoginListener;
import com.speakinbytes.login.utils.Commons;
import com.speakinbytes.login.utils.Constants;
import com.speakinbytes.login.utils.TextListener;
import com.speakinbytes.login.R;

public class FacebookBtn extends ImageButton implements FbLoginListener, OnClickListener {

	/**
	 * SharedPreferences que contiene la referencias a las conexiones
	 */
	private SharedPreferences mSharedPreferences;
	/**
	 * Etiqueta
	 */
	private final String TAG = getClass().getSimpleName();
	/**
	 * Contexto de aplicaci�n
	 */
	private static Context mCtx;
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
	/**
	 * Objeto facebook con el id de aplicaci�n
	 */
	private Facebook mFb;
	
	private TextListener mtl;
	
	public FacebookBtn(Context context, AttributeSet attrs) {
		super(context, attrs);
		mCtx = context;
		mSharedPreferences = context.getSharedPreferences(
				Constants.PREFERENCIAS, Context.MODE_PRIVATE);
		setOnClickListener(this);

	}

	/**
	 * M�todo que inicia los datos del manejador de las conexiones nativas con facebook y el estado del toogle en funci�n de si el usuario est� previamente logueado con esa red social

	 */
	public void initData(String id_app, boolean bDelegado) {
		mFb = (FacebookManager.newInstance()).initFb(mCtx, id_app, this,
				mSharedPreferences);
		this.bDelegado = bDelegado;
		setSelected((FacebookManager.newInstance()).isFbLoggedInAlready());

	}
	
	/**
	 * M�todo que inicia los datos del manejador de las conexiones nativas con facebook y el estado del toogle en funci�n de si el usuario est� previamente logueado con esa red social
	 *
	 */
	public void initData(String id_app, boolean bDelegado, Context context) {
		mCtx = context;
		mFb = (FacebookManager.newInstance()).initFb(mCtx, id_app, this,
				mSharedPreferences);
		this.bDelegado = bDelegado;
		setSelected((FacebookManager.newInstance()).isFbLoggedInAlready());

	}
	/**
	 * Asigna el listener del login delegado
	 */
	public void setRTVELogin(TextListener tl) {
		mtl = tl;
	}

	/**
	 * M�todo que gestiona la actualizaci�n de la interfaz despu�s del login nativo de facebook y lanza el login delegado en los siguientes casos:
	 * 1. Cuando el boolean de isDelegado es true
	 * 2. Cuando el usuario no se est� logueado con RTVE
	 */
	@Override
	public void isLoginFb(final boolean success) {

		if(mtl!=null)
			mtl.updateText( bLogin ? success:!success, Constants.FACEBOOK);
		if (success && bLogin) {

			(FacebookManager.newInstance()).getProfileInformation();

			SharedPreferences.Editor editor = mSharedPreferences.edit();
			editor.putBoolean(Constants.PREF_KEY_FB_LOGIN, success);
			editor.commit();
		} else {
			if (!success) {
				final String message = bLogin ? mCtx
						.getString(R.string.error_login_fb) : mCtx
						.getString(R.string.error_logout_fb);

				((Activity) mCtx).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(mCtx, message, Toast.LENGTH_SHORT)
								.show();

					}
				});
			}

			((Activity) mCtx).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(mCtx,
							mCtx.getString(R.string.closed),
							Toast.LENGTH_SHORT).show();
					//setSelected(!success);

				}
			});

		}

	}
	
	/**
	 * M�todo que gestiona el login/logout de la red social Facebook
	 */

	@Override
	public void onClick(View v) {

		start = System.currentTimeMillis();
		if (!(FacebookManager.newInstance()).isFbLoggedInAlready() && !bLogin) {
			bLogin = true;
			mConnectionProgressDialog = new ProgressDialog(mCtx);
			mConnectionProgressDialog.setMessage("Iniciando sesi�n..");
			((Activity) mCtx).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mConnectionProgressDialog.show();
				}
			});
			(FacebookManager.newInstance()).login(new String[] {
					"publish_stream", "email" });
		} else {
			bLogin = false;
			(FacebookManager.newInstance()).logout();
			FacebookBtn.this.setSelected(false);
		}

	}

}