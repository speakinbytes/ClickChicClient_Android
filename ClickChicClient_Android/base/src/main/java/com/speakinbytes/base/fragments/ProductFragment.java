package com.speakinbytes.base.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.speakinbytes.base.R;
import com.speakinbytes.base.adapters.BottomDetailShopAdapter;
import com.speakinbytes.base.adapters.ImageSlideFragmentAdapter;
import com.speakinbytes.base.adapters.SectionsPagerAdapter;
import com.speakinbytes.base.models.Product;
import com.speakinbytes.base.utils.Constants;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

/**
 * Created by bmjuan on 16/05/14.
 */
public class ProductFragment extends Fragment {

    ImageSlideFragmentAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;
    Product p;
    /**
     * Create a new instance of DetailsFragment, initialized to
     * show the text at 'index'.
     */
    public static ProductFragment newInstance(Product product) {
        ProductFragment f = new ProductFragment();
        Bundle b = new Bundle();
        b.putSerializable(Constants.ARGS_PRODUCT, product);
        f.setArguments(b);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
        p = (Product) getArguments().getSerializable(Constants.ARGS_PRODUCT);
        View rootView = inflater.inflate(R.layout.fragment_product, container, false);
        if(p!=null)
        {
            fillPagerImages(rootView);
            fillShopDetail(rootView);
            fillDetailsViews(rootView);
        }
        else
            Toast.makeText(getActivity(), "Producto nulll", Toast.LENGTH_LONG).show();
        return rootView;
    }

    private void fillShopDetail(View rootView) {
        TextView model = (TextView) rootView.findViewById(R.id.txtModel);
        model.setText(p.getModel());
        Button btn = (Button) rootView.findViewById(R.id.buyButton);
        btn.setText("BUY IT NOW - 10.99 EUR");
    }

    private void fillPagerImages(View view)
    {

        mAdapter = new ImageSlideFragmentAdapter(getActivity()
                .getSupportFragmentManager(), p.getImages());

        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        ((CirclePageIndicator) mIndicator).setSnap(true);

        mIndicator
                .setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        Toast.makeText(ProductFragment.this.getActivity(),
                                "Changed to page " + position,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPageScrolled(int position,
                                               float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });

    }

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v13.app.FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    BottomDetailShopAdapter mSectionsPagerAdapter;
    private final Handler handler = new Handler();

    private PagerSlidingTabStrip tabs;


    /**
     * The {@link android.support.v4.view.ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private void fillDetailsViews(View rootView) {

        mSectionsPagerAdapter = new BottomDetailShopAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) rootView.findViewById(R.id.pagerDetails);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Bind the tabs to the ViewPager

        tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);


    }




}

