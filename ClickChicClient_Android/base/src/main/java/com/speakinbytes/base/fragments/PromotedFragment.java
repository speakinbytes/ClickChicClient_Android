package com.speakinbytes.base.fragments;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.etsy.android.grid.StaggeredGridView;
import com.etsy.android.grid.util.DynamicHeightTextView;
import com.speakinbytes.base.ProductDetailActivity;
import com.speakinbytes.base.R;
import com.speakinbytes.base.adapters.EtsyGridAdapter;
import com.speakinbytes.base.managers.ProductsManager;
import com.speakinbytes.base.models.Product;
import com.speakinbytes.base.utils.Constants;
import com.speakinbytes.base.utils.VolleySingleton;

import java.util.ArrayList;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link PromotedFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class PromotedFragment extends Fragment implements
        AbsListView.OnScrollListener, AbsListView.OnItemClickListener,ProductsManager.ProductsListener {

    private int mPromoted;
    private static final String TAG = "ETSY";
    private StaggeredGridView mGridView;
    private boolean mHasRequestedMore;
    private EtsyGridAdapter mAdapter;

    private static ArrayList<Product> mData;

    private View rootView;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PromeotedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PromotedFragment newInstance(int param1) {
        PromotedFragment fragment = new PromotedFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.ARGS_INDEX_PROMOTED, param1);
        fragment.setArguments(args);
        return fragment;
    }
    public PromotedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPromoted = getArguments().getInt(Constants.ARGS_INDEX_PROMOTED);
            if(mData == null)
                ProductsManager.getProducts(this, getActivity());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_promoted, container, false);
        return rootView;
    }

    private void findPromoted(Product promoted){

        DynamicHeightTextView txtLineOne = (DynamicHeightTextView) rootView.findViewById(R.id.txtSeller);
        TextView txtModel = (TextView)rootView.findViewById(R.id.txtModel);
        NetworkImageView mImg = (NetworkImageView) rootView.findViewById(R.id.imgMain);
        txtModel.setText(promoted.getModel());
        mImg.setImageUrl(promoted.getImages()[0], VolleySingleton.getInstance().getImageLoader());
    }

    private void findViews()
    {
        mGridView = (StaggeredGridView) rootView.findViewById(R.id.grid_view);
        if (mAdapter == null) {
            mAdapter = new EtsyGridAdapter(getActivity(), R.layout.list_item_card);
            mAdapter.addAll(mData);
        }

        mGridView.setAdapter(mAdapter);
        mGridView.setOnScrollListener(this);
        mGridView.setOnItemClickListener(this);

    }



    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
        Log.d(TAG, "onScrollStateChanged:" + scrollState);
    }

    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        Log.d(TAG, "onScroll firstVisibleItem:" + firstVisibleItem +
                " visibleItemCount:" + visibleItemCount +
                " totalItemCount:" + totalItemCount);
        // our handling
        if (!mHasRequestedMore) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen >= totalItemCount) {
                Log.d(TAG, "onScroll lastInScreen - so load more");
                mHasRequestedMore = true;
                onLoadMoreItems();
            }
        }
    }

    private void onLoadMoreItems() {
        //TODO pedir más datos al servicio
       /* final ArrayList<String> sampleData = SampleData.generateSampleData();
        for (String data : sampleData) {
            mAdapter.add(data);
        }
        // stash all the data in our backing store
        mData.addAll(sampleData);
        // notify the adapter that we can update now
        mAdapter.notifyDataSetChanged();
        mHasRequestedMore = false;*/
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent i = new Intent(getActivity(), ProductDetailActivity.class);
        i.putExtra(Constants.ARGS_PRODUCT,mData.get(position));
        getActivity().startActivity(i);

    }

    @Override
    public void getProducts(boolean result, ArrayList<Product> cats) {
        Product promoted = cats.get(mPromoted);
        findPromoted(promoted);
        cats.remove(mPromoted);
        mData = cats;
        findViews();
    }


}
