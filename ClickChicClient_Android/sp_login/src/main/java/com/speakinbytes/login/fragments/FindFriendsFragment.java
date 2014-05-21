package com.speakinbytes.login.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.speakinbytes.login.R;
import com.speakinbytes.login.SellersTwAdapter;
import com.speakinbytes.login.models.PerfilModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link FindFriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class FindFriendsFragment extends Fragment {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FindFriendsFragment.
     */
    private ListView mList;
    public static FindFriendsFragment newInstance() {
        FindFriendsFragment fragment = new FindFriendsFragment();
        return fragment;
    }
    public FindFriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.find_friends, container, false);
        mList = (ListView) view.findViewById(R.id.listView);
        SellersTwAdapter  mAdapter= new SellersTwAdapter(getActivity(), R.layout.item_tw_list);
        mAdapter.addAll(generateData());
        mList.setAdapter(mAdapter);
        return view;
    }

    private ArrayList<PerfilModel> generateData()
    {
        ArrayList<PerfilModel> perfils = new ArrayList<PerfilModel>();
        for(int i = 0;i<5;i++)
            perfils.add(new PerfilModel());

        return perfils;

    }


}
