package com.speakinbytes.login;

/**
 * Created by bmjuan on 21/05/14.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.android.volley.toolbox.NetworkImageView;
import com.speakinbytes.login.models.PerfilModel;

import java.util.ArrayList;


/***
 * ADAPTER
 */

public class SellersTwAdapter extends ArrayAdapter<PerfilModel> {

    private static final String TAG = "EtsyGridAdapter";

    static class ViewHolder {
        NetworkImageView mImg;
    }

    private final LayoutInflater mLayoutInflater;

    public SellersTwAdapter(final Context context, final int textViewResourceId) {
        super(context, textViewResourceId);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.header, parent, false);
            vh = new ViewHolder();
            vh.mImg = (NetworkImageView) convertView.findViewById(R.id.imageView);

            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }
       // vh.mImg.setImageUrl(getItem(position).getImages()[0], VolleySingleton.getInstance().getImageLoader());

        return convertView;
    }

}

