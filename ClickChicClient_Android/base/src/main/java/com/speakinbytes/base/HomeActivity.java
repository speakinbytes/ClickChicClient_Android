package com.speakinbytes.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.speakinbytes.base.navigation.NavigationDrawerFragment;
import com.speakinbytes.base.navigation.NavigationDrawerRightFragment;
import com.speakinbytes.base.fragments.HomeFragment;
import com.speakinbytes.base.fragments.ShopFragment;
import com.speakinbytes.base.models.Shop;
import com.speakinbytes.login.fragments.LoginFragment;
import com.speakinbytes.login.social.FacebookManager;
import com.speakinbytes.login.social.GPManager;
import com.speakinbytes.login.social.TwitterManager;

public class HomeActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, NavigationDrawerRightFragment.NavigationDrawerCallbacks{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private SharedPreferences mSharedPreferences;
    private boolean[] redes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        redes = new boolean[]{true,true,true,true};
        mSharedPreferences = getSharedPreferences(com.speakinbytes.login.utils.Constants.PREFERENCIAS,
                Context.MODE_PRIVATE);
        (GPManager.newInstance()).initInstance(this,null,mSharedPreferences);
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
    public void onStart() {
        super.onStart();
        if((GPManager.newInstance()).isGPLoggedInAlready())
        {
            (GPManager.newInstance()).refreshToken();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if((GPManager.newInstance()).isGPLoggedInAlready())
            (GPManager.newInstance()).disconnect();
    }

    private void initData() {
        (TwitterManager.newInstance()).initTwitter(this,
                getString(R.string.consumer_key),
                getString(R.string.consumer_secret), mSharedPreferences);
        (FacebookManager.newInstance()).initFb(this, mSharedPreferences);

        (GPManager.newInstance()).initInstance(this, null, mSharedPreferences);

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment fragment =null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        Log.i("NAV", "Position " + position);
        if(position == 0)
        {
            fragment = showDialog(true);
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }
        if(position == 1)
        {
            fragment = new HomeFragment();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }
        else if(position == 2)
        {
            fragment = ShopFragment.newInstance(new Shop());
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == com.speakinbytes.login.utils.Constants.SIGNIN_TWITTER) {
            if (resultCode == Activity.RESULT_OK) {

                String oauthVerifier = (String) data.getExtras().get(
                        "oauth_verifier");
                (TwitterManager.newInstance()).authorize(oauthVerifier);
            }
        }

        else if (requestCode == com.speakinbytes.login.utils.Constants.REQUEST_CODE_SIGN_IN
                || requestCode == com.speakinbytes.login.utils.Constants.REQUEST_CODE_RESOLVE_ERR) {
            if (resultCode == RESULT_OK) {
                (GPManager.newInstance()).authorize(requestCode, resultCode);
            }
        } else {
            (FacebookManager.newInstance()).authorize(requestCode, resultCode,
                    data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Fragment showDialog(boolean bFullScreen) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction. We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        int style = 2;
        if(bFullScreen)
            style = 5;
        // Create and show the dialog.
        DialogFragment newFragment = LoginFragment.newInstance(style, true, redes, new String[]{getString(R.string.consumer_key),
                getString(R.string.consumer_secret)}, getString(R.string.app_facebook_id));

        return newFragment;
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

    void showComentarDialog(boolean bFullScreen) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction. We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(
                "comentar");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        int style = 2;
        if(bFullScreen)
            style = 5;
		/*DialogFragment newFragment = NativeCommentFragment.newInstance(style,
				"570", null, new String[] { "#abcd" },
				null, redes);*/
      /*  DialogFragment newFragment = CommentFragment.newInstance(style, "MOMMENT",
                "570", null, getString(R.string.app_facebook_id),
                getString(R.string.consumer_key),
                getString(R.string.consumer_secret), new String[] { "#abcd" },
                null, false, redes);
        newFragment.show(ft, "comentar");*/
    }

}

