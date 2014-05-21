package com.speakinbytes.login.fragments;

import com.speakinbytes.login.social.FacebookManager;
import com.speakinbytes.login.social.GPManager;
import com.speakinbytes.login.social.TwitterManager;
import com.speakinbytes.login.utils.Constants;

import com.speakinbytes.login.utils.TextListener;
import com.speakinbytes.login.views.FacebookBtn;
import com.speakinbytes.login.views.GPBtn;
import com.speakinbytes.login.views.TwitterBtn;
import com.speakinbytes.login.R;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends DialogFragment implements TextListener {

	private static final String FRAGMENT_NAME = "LOGIN";

	private final int LOGIN = 0;
	String FILENAME = "AndroidSSO_data";
	// private SharedPreferences mPrefs;
	private TwitterBtn btnTwitterLogin;
	private FacebookBtn btnloginFacebook;
	private GPBtn btnLogGp;
	private TextView twitterText;
	private TextView facebookText;
	private TextView gPlusText;

	private static final String TAG = FRAGMENT_NAME;
	private View aux;
	private View convertView;
	int mNum;
	private boolean bDelegado;
	
	private String consumer, secretKey, facebook;

	private boolean[] redes;
	private SharedPreferences mSharedPreferences;
	private long start;
	private String name = "";

	/**
	 * Create a new instance of MyDialogFragment, providing "num" as an
	 * argument.
	 */
	public static LoginFragment newInstance(int num, boolean delegado, boolean[] redes, String[] twKeys, String facebook) {
		LoginFragment f = new LoginFragment();
		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putInt("num", num);
		args.putBooleanArray("redes", redes);
		args.putBoolean("bDelegado", delegado);
		if(Constants.debug)
			Log.i(TAG, "is Delegado receiver"+delegado);
		if(facebook!=null)
			args.putString("facebook", facebook);
		if(twKeys!=null && twKeys.length ==2)
		{
			args.putString("consumer", twKeys[0]);
			args.putString("secretKey", twKeys[1]);
		}
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNum = getArguments().getInt("num");
		bDelegado = getArguments().getBoolean("bDelegado");
		if(Constants.debug)
			Log.i(TAG, "is Delegado recuperado"+bDelegado);
		if(getArguments().containsKey("redes"))
			redes = getArguments().getBooleanArray("redes");
		else
			redes = new boolean[]{true,true,true,true};
		
		if(getArguments().containsKey("facebook"))
			facebook = getArguments().getString("facebook");
		if(getArguments().containsKey("consumer"))
			consumer = getArguments().getString("consumer");
		if(getArguments().containsKey("secretKey"))
			secretKey = getArguments().getString("secretKey");
		// Pick a style based on the num.
		int style = DialogFragment.STYLE_NORMAL, theme = 0;
		switch ((mNum - 1) % 6) {
		case 1:
			style = DialogFragment.STYLE_NO_TITLE;
			break;
		case 2:
			style = DialogFragment.STYLE_NO_FRAME;
			break;
		case 3:
			style = DialogFragment.STYLE_NO_INPUT;
			break;
		case 4:
			style = DialogFragment.STYLE_NORMAL;
			break;
		case 5:
			style = DialogFragment.STYLE_NORMAL;
			break;
		case 6:
			style = DialogFragment.STYLE_NO_TITLE;
			break;
		case 7:
			style = DialogFragment.STYLE_NO_FRAME;
			break;
		case 8:
			style = DialogFragment.STYLE_NORMAL;
			break;
		}
		switch ((mNum - 1) % 6) {
		case 4:
			theme = android.R.style.Theme_Holo;
			break;
		case 5:
			theme = android.R.style.Theme_Holo_Light_Dialog;
			break;
		case 6:
			theme = android.R.style.Theme_Holo_Light;
			break;
		case 7:
			theme = android.R.style.Theme_Holo_Light_Panel;
			break;
		case 8:
			theme = android.R.style.Theme_Holo_Light;
			break;
		}
		setStyle(style, theme);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (!(FacebookManager.newInstance()).isFbLoggedInAlready()
				&&! (TwitterManager.newInstance()).isTwitterLoggedInAlready()
				)

		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	
		convertView = getActivity().getLayoutInflater().inflate(
				R.layout.fragment_login, null);
		mSharedPreferences = getActivity().getSharedPreferences(
				Constants.PREFERENCIAS, Context.MODE_PRIVATE);

		
		return convertView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	
		aux = convertView.findViewById(R.id.viewLogin);
		replaceView(LOGIN);


	}

	private void replaceView(int show) {

		View old = aux.findViewById(aux.getId());
		ViewGroup parent = (ViewGroup) old.getParent();
		int index = parent.indexOfChild(old);
		parent.removeView(old);

		switch (show) {
		case LOGIN:
			aux = createMainLoginView(parent);
			break;

		}
		((ViewGroup) convertView).addView(aux, index);

	}

	private View createMainLoginView(ViewGroup parent) {
		View replacement = getActivity().getLayoutInflater().inflate(
				R.layout.view_login, parent, false);
	
		if(redes == null)
		{
			bDelegado = false;
			redes = new boolean[]{true,true,true,true};
		}

		twitterText = (TextView) replacement.findViewById(R.id.textTwitter);
		facebookText = (TextView) replacement.findViewById(R.id.textFacebook);
		gPlusText = (TextView) replacement.findViewById(R.id.textGplus);
		
		btnloginFacebook = (FacebookBtn) replacement
				.findViewById(R.id.btnFacebookLogin);
		if (redes[1])
			btnloginFacebook.initData(facebook, bDelegado);
		else
			btnloginFacebook.setVisibility(View.GONE);




		btnTwitterLogin = (TwitterBtn) replacement
				.findViewById(R.id.btnTwitterLogin);
		if (redes[2]) {
			btnTwitterLogin.initData(
					consumer,
					secretKey,
					bDelegado);
		} else
			btnTwitterLogin.setVisibility(View.GONE);

		btnLogGp = (GPBtn) replacement.findViewById(R.id.btnGplusLogin);
		if (redes[3])
			btnLogGp.initData(bDelegado);
		else
			btnLogGp.setVisibility(View.GONE);

		checkLogin();
		return replacement;

	}


	public void checkLogin() {


		updateText((GPManager.newInstance()).isGPLoggedInAlready(),
				Constants.GP);
		updateText((FacebookManager.newInstance()).isFbLoggedInAlready(),
				Constants.FACEBOOK);
		updateText((TwitterManager.newInstance()).isTwitterLoggedInAlready(),
				Constants.TWITTER);

	}



	@Override
	public void updateText(final boolean success, final int source) {
		if(getActivity()!=null)
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				switch (source) {
					case Constants.FACEBOOK:
						if(!success) {
							facebookText.setText(R.string.facebook_session);
							facebookText.setTypeface(null, Typeface.NORMAL);
						}
						else {
							name = " ("+mSharedPreferences.getString(Constants.PREF_KEY_FB_NAME, "")+")";
							if(mSharedPreferences.getString(Constants.PREF_KEY_FB_NAME, "") == "") {
								facebookText.setText(getResources().getString(R.string.facebook_login));
							}
							else {
								facebookText.setText(getResources().getString(R.string.facebook_login)+name);
							}
							facebookText.setTypeface(null, Typeface.BOLD);
						}
						break;
					case Constants.TWITTER:
						if(!success) {
							twitterText.setText(R.string.twitter_session);
							twitterText.setTypeface(null, Typeface.NORMAL);
						}	
						else {
							name = " ("+mSharedPreferences.getString(Constants.PREF_KEY_TWITTER_NAME, "")+")";
							if(mSharedPreferences.getString(Constants.PREF_KEY_TWITTER_NAME, "") == "") {
								twitterText.setText(getResources().getString(R.string.twitter_login));
							}
							else {
								twitterText.setText(getResources().getString(R.string.twitter_login)+name);
							}
							twitterText.setTypeface(null, Typeface.BOLD);
						}
						break;
					case Constants.GP:
						if(!success) {
							gPlusText.setText(R.string.google_session);
							gPlusText.setTypeface(null, Typeface.NORMAL);			
						}
						else {
							name = " ("+mSharedPreferences.getString(Constants.PREF_KEY_GP_NAME, "")+")";
							if(mSharedPreferences.getString(Constants.PREF_KEY_GP_NAME, "") == "") {
								gPlusText.setText(getResources().getString(R.string.google_login));
							}
							else {	
								gPlusText.setText(getResources().getString(R.string.google_login)+name);
							}
							gPlusText.setTypeface(null, Typeface.BOLD);
							
						}
						break;

					default:
						facebookText.setText(R.string.facebook_session);
						facebookText.setTypeface(null, Typeface.NORMAL);
						twitterText.setText(R.string.twitter_session);
						twitterText.setTypeface(null, Typeface.NORMAL);
						gPlusText.setText(R.string.google_session);
						gPlusText.setTypeface(null, Typeface.NORMAL);

						break;
				}
	
			}
		});
	}

}