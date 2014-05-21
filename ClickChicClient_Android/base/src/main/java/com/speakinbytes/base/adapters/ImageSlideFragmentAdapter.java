package com.speakinbytes.base.adapters;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.speakinbytes.base.fragments.ImageSlideFragment;

/**
 * Created by bmjuan on 16/05/14.
 */
public class ImageSlideFragmentAdapter extends FragmentPagerAdapter {

    private String[] IMAGES;


    public ImageSlideFragmentAdapter(FragmentManager fm, String[] images) {
        super(fm);
        IMAGES = images;
    }

    @Override
    public Fragment getItem(int i) {
        return new ImageSlideFragment(IMAGES[i]);
    }


    @Override
    public int getCount() {
        return IMAGES.length;
    }

}
