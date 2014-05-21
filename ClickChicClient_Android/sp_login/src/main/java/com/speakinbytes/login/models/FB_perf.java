package com.speakinbytes.login.models;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.speakinbytes.login.utils.Constants;

public class FB_perf {

	private final String TAG = getClass().getSimpleName();

	public boolean parserFBData(String str, SharedPreferences mSharedPreferences) {
		// if(Constants.debug) Log.i(FRAGMENT_NAME, "Log out fb " + response);
		String json = str;
		FBModel perfil = new FBModel();
		try {
			// Facebook Profile JSON data
			JSONObject profile = new JSONObject(json);
			// getting name of the user
			String name = profile.getString("first_name");
			perfil.setName(name);
			// getting id of the user
			String id = profile.getString("id");
			perfil.setId(id);
			String avatar = Constants.BASE_AVATAR_FB + id + Constants.QUERY_SQUARE_FB;
			perfil.setAvatar(avatar);
			Editor e = mSharedPreferences.edit();
			e.putString(Constants.PREF_KEY_FB_AVATAR, avatar);
			e.putString(Constants.PREF_KEY_FB_NAME, name);
			e.commit(); // save changes
			return true;
		} catch (JSONException e) {
			if(Constants.debug) Log.e(TAG, e.toString());
			return false;
		}
	}
}
