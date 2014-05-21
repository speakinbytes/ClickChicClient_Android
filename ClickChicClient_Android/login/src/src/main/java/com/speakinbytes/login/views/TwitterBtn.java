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

import com.speakinbytes.login.R;
import com.speakinbytes.login.social.TwitterManager;
import com.speakinbytes.login.utils.Commons;
import com.speakinbytes.login.utils.Constants;
import com.speakinbytes.login.utils.TextListener;


/**
 * ImageButton que gestiona las conexiones nativas con Twitter y delegada con RTVE
 * 
 * @author planetmedia
 *
 */
public class TwitterBtn extends ImageButton implements TwitterManager.TwitterLoginListener,
		OnClickListener {

	/**
	 * SharedPreferences que contiene la referencias a las conexiones
	 */
	private SharedPreferences mSharedPreferences;
	/**
	 * Etiqueta
	 */
	private final String TAG = "TW_BTN";
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
	/**
	 * Listener para el proceso de login delegado
	 */
	private TextListener mtl;
	

	public TwitterBtn(Context context, AttributeSet attrs) {
		super(context, attrs);
		mSharedPreferences = context.getSharedPreferences(
				Constants.PREFERENCIAS, Context.MODE_PRIVATE);
		mCtx = context;
		setOnClickListener(this);
		Commons.getHttpContext(mCtx);

	}

	/**
	 * Asigna el listener del login delegado
	 */
	public void setListenerLogin(TextListener twitterTextListener ) {
		mtl = twitterTextListener;
	}

	/**
	 * M�todo que inicia los datos del manejador de las conexiones nativas con twitter y el estado del toogle en funci�n de si el usuario est� previamente logueado con esa red social
	 * 
	 * @param key clave de la aplicaci�n
	 * @param secret clave screta de la aplicaci�n
	 */
	public void initData(String key, String secret, boolean bDelegado) {
		(TwitterManager.newInstance()).initTwitter(mCtx, key, secret, this,
				mSharedPreferences);
		this.bDelegado = bDelegado;
		setSelected((TwitterManager.newInstance()).isTwitterLoggedInAlready());
	}

	/**
	 * M�todo que gestiona la actualizaci�n de la interfaz despu�s del login nativo de twitter y lanza el login delegado en los siguientes casos:
	 * 1. Cuando el boolean de isDelegado es true
	 * 2. Cuando el usuario no se est� logueado con RTVE
	 */
	@Override
	public void isLoginTw(final boolean success) {

		long value = (System.currentTimeMillis() - start) / 1000;

		if(mtl!=null)
			mtl.updateText( bLogin ? success:!success, Constants.TWITTER);
		if (success && bLogin) {

				((Activity) mCtx).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (mConnectionProgressDialog != null)
							mConnectionProgressDialog.dismiss();
						Toast.makeText(mCtx,
								mCtx.getString(R.string.login_twitter),
								Toast.LENGTH_SHORT).show();
						setSelected(success);
						
					}
				});

			SharedPreferences.Editor editor = mSharedPreferences.edit();
			editor.putBoolean(Constants.PREF_KEY_TWITTER_LOGIN, success);
			editor.commit();
			
		} else {
			((Activity) mCtx).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (!success) {
						final String message = bLogin ? mCtx
								.getString(R.string.error_login_twitter) : mCtx
								.getString(R.string.error_logout_twitter);

						Handler h = new Handler();
						h.post(new Runnable() {

							@Override
							public void run() {
								if (mConnectionProgressDialog != null)
									mConnectionProgressDialog.dismiss();
								Toast.makeText(mCtx, message,
										Toast.LENGTH_SHORT).show();

							}
						});
					} else {
						Handler h = new Handler();
						h.post(new Runnable() {

							@Override
							public void run() {
							try
							{
								if (mConnectionProgressDialog != null)
									mConnectionProgressDialog.dismiss();
								Toast.makeText(mCtx,
										mCtx.getString(R.string.closed),
										Toast.LENGTH_SHORT).show();
							}catch(Exception e)
							{
								if(Constants.debug) Log.e(TAG, "Error "+e.toString());
							}
							}
						});
					}
					setSelected((!bLogin) ? !success : success);

				}
			});

		}

	}

	/**
	 * M�todo que gestiona el login/logout de la red social Twitter
	 */
	@Override
	public synchronized void onClick(View v) {
		start = System.currentTimeMillis();
		if (!(TwitterManager.newInstance()).isTwitterLoggedInAlready()) {
			bLogin = true;
			(TwitterManager.newInstance()).login();
		} else {
			bLogin = false;
			(TwitterManager.newInstance()).logout();
		}
		mConnectionProgressDialog = new ProgressDialog(mCtx);
		mConnectionProgressDialog.setMessage(bLogin ? mCtx
				.getString(R.string.login) : mCtx.getString(R.string.logout));
		mConnectionProgressDialog.show();

	}

	public interface TwitterTextListener {
		public void updateText(boolean success);
	}
}
