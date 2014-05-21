package com.speakinbytes.login.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.ToggleButton;



import android.widget.CompoundButton.OnCheckedChangeListener;

import com.speakinbytes.login.social.TwitterManager;
import com.speakinbytes.login.utils.Constants;

/**
 * Elemento de interfaz de tipo Toggle que autoriza al envio de comentarios
 * 
 * @author bmjuan
 *
 */
public class TWToggle extends ToggleButton implements OnCheckedChangeListener {

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
	private static CheckedTwitter mListener;
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
	/**
	 * Constructor por defecto
	 * @param context
	 * @param attrs
	 */
	public TWToggle(Context context, AttributeSet attrs) {
		super(context, attrs);
		mSharedPreferences = context.getSharedPreferences(
				Constants.PREFERENCIAS, Context.MODE_PRIVATE);
		mCtx = context;
		setOnCheckedChangeListener(this);
		
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
		(TwitterManager.newInstance()).initTwitter(mCtx,key,secret,
				mSharedPreferences);
		setChecked((TwitterManager.newInstance()).isTwitterLoggedInAlready());
		
	}
	
	/**
	 * Asigna los listener para el login delegado y el estado del toggle.
	 * 
	 * @param listener CheckedTwitter
	 * @param delegado boolean si es delegado
	 * @param rds redes de la interfaz
	 */
	public static void setListener(CheckedTwitter listener,boolean delegado, boolean[] rds)
	{
		mListener = listener;
		bDelegado = delegado;
		redes = rds;
	}



	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(!(TwitterManager.newInstance()).isTwitterLoggedInAlready())
		{
			AlertDialogSocial.showAlert("TW", mCtx, bDelegado,redes,new String[]{consumer,secretKey},facebook);
			setChecked((TwitterManager.newInstance()).isTwitterLoggedInAlready());
		}
		else if (mListener!=null && (TwitterManager.newInstance()).isTwitterLoggedInAlready())
			mListener.setTwChecked(isChecked);

	}

	/**
	 * Interfaz de escucha para el toggle
	 * @author bmjuan
	 *
	 */
	public interface CheckedTwitter {
		public void setTwChecked(boolean isChecked);
	}

}
