package com.speakinbytes.base.fragments;



import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.speakinbytes.base.HomeActivity;
import com.speakinbytes.base.R;
import com.speakinbytes.base.adapters.SectionsPagerAdapter;
import com.speakinbytes.base.adapters.ShopPagerAdapter;
import com.speakinbytes.base.models.Shop;
import com.speakinbytes.base.utils.Constants;

import java.util.ArrayList;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link ShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ShopFragment extends Fragment {


    private Shop mShop;
    private ShopPagerAdapter mShopPagerAdapter;
    private PagerSlidingTabStrip tabs;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param data ArrayList datos serializables.
     * @return A new instance of fragment ShopFragment.
     */
    /**
     * The {@link android.support.v4.view.ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    public static ShopFragment newInstance(Shop data) {
        ShopFragment fragment = new ShopFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.ARGS_SHOP, data);
        fragment.setArguments(args);
        return fragment;
    }
    public ShopFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mShop = (Shop) getArguments().getSerializable(Constants.ARGS_SHOP);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_shop, container, false);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mShopPagerAdapter = new ShopPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mViewPager.setAdapter(mShopPagerAdapter);

        // Bind the tabs to the ViewPager

        tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((HomeActivity) activity).onSectionAttached("SHOP");
    }

}
