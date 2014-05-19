package com.speakinbytes.base.adapters;

/**
 * Created by bmjuan on 17/05/14.
 */
import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.etsy.android.grid.util.DynamicHeightTextView;
import com.speakinbytes.base.R;
import com.speakinbytes.base.models.Product;
import com.speakinbytes.base.utils.VolleySingleton;

import org.w3c.dom.Text;

/***
 * ADAPTER
 */

public class EtsyGridAdapter extends ArrayAdapter<Product> {

    private static final String TAG = "EtsyGridAdapter";

    static class ViewHolder {
        DynamicHeightTextView txtLineOne;
        TextView txtModel;
        NetworkImageView mImg;
    }

    private final LayoutInflater mLayoutInflater;

    public EtsyGridAdapter(final Context context, final int textViewResourceId) {
        super(context, textViewResourceId);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.custom_small_card, parent, false);
            vh = new ViewHolder();
            vh.txtLineOne = (DynamicHeightTextView) convertView.findViewById(R.id.txtSeller);
            vh.txtModel = (TextView)convertView.findViewById(R.id.txtModel);
            vh.mImg = (NetworkImageView) convertView.findViewById(R.id.imgMain);

            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.txtModel.setText(getItem(position).getModel());
        vh.mImg.setImageUrl(getItem(position).getImages()[0], VolleySingleton.getInstance().getImageLoader());

        return convertView;
    }

}
