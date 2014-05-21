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

public class EtsyAsymetricAdapter extends ArrayAdapter<Product> {

    private static final String TAG = "EtsyAsymetricAdapter";

    static class ViewHolder {
        DynamicHeightTextView txtLineOne;
        TextView txtModel;
        NetworkImageView mImg;
    }

    private final LayoutInflater mLayoutInflater;
    private final Random mRandom;
    private final ArrayList<Integer> mBackgroundColors;


    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public EtsyAsymetricAdapter(final Context context, final int textViewResourceId) {
        super(context, textViewResourceId);
        mLayoutInflater = LayoutInflater.from(context);
        mRandom = new Random();
        mBackgroundColors = new ArrayList<Integer>();
        mBackgroundColors.add(R.color.orange);
        mBackgroundColors.add(R.color.green);
        mBackgroundColors.add(R.color.blue);
        mBackgroundColors.add(R.color.yellow);
        mBackgroundColors.add(R.color.grey);
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

        double positionHeight = getPositionRatio(position);
        int backgroundIndex = position >= mBackgroundColors.size() ?
                position % mBackgroundColors.size() : position;

        convertView.setBackgroundResource(mBackgroundColors.get(backgroundIndex));

        Log.d(TAG, "getView position:" + position + " h:" + positionHeight);

        vh.txtLineOne.setHeightRatio(positionHeight);
        vh.txtLineOne.setText(getItem(position).getSeller_id());
        vh.txtModel.setText(getItem(position).getModel());
        vh.mImg.setImageUrl(getItem(position).getImages()[0], VolleySingleton.getInstance().getImageLoader());

        return convertView;
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
            Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
    }
}
