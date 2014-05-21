package com.speakinbytes.login.utils;

public class Constants {

	public static final boolean debug = true;
	/**
	 * Indices detecci�n red social
	 */
	public static final int RTVE = 0;
	public static final int FACEBOOK = 1;
	public static final int TWITTER = 2;
	public static final int GP = 3;
	public static final int FBPROFILE = 4;

	public interface REDSOCIAL {
		public static final String RTVE = "RTVE";
		public static final String FACEBOOK = "facebook";
		public static final String TWITTER = "twitter";
		public static final String GOOGLE = "google";
	}

	/**
	 * Preferences
	 */
	public static final String PREFERENCE_NAME = "twitter_oauth";
	public static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	public static final String PREF_KEY_USER_ID = "user_id";
	public static final String PREF_KEY_URI = "uri";
	public static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	public static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
	public static final String PREF_KEY_TWITTER_AVATAR = "TwitterAvatar";
	public static final String PREF_KEY_TWITTER_NAME = "TwitterName";
	public static final String PREF_USER_TWITTER = "userMail";
	public static final String PREF_KEY_RTVE_LOGIN = "isRTVELogedIn";
	public static final String PREF_KEY_RTVE_LOGIN_SESSION = "RTVELoginSession";
	public static final String PREF_KEY_FB_LOGIN = "isFBLogedIn";
	public static final String PREF_KEY_GP_LOGIN = "isGPLogedIn";
	public static final String PREF_KEY_FB_NAME = "FbName";
	public static final String PREF_KEY_FB_AVATAR = "FbAvatar";
	public static final String PREF_KEY_RTVE_NAME = "rtveName";
	public static final String PREF_KEY_RTVE_AVATAR = "rtveAvatar";
	public static final String PREF_KEY_GP_NAME = "gpName";
	public static final String PREF_KEY_GP_AVATAR = "gpAvatar";
	
	/**
	 * OnActivityResult
	 */
	public static final int SIGNIN_TWITTER = 1;
	public static final int SIGNIN_FB = 2;
	public static final int SIGNIN_RTVE = 3;
	
	/**
	 * URLS
	 */
	public static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";

	/**
	 *  Twitter oauth urls
	 */
	public static final String URL_TWITTER_AUTH = "auth_url";
	public static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	public static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

	
	/**
	 * Almac�n de preferencias m�dulo login
	 */
	public static final String PREFERENCIAS = "LOGIN_PREF";
	
	/**
	 * URLS de servicios
	 */
	public static final String HTTPS = "https";
	public static final String BASE_SECURE= "secure.rtve.es";
	//public static final String BASE_SECURE = "secure.pre.rtve.es";
	//public static final String BASE_SECURE = "secure.local.es";
	public static final String BASE_RTVE= "www.rtve.es";
	//public static final String BASE_RTVE = "prewww.rtve.es";
	public static final String PARAM_SESION="?JSESSIONID=";
	public static final String PATH_ACCESS_USERS="/usuarios/validate/accessUser";
	public static final String PATH_SECURE_CHECK = "/usuarios/j_spring_security_check";
	public static final String PATH_LOGIN = "/usuarios/secure/login";
	public static final String PATH_FB = "/usuarios/social/facebookApp";
	public static final String PATH_GP = "/usuarios/social/googleApp";
	public static final String PATH_TWITTER = "/usuarios/social/twitterApp";
	public static final String HTTP = "http";

	public static final String PATH_USUARIOS = "/usuarios/";
	
	public static final String BASE_AVATAR_FB = "http://graph.facebook.com/";
	public static final String QUERY_SQUARE_FB = "/picture?type=square";
	

	public static final String PATH_LOGOUT = "/usuarios/secure/logout";
	public static final String PATH_COM_FB = "social/facebook/post/";
	public static final String PATH_LIKE = "/usuarios/addilikeit";
	public static final String PATH_FAV = "/usuarios/addToFavorite";
	public static final String PATH_ADDCOMMENT = "/usuarios/addComment";
	public static final String PATH_SHARE = "/usuarios/social/share";
	
	public static final String PATH_MISHARE="myshar";
	public static final String PATH_MILIKE="mylike";
	public static final String PATH_MIPERFIL = "myperf";
	public static final String PATH_MIFAVO="myfavo";
	public static final String PATH_MILIST="mylist";
	public static final String PATH_MIHIST = "myhist";
	public static final String PATH_MICOMMENT = "mycomme";
	public static final String PATH_MIMOME="mymome";
	
	public static final String PATH_A_MILIST="addToPlayList";
	public static final String PATH_A_MYHISTORICO="addToPlayList";
	public static final String PATH_A_MIFAVORITOS="addToFavorite";
	
	public static final String PATH_B_MYLIST="perfil/deleteItem";
	public static final String PATH_B_FAVORITO="perfil/deleteFavoritesListItem2?itemToDelete=";
	
	/**
	 * Variables de Google plus
	 */
	public static final int DIALOG_GET_GOOGLE_PLAY_SERVICES = 1;
	
	public static final int REQUEST_CODE_SIGN_IN = 1;
	public static final int REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES = 2;
	public static final int REQUEST_AUTHORIZATION = 3;

	public static final int REQUEST_CODE_RESOLVE_ERR = 9000;
	public static final int RESULT_OK = -1;
	
	//Playlist
	/*public static final String PATH_PLAYLIST="perfil/mi-lista-json";
	public static final String PATH_HISTORICO="pefil/historico-json";
	public static final String PATH_FAVORITOS="favoritos_ajax_movil";*/
	
	
}
