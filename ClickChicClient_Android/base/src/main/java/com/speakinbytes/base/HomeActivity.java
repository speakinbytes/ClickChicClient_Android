package com.speakinbytes.base;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.speakinbytes.base.NavigationDrawer.NavigationDrawerFragment;
import com.speakinbytes.base.NavigationDrawer.NavigationDrawerRightFragment;
import com.speakinbytes.base.adapters.SectionsPagerAdapter;
import com.speakinbytes.base.fragments.HomeFragment;
import com.speakinbytes.base.managers.CategoryManager;
import com.speakinbytes.base.managers.ProductsManager;
import com.speakinbytes.base.models.Category;
import com.speakinbytes.base.models.Product;

import java.util.ArrayList;

public class HomeActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, NavigationDrawerRightFragment.NavigationDrawerCallbacks{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
       /* fragments = new Vector<Fragment>();
        if (savedInstanceState != null) {
            currentPage = savedInstanceState.getInt("page");
            // create fragments to use
            beforeFr = (BeforeFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, BeforeFragment.TAG);

            afterFr = (AfterFragment) getSupportFragmentManager().getFragment(
                    savedInstanceState, AfterFragment.TAG);
            nowFr = (NowFragment) getSupportFragmentManager().getFragment(
                    savedInstanceState, NowFragment.TAG);

        }*/

    }

    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        try {
            outState.putInt("page", mViewPager.getCurrentItem());
            if (getSupportFragmentManager().findFragmentByTag(
                    BeforeFragment.TAG) == null) {
                getSupportFragmentManager().putFragment(outState,
                        BeforeFragment.TAG, mPagerAdapter.getItem(0));
                getSupportFragmentManager().putFragment(outState,
                        NowFragment.TAG, mPagerAdapter.getItem(1));
                getSupportFragmentManager().putFragment(outState,
                        AfterFragment.TAG, mPagerAdapter.getItem(2));
                if (MetodosComunes.login != null) {
                    try {
                        getSupportFragmentManager().putFragment(outState,
                                LoginFragment.FRAGMENT_NAME,
                                MetodosComunes.login);
                    } catch (Exception e) {

                    }
                }
            }
        } catch (Exception e) {
            if (Constants.debug) {
                Log.e(TAG, e.toString());
            }
        }
        super.onSaveInstanceState(outState);

    }*/

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new HomeFragment())
                .commit();
    }

    public void onSectionAttached(String title) {
        mTitle = title;
    }

    public void restoreActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(android.support.v7.app.ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


}

