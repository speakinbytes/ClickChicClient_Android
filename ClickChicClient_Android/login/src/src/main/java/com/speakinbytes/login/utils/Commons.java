package com.speakinbytes.login.utils;

import java.util.List;

import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.protocol.BasicHttpContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.speakinbytes.login.cookies.PersistentCookieStore;
import com.speakinbytes.login.social.GPManager;

public class Commons {
	
	/**
	 * Objecto para la sincronizaci�n de cookies
	 */
	private static CookieSyncManager syncManager;
	/**
	 * Almacen persistente de cookies
	 */
	private static PersistentCookieStore myCookieStore;
	/**
	 * Manejador de cookies
	 */
	private static CookieManager cookieManager;
	/**
	 * Contexto de peticiones HTTP 
	 */
	private static BasicHttpContext mHttpContext; 
	
	/**
	 * M�todo que crea un HttpContext utilizando las cookies almacenadas
	 * @param context contexto de aplicaci�n
	 * @return BasicHttpContext
	 */
	public static BasicHttpContext getHttpContext(Context context) {
		
		if(mHttpContext==null)
		{
			mHttpContext = new BasicHttpContext();
			try {
				mHttpContext.setAttribute(ClientContext.COOKIE_STORE, inicCookies(context));
				
			} catch (NullPointerException np) {
				if(Constants.debug)
				{
					Log.e("COMMONS", np.toString());
					np.printStackTrace();
				}
			}
		}
		return mHttpContext;
	}
	
	/**
	 * Inicializaci�n de los objetos de gesti�n de cookies
	 * @param context contexto de aplicaci�n
	 * @return PersistenCookieStore
	 */
	private static PersistentCookieStore inicCookies(Context context)
	{
		if(myCookieStore==null)
		{
			syncManager = CookieSyncManager.createInstance(context);
			cookieManager = CookieManager.getInstance();
			cookieManager.setAcceptCookie(true);
			myCookieStore = new PersistentCookieStore(context);
		}
		
		return myCookieStore;
			
	}
	
	/**
	 * Borrado de cookies
	 */
	public static void removeCookies()
	{
		// https://secure.rtve.es/usuarios/secure/logout
		myCookieStore.clear();
		cookieManager.removeAllCookie();
		syncManager.sync();
	}
	/**
	 * Sincronizaci�n de cookies
	 * 
	 * @param mSharedPreferences SharedPreferences
	 */
	public static void syncCookies(SharedPreferences mSharedPreferences)
	{
		
		List<Cookie> cookies = myCookieStore.getCookies();
		if (cookies.isEmpty()) {

		} else {
			for (int i = 0; i < cookies.size(); i++) {
				String jsession = null;
				if (cookies.get(i).getName().equals("JSESSIONID")) {
					jsession = cookies.get(i).getValue();
					// GUARDAR EL JSESSIONIDACTUAL
					if(jsession !=null)
					{
						Editor e2 = mSharedPreferences.edit();
						e2.putString(Constants.PREF_KEY_RTVE_LOGIN_SESSION, jsession);
						e2.commit(); // save changes
					}
				}
				cookieManager.setCookie(cookies.get(i).getDomain(),
						cookies.get(i).getName() + "="
								+ cookies.get(i).getValue());
				
			}
		}
		syncManager.sync();
		

	}
}
