package com.speakinbytes.login.views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.android.FacebookError;
import com.speakinbytes.login.models.FBPostModel;
import com.speakinbytes.login.models.PostModel;
import com.speakinbytes.login.social.FacebookManager;
import com.speakinbytes.login.social.GPManager;
import com.speakinbytes.login.social.TwitterManager;
import com.speakinbytes.login.social.FacebookManager.FbCommentListener;
import com.speakinbytes.login.social.TwitterManager.TwitterCommentListener;
import com.speakinbytes.login.utils.Constants;
import com.speakinbytes.login.views.FBToggle.CheckedFb;
import com.speakinbytes.login.views.GPToggle.CheckedGp;
import com.speakinbytes.login.views.TWToggle.CheckedTwitter;
import com.speakinbytes.login.R;

public class SendButton extends ImageButton implements FbCommentListener,TwitterCommentListener, OnClickListener,
		CheckedFb, CheckedTwitter, CheckedGp {

	/**
	 * SharedPreferences que contiene la referencias a las conexiones
	 */
	private SharedPreferences mSharedPreferences;
	/**
	 * Etiqueta
	 */
	private final String TAG = "SendButton";
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
	 * Listener para el proceso de envio
	 */
	private SendCompleteListener mListener;
	/**
	 * Boolean de control para el envio de comentarios
	 */
	private boolean bSending;
	/**
	 * Modelo de datos con la informaci�n a compartir
	 */
	private PostModel post;

	/**
	 * Booleans que indican que redes sociales est�n seleccionadas para
	 * compartir
	 */
	private boolean bFbChecked, bTwChecked, bGpChecked;
	/**
	 * Boolean que indica si se incluye una imagen
	 */
	private boolean bPicture = false;

	public SendButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		mSharedPreferences = context.getSharedPreferences(
				Constants.PREFERENCIAS, Context.MODE_PRIVATE);
		mCtx = context;
		setOnClickListener(this);

	}

	/**
	 * Inicializaci�n de los manejadores de redes sociales.
	 * 
	 * @param id_app
	 *            identificador d ela aplicaci�n de facebook
	 * @param key
	 *            clave de la aplicaci�n de twitter
	 * @param secret
	 *            clave secreta de la aplicaci�n de twitter
	 * @param listener
	 *            listener para el control del envio
	 * @param bPicture
	 *            indica si se incluye una imagen para compartir
	 * @param bDelegado
	 *            indica si se precisa de login delegado
	 * @param redes
	 *            redes sociales que se incluyen en la interfaz
	 */
	public void initData(String id_app, String key, String secret,
			SendCompleteListener listener, boolean bPicture, boolean bDelegado,
			boolean[] redes) {
		mListener = listener;
		this.bPicture = bPicture;

		FBToggle.setListener(this, bDelegado, redes);
		TWToggle.setListener(this, bDelegado, redes);

		GPToggle.setListener(this, bDelegado, redes);

		(FacebookManager.newInstance()).initFb(mCtx, id_app, null,
				mSharedPreferences);

		(TwitterManager.newInstance()).initTwitter(mCtx, null, key, secret,
				mSharedPreferences);
		(GPManager.newInstance()).initInstance(mCtx, null, mSharedPreferences);

		bFbChecked = (FacebookManager.newInstance()).isFbLoggedInAlready();
		bTwChecked = (TwitterManager.newInstance()).isTwitterLoggedInAlready();

		bGpChecked = (GPManager.newInstance()).isGPLoggedInAlready();

		// Si ninguna red social est� logueada no se puede compartir
		setClickable((bFbChecked || bTwChecked  || bGpChecked) ? true
				: false);
	}

	/**
	 * Gesti�n del resultado de compartir a trav�s de facebook. Envio de
	 * estad�sticas, envio del share a RTVE y notificaci�n al usuario.
	 */
	@Override
	public void sendCommentFb(final boolean success) {

		long value = (System.currentTimeMillis() - start) / 1000;


		mListener.setComplete(success);
		bSending = false;


		((Activity) mCtx).runOnUiThread(new Runnable() {
			@Override
			public void run() {

				String message = success ? mCtx
						.getString(R.string.update_status_facebook) : mCtx
						.getString(R.string.error_post_fb);
				Toast.makeText(mCtx, message, Toast.LENGTH_SHORT).show();

			}
		});
		if (mConnectionProgressDialog != null)
			mConnectionProgressDialog.dismiss();
	}

	/**
	 * Gesti�n del evento onClick. Envia la informaci�n a las redes sociales
	 * seleccionadas a trav�s de los toggles, y que se indican a trav�s del
	 * estado seleccionado.
	 */
	private void showDialog() {
		if (mConnectionProgressDialog == null
				|| !mConnectionProgressDialog.isShowing()) {
			mConnectionProgressDialog = new ProgressDialog(mCtx);
			mConnectionProgressDialog.setMessage("Enviando...");
			mConnectionProgressDialog.show();
		}
	}

	@Override
	public void onClick(View v) {
		start = System.currentTimeMillis();

		post = (PostModel) v.getTag();

		if ((FacebookManager.newInstance()).isFbLoggedInAlready()
				|| (TwitterManager.newInstance()).isTwitterLoggedInAlready()
				|| (GPManager.newInstance()).isGPLoggedInAlready()) {

			String aux = subsEntity(shortUrl(post.getComentario()));
			String texto_rtveTrim = aux.trim();
			if (texto_rtveTrim.equals("")) {
				Toast.makeText(mCtx, mCtx.getString(R.string.texto_vacio),
						Toast.LENGTH_SHORT).show();
			} else if (post != null && !bSending) {
				if (bFbChecked) {
					showDialog();
					String url = null;
					if (post instanceof FBPostModel)
						url = ((FBPostModel) post).getUrl();
					(FacebookManager.newInstance()).postToWall(
							subsEntity(shortUrl(post.getComentario())), url,
							((FBPostModel) post).getUrlImage(),
							((FBPostModel) post).getMime(), this);
				}

				if (bTwChecked) {
					(TwitterManager.newInstance()).update(post.getComentario(),
							this);
					showDialog();
				}

				if (bGpChecked) {

					String url = null;
					if (post instanceof FBPostModel) {
						url = ((FBPostModel) post).getUrl();

					}
					if (((FBPostModel) post).getUrlImage() != null
							&& !((FBPostModel) post).getUrlImage().equals("")) {
						(GPManager.newInstance()).sendCommentPicture(
								subsEntity(shortUrl(post.getComentario())),
								Uri.parse(((FBPostModel) post).getUrlImage()),
								((FBPostModel) post).getMime());
					} else
						(GPManager.newInstance())
								.sendComment(subsEntity(shortUrl(post
										.getComentario())), url);



				}
			}
		}

	}

	private String shortUrl(String url) {
		// METER TEXTO COMPARTIR SIN WWW.RTVE.ES/MT
		if ((url).contains("http://www.rtve.es/mt/")) {
			url = url.replace("http://www.rtve.es/mt/", "&&&&");
			int inicio = url.indexOf("&&&&");
			url = url.substring(0, inicio);
		}

		return url;
	}

	private String subsEntity(String url) {
		// METER TEXTO COMPARTIR SIN @V�A MAS_TVE
		// Si el texto contiene la cadena... la sustituimos
		if (post.getApp_entity() != null
				&& (url).contains(post.getApp_entity())) {
			url = url.replace(post.getApp_entity(), "&&&&");
			int inicio = url.indexOf("&&&&");
			url = url.substring(0, inicio);
		}
		return url;
	}

	private String deleteHashTags(String comment) {
		if (post.getHashTags() != null && !post.getHashTags().equals("")
				&& comment.contains(post.getHashTags())) {
			comment = comment.replaceAll(post.getHashTags(), "");
		}
		return comment;
	}


	/**
	 * Gesti�n del resultado de compartir a trav�s de twitter. Envio de
	 * estad�sticas, envio del share a RTVE y notificaci�n al usuario.
	 */
	@Override
	public void sendComment(final boolean success) {
		bSending = false;
		long value = (System.currentTimeMillis() - start) / 1000;

		mListener.setComplete(success);

		((Activity) mCtx).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				String message = success ? mCtx
						.getString(R.string.update_status_twitter) : mCtx
						.getString(R.string.error_status_twitter);
				Toast.makeText(mCtx, message, Toast.LENGTH_SHORT).show();

			}
		});
		if (mConnectionProgressDialog != null)
			mConnectionProgressDialog.dismiss();

	}

	/**
	 * Cambio del estado del toggle de Twitter
	 */
	@Override
	public void setTwChecked(boolean isChecked) {
		bTwChecked = isChecked;

	}

	/**
	 * Cambio del estado del toggle de Facebook
	 */
	@Override
	public void setFbChecked(boolean isChecked) {
		bFbChecked = isChecked;

	}

	/**
	 * Cambio del estado del toggle de Google Plus
	 */
	@Override
	public void setGpChecked(boolean isChecked) {
		bGpChecked = isChecked;

	}

	/**
	 * Interfaz de escuchara para el envio completado de comentarios
	 */
	public interface SendCompleteListener {
		public void setComplete(boolean isComplete);
	}

	/**
	 * Gesti�n de los posibles errores derivados del envio de comentarios, login
	 * y logout de facebook.
	 */
	@Override
	public void error(int state, Exception e) {

		switch (state) {
		case FacebookManager.LOGOUT:
			long value = (System.currentTimeMillis() - start) / 1000;

			Toast.makeText(
					mCtx,
					mCtx.getString(R.string.error_logout_fb) + " "
							+ e.getMessage(), Toast.LENGTH_SHORT).show();
			break;
		case FacebookManager.PERFIL:
			Toast.makeText(
					mCtx,
					mCtx.getString(R.string.error_perfil_fb) + " "
							+ e.getMessage(), Toast.LENGTH_SHORT).show();
			break;
		case FacebookManager.LOGIN:

			Toast.makeText(
					mCtx,
					mCtx.getString(R.string.error_login_fb) + " "
							+ e.getMessage(), Toast.LENGTH_SHORT).show();
			break;
		case FacebookManager.POST:

			Toast.makeText(
					mCtx,
					mCtx.getString(R.string.error_post_fb) + " "
							+ e.getMessage(), Toast.LENGTH_SHORT).show();
			break;

		}
		if (mConnectionProgressDialog != null)
			mConnectionProgressDialog.dismiss();
	}

	/**
	 * Notificaci�n de errores de facebook al hacer logout
	 */
	@Override
	public void fbError(int state, final FacebookError e) {

		((Activity) mCtx).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(
						mCtx,
						mCtx.getString(R.string.error_fb) + " "
								+ e.getErrorType(), Toast.LENGTH_SHORT).show();
			}
		});
	}

}