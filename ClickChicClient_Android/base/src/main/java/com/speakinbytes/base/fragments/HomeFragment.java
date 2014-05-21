package com.speakinbytes.base.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.speakinbytes.base.R;
import com.speakinbytes.base.adapters.SectionsPagerAdapter;
import com.speakinbytes.base.managers.CategoryManager;
import com.speakinbytes.base.managers.ProductsManager;

/**
 * Created by bmjuan on 26/04/14.
 */
public class HomeFragment extends Fragment{
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v13.app.FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    private final Handler handler = new Handler();

    private PagerSlidingTabStrip tabs;


    /**
     * The {@link android.support.v4.view.ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_pager, null, false);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Bind the tabs to the ViewPager

        tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);
        // continued from above

        /*// When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(this);*/
        //changeColor(currentColor);
        return rootView;

    }

}
