package com.speakinbytes.base.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.koushikdutta.ion.NetworkImageView;

/**
 * Created by bmjuan on 16/05/14.
 */
public final class ImageSlideFragment extends Fragment {
    private String imageResourceUrl;


    public ImageSlideFragment(String url) {
        imageResourceUrl = url;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        NetworkImageView image = new NetworkImageView(getActivity());
        image.setImageUrl(imageResourceUrl);

        LinearLayout layout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams lyParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        image.setLayoutParams(lyParams);
        layout.setLayoutParams(lyParams);
        layout.setGravity(Gravity.CENTER);
        layout.addView(image);

        return layout;
    }
}
