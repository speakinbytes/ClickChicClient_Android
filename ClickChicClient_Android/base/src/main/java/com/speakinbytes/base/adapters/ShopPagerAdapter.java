package com.speakinbytes.base.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.speakinbytes.base.fragments.BlankFragment;
import com.speakinbytes.base.fragments.DetailTextFragment;
import com.speakinbytes.base.fragments.FindMeFragment;
import com.speakinbytes.base.fragments.ListCardFragment;
import com.speakinbytes.base.fragments.PromotedFragment;

/**
 * Created by bmjuan on 19/05/14.
 */
public class ShopPagerAdapter extends FragmentStatePagerAdapter {

    private final String[] SECTIONS = { "Products", "Fans", "Find me" };

    public ShopPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1: // Fragment # 0 - This will show FirstFragment
                return DetailTextFragment.newInstance("ClickChic", "holadsfjkadjkflajsdkfjaklsdjfkajsdklfjaksldfjkdsajfkdsjfklsd");
            case 0: // Fragment # 0 - This will show FirstFragment different title
                return BlankFragment.newInstance(1);
            case 2: // Fragment # 1 - This will show SecondFragment
                return FindMeFragment.newInstance();
            default:
                return BlankFragment.newInstance(1);
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