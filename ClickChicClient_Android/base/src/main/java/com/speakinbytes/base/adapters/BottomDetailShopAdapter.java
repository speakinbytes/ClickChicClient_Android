package com.speakinbytes.base.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.speakinbytes.base.fragments.DetailTextFragment;
import com.speakinbytes.base.fragments.ListCardFragment;

/**
 * Created by bmjuan on 16/05/14.
 */
public class BottomDetailShopAdapter extends FragmentPagerAdapter {

    private final String[] SECTIONS = { "Description", "Comments", "Terms" };


    public BottomDetailShopAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return DetailTextFragment.newInstance("ClickChic", "holadsfjkadjkflajsdkfjaklsdjfkajsdklfjaksldfjkdsajfkdsjfklsd");
    }

    @Override
    public int getCount() {
        return SECTIONS.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return SECTIONS[position];
    }

}