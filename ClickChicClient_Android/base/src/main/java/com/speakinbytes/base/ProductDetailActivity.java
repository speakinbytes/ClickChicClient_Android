package com.speakinbytes.base;

import android.app.ActionBar;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.speakinbytes.base.fragments.ProductFragment;
import com.speakinbytes.base.models.Product;
import com.speakinbytes.base.utils.Constants;

public class ProductDetailActivity extends ActionBarActivity{

    private ShareActionProvider mShareActionProvider;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private Product p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        p = (Product) getIntent().getSerializableExtra(Constants.ARGS_PRODUCT);
        Log.i("OnCreate", p.getImages()[0]);
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, ProductFragment.newInstance(p))
                .commit();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

            getMenuInflater().inflate(R.menu.product, menu);
            // Get the menu item.
            MenuItem menuItem = menu.findItem(R.id.menu_item_share);
            // Get the provider and hold onto it to set/change the share intent.
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
            mShareActionProvider = (ShareActionProvider) MenuItemCompat
                    .getActionProvider(menuItem);
            if(mShareActionProvider!=null) {
                mShareActionProvider.setShareIntent(getDefaultIntent());
            }
            restoreActionBar();
            return true;
    }


    /**
     * Defines a default (dummy) share intent to initialize the action provider.
     * However, as soon as the actual content to be used in the intent is known
     * or changes, you must update the share intent by again calling
     * mShareActionProvider.setShareIntent()
     */
    private Intent getDefaultIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        return intent;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_item_share) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
