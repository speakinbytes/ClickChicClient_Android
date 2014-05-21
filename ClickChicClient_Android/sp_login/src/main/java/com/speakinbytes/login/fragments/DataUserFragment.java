package com.speakinbytes.login.fragments;


import com.speakinbytes.login.utils.Constants;
import com.speakinbytes.login.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;


public class DataUserFragment extends DialogFragment implements OnClickListener{
	
	public static final String FRAGMENT_NAME = "DATAUSER";

	String FILENAME = "AndroidSSO_data";
	private static final String TAG = FRAGMENT_NAME;
	private View convertView;
	private ImageButton mClose;
	private String mInfoExtra;
	int mNum;
	private SharedPreferences mSharedPreferences;

	/**
	 * Create a new instance of MyDialogFragment, providing "num" as an
	 * argument.
	 */
	public static DataUserFragment newInstance(String extra) {
		DataUserFragment f = new DataUserFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		if(extra!=null)
			args.putString("extra", extra);

		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		
		if(getArguments().containsKey("extra"))
			mInfoExtra = getArguments().getString("extra");
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
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		// ShareSocialMedia.setData(getActivity(),null);
		convertView = getActivity().getLayoutInflater().inflate(
				R.layout.fragment_data, null);

		mClose = (ImageButton)convertView.findViewById(R.id.bClose);
		mClose.setOnClickListener(this);
		mSharedPreferences = getActivity().getSharedPreferences(
				Constants.PREFERENCIAS, Context.MODE_PRIVATE);

		return convertView;
	}


	@Override
	public void onClick(View v) {
		dismissAllowingStateLoss();
	}


}
