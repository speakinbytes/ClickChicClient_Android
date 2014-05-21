package com.speakinbytes.login.fragments;

import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.android.gms.plus.PlusShare;
import com.speakinbytes.login.utils.Constants;
import com.speakinbytes.login.R;


@SuppressLint("NewApi")
public class NativeCommentFragment extends DialogFragment implements
		OnClickListener {

	public static final String FRAGMENT_NAME = "COMENTAR_NATIVO";

	String FILENAME = "AndroidSSO_data";
	private static final String TAG = FRAGMENT_NAME;
	private View convertView;
	private ImageButton mBtTw, mBtFb, mBtGp, mBtMail;
	private String mUrl, mCtvid;

	private String mHashtags;
	private String mInfoExtra;
	int mNum;
	private boolean[] redes;

	/**
	 * Create a new instance of MyDialogFragment, providing "num" as an
	 * argument.
	 */
	public static NativeCommentFragment newInstance(int num, String ctvid, String url, String[] hashtags, String extra,
			boolean[] redes) {
		NativeCommentFragment f = new NativeCommentFragment();
		// Supply num input as an argument.
	
		Bundle args = new Bundle();
		args.putInt("num", num);
		args.putString("ctvid", ctvid);
		args.putBooleanArray("redes", redes);
		if (hashtags != null)
			args.putStringArray("hashtags", hashtags);
		if (extra != null)
			args.putString("extra", extra);
		if (url != null)
			args.putString("url", url);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNum = getArguments().getInt("num");
		mCtvid = getArguments().getString("ctvid");
	
		redes = getArguments().getBooleanArray("redes");
		if (getArguments().containsKey("url"))
			mUrl = getArguments().getString("url");
		if (getArguments().containsKey("hashtags")) {
			String[] hashtags = getArguments().getStringArray("hashtags");
			StringBuilder builder = new StringBuilder();
			for (String s : hashtags) {
				builder.append(s);
			}
			mHashtags = builder.toString();

		}
		if (getArguments().containsKey("extra"))
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
				R.layout.native_comment_fragment, null);

		findViews();
		return convertView;
	}

	private void findViews() {
		
		mBtFb = (ImageButton) convertView.findViewById(R.id.btfb);
		if(redes[1])
			mBtFb.setOnClickListener(this);
		else
			mBtFb.setVisibility(View.GONE);
		mBtTw = (ImageButton) convertView.findViewById(R.id.bttwitter);
		if(!redes[2])
			mBtTw.setVisibility(View.GONE);
		else
			mBtTw.setOnClickListener(this);
		mBtMail = (ImageButton) convertView.findViewById(R.id.btn_mail);
		if(!redes[0])
			mBtMail.setVisibility(View.GONE);
		else
			mBtMail.setOnClickListener(this);
		mBtGp = (ImageButton) convertView.findViewById(R.id.btgp);
		if(!redes[3])
			mBtGp.setVisibility(View.GONE);
		else
			mBtGp.setOnClickListener(this);

		convertView.findViewById(R.id.btwu).setOnClickListener(this);

	}


	@Override
	public void onClick(View v) {
		String text = null;
		if (mHashtags != null && !mHashtags.isEmpty())
			text = mHashtags;
		if(mUrl!=null)
			text = text+ " "+ mUrl;
		if (mInfoExtra != null && !mInfoExtra.isEmpty())
			text = mInfoExtra +" "+ text;
		if (v.getId() == R.id.bttwitter) {

			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					(String) v.getTag(R.string.app_name));
			shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
					(String) v.getTag(R.drawable.ic_launcher));
			boolean resolved = false;
			PackageManager pm = v.getContext().getPackageManager();
			List<ResolveInfo> activityList = pm.queryIntentActivities(
					shareIntent, 0);
			for (final ResolveInfo app : activityList) {
				if (Constants.debug)
					Log.v(TAG, " " + app.activityInfo.name);
				if ((app.activityInfo.name).contains(".twitter")) {
					resolved = true;
					final ActivityInfo activity = app.activityInfo;
					final ComponentName name = new ComponentName(
							activity.applicationInfo.packageName, activity.name);
					
					shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
					shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					if(text!=null)
						shareIntent.putExtra(Intent.EXTRA_TEXT, text);
					if(mUrl!=null)
						shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(mUrl));
					shareIntent.setComponent(name);
					v.getContext().startActivity(shareIntent);
					break;
				}
			}
			startActivity(resolved ? shareIntent : Intent.createChooser(
					shareIntent, getString(R.string.share_with)));

		} else if (v.getId() == R.id.btfb) {

			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			PackageManager pm = v.getContext().getPackageManager();
			List<ResolveInfo> activityList = pm.queryIntentActivities(
					shareIntent, 0);
			for (final ResolveInfo app : activityList) {
				if ((app.activityInfo.name).contains("facebook")) {
					final ActivityInfo activity = app.activityInfo;
					final ComponentName name = new ComponentName(
							activity.applicationInfo.packageName, activity.name);
					shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
					shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					if(text!=null)
					{
						if(Constants.debug)
							Log.i("COM", text);
						shareIntent.putExtra(Intent.EXTRA_TEXT, text);
					}
					if(mUrl!=null)
						shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(mUrl));
					shareIntent.setComponent(name);
					v.getContext().startActivity(shareIntent);
					break;
				}
			}

		} else if (v.getId() == R.id.btgp) {

			// Launch the Google+ share dialog with attribution to your app.
			PlusShare.Builder builder = new PlusShare.Builder(getActivity())
					.setType("text/plain");
			Intent shareIntent = builder.getIntent();
			if(text!=null)
				shareIntent.putExtra(Intent.EXTRA_TEXT, text);
			if(mUrl!=null)
				shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(mUrl));
			if (shareIntent != null) {
				v.getContext().startActivity(Intent.createChooser(shareIntent, getString(R.string.share_with)));
			} 
			else
				Toast.makeText(getActivity(), "No tiene instalada la aplicaci�n", Toast.LENGTH_SHORT).show();
		} else if (v.getId() == R.id.btwu) {

			Intent waIntent = new Intent(Intent.ACTION_SEND);
			waIntent.setType("text/plain");
			if(text!=null)
				waIntent.putExtra(Intent.EXTRA_TEXT, mInfoExtra);
			if(mUrl!=null)
				waIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(mUrl));
			waIntent.setPackage("com.whatsapp");
			if (waIntent != null) {
				waIntent.putExtra(Intent.EXTRA_TEXT, mInfoExtra);//
				v.getContext().startActivity(Intent.createChooser(waIntent, getString(R.string.share_with)));
			} 

		} else {

			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			emailIntent.setType("text/plain");

			emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			if(text!=null)
				emailIntent.putExtra(Intent.EXTRA_TEXT, text);
			if(mUrl!=null)
				emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(mUrl));
			v.getContext().startActivity(Intent.createChooser(emailIntent, getString(R.string.share_with)));
			
			/*File root = Environment.getExternalStorageDirectory();
			String pathToMyAttachedFile="temp/attachement.xml";
			File file = new File(root, pathToMyAttachedFile);
			if (!file.exists() || !file.canRead()) {
			    return;
			}
			Uri uri = Uri.fromFile(file);*/

		}

	}

}
