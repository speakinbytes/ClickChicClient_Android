package com.speakinbytes.login.social;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import com.speakinbytes.login.activities.TwitterWebviewActivity;
import com.speakinbytes.login.utils.Commons;
import com.speakinbytes.login.utils.Constants;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

public class TwitterManager {

	private static final String TAG = "Twitter";
	// AccessToken
	private AccessToken accessToken;
	private Context mCtx;

	// User
	private User user;
	private long userID;

	// Twitter
	private RequestToken requestToken;

	// Internet Connection detector
	private static Twitter twitter;

	// Shared Preferences
	private SharedPreferences mSharedPreferences;
	private static TwitterLoginListener mListener;
	private TwitterCommentListener mCommentListener;
	private String mSecret;
	private String mKey;
	private static TwitterManager _instance;
	

	private TwitterManager() {
	}

	public static TwitterManager newInstance() {
		if (_instance == null) {
			_instance = new TwitterManager();
		}
		return _instance;
	}

	public void initTwitter(Context context, String key, String secret,
			TwitterLoginListener listener, SharedPreferences sp) {
		mListener = listener;
		mCtx = context;
		if (twitter == null) {
		
			mSecret = secret;
			mKey = key;
			mSharedPreferences = sp;
			inicTwitter();
		}

	}

	public void initTwitter(Context context, String key, String secret,
			SharedPreferences sp) {

		mCtx = context;
		mSharedPreferences = sp;
		if (loadAccessToken() != null) {
			accessToken = loadAccessToken();
			mSecret = secret;
			mKey = key;
			inicTwitter();
		}

	}

	public void initTwitter(Context context, TwitterCommentListener listener,
			String key, String secret, SharedPreferences sp) {

		if (twitter == null) {
			mCtx = context;
			mSecret = secret;
			mKey = key;
			mCommentListener = listener;
			mSharedPreferences = sp;
			accessToken = loadAccessToken();
			inicTwitter();
		}

	}

	public void setPreferences(Context context, SharedPreferences sp) {
		mCtx = context;
		mSharedPreferences = sp;
	}

	/**
	 * Function to logout from twitter It will just clear the application shared
	 * preferences
	 * */
	public void logout() {
		// Clear the shared preferences
		logoutNativeTwitter();
		Editor e = mSharedPreferences.edit();
		e.putString(Constants.PREF_KEY_OAUTH_TOKEN, null);
		e.putLong(Constants.PREF_KEY_USER_ID, 0);
		e.putString(Constants.PREF_KEY_OAUTH_SECRET, null);
		e.putBoolean(Constants.PREF_KEY_TWITTER_LOGIN, false);
		e.remove(Constants.PREF_KEY_USER_ID);
		e.remove(Constants.PREF_KEY_URI);
		e.commit();
		if (mListener != null)
			mListener.isLoginTw(true);
		else
		{
			if(Constants.debug)
				Log.i(TAG, "Log out completo mListener a null");
		}

	}

	private void logoutNativeTwitter() {

		if (twitter != null) {
			try {
				twitter.setOAuthAccessToken(loadAccessToken());
			} catch (IllegalStateException e) {
				if(Constants.debug)
					Log.e(TAG, "Logout " + e.toString());
			}
			twitter.shutdown();
		}

	}

	public void authorize(final String oauthVerifier) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				if (twitter != null && oauthVerifier != null
						&& requestToken != null) {
					try {
						inicTwitter();
						accessToken = twitter.getOAuthAccessToken(requestToken,
								oauthVerifier);
						setPreferencesTwitter(accessToken);
						getUserTwitter();
						if(mListener!=null)
							mListener.isLoginTw(true);
					} catch (TwitterException e) {
						if(mListener!=null)
							mListener.isLoginTw(false);

					}
				} else
					if(mListener!=null)
						mListener.isLoginTw(false);

			}
		}).start();

	}

	private void inicTwitter() {
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setOAuthConsumerKey(mKey);
		configurationBuilder.setOAuthConsumerSecret(mSecret);
		configurationBuilder.setUseSSL(true);
		Configuration configuration = configurationBuilder.build();
		TwitterFactory twitterFactory = new TwitterFactory(configuration);
		twitter = twitterFactory.getInstance();
	}

	@TargetApi(Build.VERSION_CODES.CUPCAKE)
    public void login() {

		String token = mSharedPreferences.getString(
				Constants.PREF_KEY_OAUTH_TOKEN, "");

		if (token.length() == 0) {
		

			AsyncTask <Void, Void, Void> a = new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					try {

						inicTwitter();

						requestToken = twitter
								.getOAuthRequestToken(Constants.TWITTER_CALLBACK_URL);

					} catch (TwitterException e) {
						if(Constants.debug)
							Log.e(TAG, "TwitterException " + e.toString());
						
						if (mListener != null)
							mListener.isLoginTw(false);
					} catch (IllegalStateException e) {
						if(Constants.debug)
							Log.e(TAG, "Consumer already exists");
						twitter = null;
						if (mListener != null)
							mListener.isLoginTw(false);
					}
					if (requestToken != null) {
						if(Constants.debug)
							Log.i(TAG, "Activity "+mCtx.getClass().toString());
						Intent intent = new Intent(mCtx, TwitterWebviewActivity.class);
						intent.putExtra("URL", requestToken.getAuthenticationURL());
						((Activity) mCtx).startActivityForResult(intent,
								Constants.SIGNIN_TWITTER);
					}
					return null;
				}
			};
			a.execute();

		} else {
			accessToken = loadAccessToken();
			ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
			configurationBuilder.setOAuthConsumerKey(mKey);
			configurationBuilder.setOAuthConsumerSecret(mSecret);
			configurationBuilder.setUseSSL(true);
			Configuration configuration = configurationBuilder.build();
			TwitterFactory twitterFactory = new TwitterFactory(configuration);
			twitter = twitterFactory.getInstance();
			TwitterFactory factory = new TwitterFactory();
			twitter = factory.getInstance();
			twitter.setOAuthConsumer(mKey, mSecret);
			twitter.setOAuthAccessToken(accessToken);
			getUserTwitter();

			if (mListener != null)
				mListener.isLoginTw(true);

		}

	}

	public AccessToken getAccessToken() {
		return loadAccessToken();
	}

	private AccessToken loadAccessToken() {

		if (accessToken != null)
			return accessToken;
		else {
			
			String token = mSharedPreferences.getString(
					Constants.PREF_KEY_OAUTH_TOKEN, "");
			String tokenSecret = mSharedPreferences.getString(
					Constants.PREF_KEY_OAUTH_SECRET, "");
			return new AccessToken(token, tokenSecret);
		}
	}
	private void setPreferencesTwitter(AccessToken accessToken) {
		// if(Constants.debug) Log.v("Login", "Usuario autorizado twitter");
		// if(Constants.debug) Log.i(TAG, "Twitter true");
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putBoolean(Constants.PREF_KEY_TWITTER_LOGIN, true);
		// After getting access token, access token secret
		// store them in application preferences
		editor.putString(Constants.PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
		editor.putLong(Constants.PREF_KEY_USER_ID, accessToken.getUserId());
		editor.putString(Constants.PREF_KEY_OAUTH_SECRET,
				accessToken.getTokenSecret());
		// Store login status - true
		editor.putBoolean(Constants.PREF_KEY_TWITTER_LOGIN, true);
		editor.commit();
	}

	private void getUserTwitter() {
		Thread UserTwitter = new Thread(new Runnable() {
			public void run() {
				try {
					
					userID = accessToken.getUserId();
					user = twitter.showUser(userID);

					Editor e = mSharedPreferences.edit(); // Store login
					// status - true e.putBoolean(Common.PREF_KEY_TWITTER_LOGIN,
					// true);
					e.putString(Constants.PREF_KEY_TWITTER_AVATAR,
							user.getBiggerProfileImageURL());
					e.putString(Constants.PREF_KEY_TWITTER_NAME, user.getName());
					e.commit(); // save changes

				} catch (TwitterException e) {
					if(Constants.debug)
						Log.e("Twitter Login Error", "> " + e.getMessage());

				}
				catch(Exception e)
				{
					if(Constants.debug)
						Log.e("Twitter Login Error", "> " + e.toString());
				}
			}
		});
		UserTwitter.start();
	}

	@TargetApi(Build.VERSION_CODES.CUPCAKE)
    public void update(String comment, TwitterCommentListener listener) {
		mCommentListener = listener;
		new UpdateTwitterStatus(mCtx).execute(comment);
	}

	/**
	 * Function to update status
	 * */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    class UpdateTwitterStatus extends AsyncTask<String, String, String> {

		private ProgressDialog pDialog;

		public UpdateTwitterStatus(Context context) {
			mCtx = context;
		}

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(mCtx);
			pDialog.setMessage("Enviando comentario...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * getting Places JSON
		 * */
		protected String doInBackground(String... args) {

			try {
				// Access Token
				String access_token = mSharedPreferences.getString(
						Constants.PREF_KEY_OAUTH_TOKEN, "");
				// Access Token Secret
				String access_token_secret = mSharedPreferences.getString(
						Constants.PREF_KEY_OAUTH_SECRET, "");
				// String status = args[0];
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthAccessToken(access_token);
				builder.setOAuthAccessTokenSecret(access_token_secret);
				builder.setOAuthConsumerKey(mKey);
				builder.setOAuthConsumerSecret(mSecret);
				OAuthAuthorization auth = new OAuthAuthorization(
						builder.build());
				twitter = new TwitterFactory().getInstance(auth);
				twitter4j.Status response = twitter.updateStatus(args[0]);
				return response.getText();
			} catch (TwitterException e) {
				// Error in updating status
				if(Constants.debug)
				{
					e.printStackTrace();
					Log.d("Twitter Update Error", e.getMessage());
				}

			}

			return null;

		}

		/**
		 * After completing background task Dismiss the progress dialog and show
		 * the data in UI Always use runOnUiThread(new Runnable()) to update UI
		 * from background thread, otherwise you will get error
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			mCommentListener.sendComment((file_url != null) ? true : false);

		}

	}

	class Twitter_AlertDialogManager {
		/**
		 * Function to display simple Alert Dialog
		 * 
		 * @param context
		 *            - application context
		 * @param title
		 *            - alert dialog title
		 * @param message
		 *            - alert message
         *
		 *            - success/failure (used to set icon) - pass null if you
		 *            don't want icon
		 * */
		public void showAlertDialog(Context context, String title,
				String message) {
			AlertDialog alertDialog = new AlertDialog.Builder(context).create();

			// Setting Dialog Title
			alertDialog.setTitle(title);

			// Setting Dialog Message
			alertDialog.setMessage(message);

			// Showing Alert Message
			alertDialog.show();
		}
	}

	/**
	 * Check user already logged in your application using twitter Login flag is
	 * fetched from Shared Preferences
	 * */
	public boolean isTwitterLoggedInAlready() {
		if (twitter == null)
			return false;
		// return twitter login status from Shared Preferences
		Boolean isLogged = false;
		try {
			isLogged = mSharedPreferences.getBoolean(
					Constants.PREF_KEY_TWITTER_LOGIN, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isLogged;
	}

	public interface TwitterLoginListener {
		public void isLoginTw(boolean success);
	}

	public interface TwitterCommentListener {
		public void sendComment(boolean success);
	}

}
