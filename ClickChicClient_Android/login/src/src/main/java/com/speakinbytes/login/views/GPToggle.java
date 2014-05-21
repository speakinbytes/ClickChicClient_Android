package com.speakinbytes.login.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.speakinbytes.login.social.FacebookManager;
import com.speakinbytes.login.social.GPManager;

import com.speakinbytes.login.social.TwitterManager;

import com.speakinbytes.login.utils.Constants;

import com.speakinbytes.login.views.FBToggle.CheckedFb;
import com.speakinbytes.login.views.TWToggle.CheckedTwitter;

public class GPToggle extends ToggleButton implements OnCheckedChangeListener {
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
	private static CheckedGp mListener;
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
	public GPToggle(Context context, AttributeSet attrs) {
		super(context, attrs);
		mSharedPreferences = context.getSharedPreferences(
				Constants.PREFERENCIAS, Context.MODE_PRIVATE);
		mCtx = context;
		setOnCheckedChangeListener(this);
		(GPManager.newInstance()).initInstance(mCtx, null, mSharedPreferences);
		setChecked((GPManager.newInstance()).isGPLoggedInAlready());
		setEnabled((GPManager.newInstance()).isGPLoggedInAlready());
	}


	/**
	 * Asigna los listener para el login delegado y el estado del toggle.
	 * 
	 * @param listener CheckedGp
	 * @param delegado boolean si es delegado
	 * @param rds redes de la interfaz
	 */
	public static void setListener(CheckedGp listener, boolean delegado, boolean[] rds)
	{
		mListener = listener;
		bDelegado = delegado;
		redes = rds;
	}

	public void initData(String key, String secret, String facebook)
	{
		consumer = key;
		secretKey = secret;
		this.facebook = facebook;
		
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		
		if(!(GPManager.newInstance()).isGPLoggedInAlready() )
		{
			AlertDialogSocial.showAlert("GP", mCtx, bDelegado, redes,new String[]{consumer, secretKey},facebook);
			setChecked((GPManager.newInstance()).isGPLoggedInAlready() );
		}
		else if (mListener!=null && (GPManager.newInstance()).isGPLoggedInAlready() )
			mListener.setGpChecked(isChecked);
		
	}
	
	/**
	 * Interfaz para la detecci�n de cambio en el toggle
	 * @author bmjuan
	 *
	 */
	public interface CheckedGp
	{
		public void setGpChecked(boolean isChecked);
	}

}