package com.speakinbytes.login.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.speakinbytes.login.social.FacebookManager;
import com.speakinbytes.login.social.GPManager;
import com.speakinbytes.login.social.TwitterManager;
import com.speakinbytes.login.utils.Constants;
import com.speakinbytes.login.views.TWToggle.CheckedTwitter;

public class FBToggle extends ToggleButton implements OnCheckedChangeListener {
	/**
	 * SharedPreferences para la consulta de las redes que est�n logueadas
	 */
	private SharedPreferences mSharedPreferences;
	/**
	 * Etiqueta de la clase utilizada en los logs
	 */
	private final String TAG = getClass().getSimpleName();
	/**
	 * Contexto de la aplicaci�n
	 */
	private Context mCtx;
	/**
	 * Listener para notificar del cambio de estado del Toggle
	 */
	private static CheckedFb mListener;
	/**
	 * Indica si hay login delegado o s�lo nativo	
	 */
	private static boolean bDelegado;
	/**
	 * Indica las redes que est�n presentes en la interfaz
	 */
	private static boolean []redes;
	
	private String consumer;
	
	private String secretKey;

	private String facebook;
	public FBToggle(Context context, AttributeSet attrs) {
		super(context, attrs);
		mSharedPreferences = context.getSharedPreferences(
				Constants.PREFERENCIAS, Context.MODE_PRIVATE);
		mCtx = context;
		setOnCheckedChangeListener(this);
		(FacebookManager.newInstance()).initFb(mCtx,mSharedPreferences);
		setChecked((FacebookManager.newInstance()).isFbLoggedInAlready());
		//setEnabled((FacebookManager.newInstance()).isFbLoggedInAlready());
	}

	/**
	 * Asigna los listener para el login delegado y el estado del toggle.
	 * 
	 * @param listener CheckedFb
	 * @param delegado boolean si es delegado
	 * @param rds redes de la interfaz
	 */
	public static void setListener(CheckedFb listener, boolean delegado, boolean[] rds)
	{
		mListener = listener;
		bDelegado = delegado;
		redes = rds;
	}

	/**
	 * M�todo que inicia los datos del manejador de las conexiones nativas con twitter y el estado del toogle en funci�n de si el usuario est� previamente logueado con esa red social
	 * 
	 * @param key clave de la aplicaci�n
	 * @param secret clave screta de la aplicaci�n
	 */
	public void initData(String key, String secret, String facebook)
	{
		consumer = key;
		secretKey = secret;
		this.facebook = facebook;
		
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		
		if(!(FacebookManager.newInstance()).isFbLoggedInAlready())
		{
			AlertDialogSocial.showAlert("GP", mCtx,bDelegado, redes,new String[]{consumer,secretKey},facebook);
			setChecked((FacebookManager.newInstance()).isFbLoggedInAlready());
		}
		else if (mListener!=null && (FacebookManager.newInstance()).isFbLoggedInAlready())
			mListener.setFbChecked(isChecked);
		
	}
	
	public interface CheckedFb
	{
		public void setFbChecked(boolean isChecked);
	}
	

}