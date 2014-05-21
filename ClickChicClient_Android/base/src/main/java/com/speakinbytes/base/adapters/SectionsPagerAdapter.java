package com.speakinbytes.base.adapters;

/**
 * Created by bmjuan on 26/04/14.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.speakinbytes.base.fragments.BlankFragment;
import com.speakinbytes.base.fragments.ListCardFragment;
import com.speakinbytes.base.fragments.PromotedFragment;
import com.speakinbytes.base.fragments.SimpleListProductsFragment;


/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    private final String[] SECTIONS = { "Populares", "Últimos", "Cercanos" };

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return SimpleListProductsFragment.newInstance();
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return BlankFragment.newInstance(0);
            case 2: // Fragment # 1 - This will show SecondFragment
                return PromotedFragment.newInstance(5);
            default:
                return BlankFragment.newInstance(0);
        }
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

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
