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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends DialogFragment {

	private static final String FRAGMENT_NAME = "LOGIN";

	private final int LOGIN = 0;
    private final int FORM = 1;
	String FILENAME = "AndroidSSO_data";
	// private SharedPreferences mPrefs;
	private TwitterBtn btnTwitterLogin;
	private FacebookBtn btnloginFacebook;
	private GPBtn btnLogGp;

	private static final String TAG = FRAGMENT_NAME;
	private View aux;
	private View convertView;
	int mNum;
	private boolean bDelegado;
	
	private String consumer, secretKey, facebook;

    private Button btnRegister;
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
            case FORM:
                aux = createForm(parent);
                break;

        }
        ((ViewGroup) convertView).addView(aux, index);

    }



    private View createForm(ViewGroup parent) {
        View replacement = getActivity().getLayoutInflater().inflate(
                R.layout.view_login_twitter, parent, false);

        return replacement;

    }


	private View createMainLoginView(ViewGroup parent) {

        View replacement = getActivity().getLayoutInflater().inflate(
                R.layout.view_login, parent, false);

        btnRegister = (Button) replacement
                .findViewById(R.id.register);
        btnRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceView(FORM);
            }
        });
		if(redes == null)
		{
			bDelegado = false;
			redes = new boolean[]{true,true,true,true};
		}


		
		btnloginFacebook = (FacebookBtn) convertView
				.findViewById(R.id.btnFacebookLogin);
		if (redes[1])
			btnloginFacebook.initData(facebook, bDelegado);
		else
			btnloginFacebook.setVisibility(View.GONE);




		btnTwitterLogin = (TwitterBtn) convertView
				.findViewById(R.id.btnTwitterLogin);
		if (redes[2]) {
			btnTwitterLogin.initData(
					consumer,
					secretKey,
					bDelegado);
		} else
			btnTwitterLogin.setVisibility(View.GONE);

		btnLogGp = (GPBtn) convertView.findViewById(R.id.btnGplusLogin);
		if (redes[3])
			btnLogGp.initData(bDelegado);
		else
			btnLogGp.setVisibility(View.GONE);

        return replacement;
	}

}