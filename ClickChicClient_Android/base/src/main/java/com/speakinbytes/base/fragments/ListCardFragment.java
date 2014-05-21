package com.speakinbytes.base.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.toolbox.ImageLoader;
import com.koushikdutta.ion.Ion;
import com.speakinbytes.base.HomeActivity;
import com.speakinbytes.base.ProductDetailActivity;
import com.speakinbytes.base.R;
import com.speakinbytes.base.adapters.CCCardArrayMultiChoiceAdapter;
import com.speakinbytes.base.managers.ProductsManager;
import com.speakinbytes.base.models.Product;
import com.speakinbytes.base.utils.Constants;
import com.speakinbytes.base.utils.VolleySingleton;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by bmjuan on 22/04/14.
 */
public class ListCardFragment extends Fragment implements Card.OnLongCardClickListener, Card.OnCardClickListener, ProductsManager.ProductsListener{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    CCCardArrayMultiChoiceAdapter mCardAdapter;
    private static ImageLoader mImageLoader;
    private static ArrayList<Product> mProducts;

    private CardListView listView;
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ListCardFragment newInstance(String sectionName) {
        ListCardFragment fragment = new ListCardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_NUMBER, sectionName);
        fragment.setArguments(args);
        mImageLoader = VolleySingleton.getInstance().getImageLoader();

        return fragment;
    }

    public ListCardFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mProducts==null)
            ProductsManager.getProducts(this, getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        listView = (CardListView) rootView.findViewById(R.id.myList);
       /* if (listView!=null){
            listView.setAdapter(mCardAdapter);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        }*/

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        //((HomeActivity)getActivity()).onSectionAttached(getArguments().getString(ARG_SECTION_NUMBER));
    }

    private ArrayList<Card> generateCards(ArrayList<Product> products)
    {
        ArrayList<Card> cards = new ArrayList<Card>();
        if(products!=null)
        {


            for(int i=0;i<products.size();i++){
                //Create a Card
                Card card = new Card(getActivity());
                //Create a CardHeader
                CardHeader header = new CardHeader(getActivity());
                try {
                    String title = URLDecoder.decode(products.get(i).getModel(), "UTF-8");
                    header.setTitle(title);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                //Add Header to card
                card.setOnLongClickListener((Card.OnLongCardClickListener) this);
                card.setOnClickListener((Card.OnCardClickListener) this);
                //Add Thumbnail
                CustomCardThumbnail thumbnail = new CustomCardThumbnail(getActivity(), products.get(i).getImages()[0]);

                thumbnail.setExternalUsage(true);


                //Error Resource ID
                thumbnail.setErrorResource(R.drawable.ic_launcher);
                //You need to set true to use an external library

                card.addCardThumbnail(thumbnail);
                card.addCardHeader(header);

                cards.add(card);

            }
        }
        return cards;

    }


    @Override
    public boolean onLongClick(Card card, View view) {
        return mCardAdapter.startActionMode(getActivity());

    }

    @Override
    public void onClick(Card card, View view) {
        Intent i = new Intent(getActivity(), ProductDetailActivity.class);
        i.putExtra(Constants.ARGS_PRODUCT,mProducts.get(0));
        getActivity().startActivity(i);
    }

    @Override
    public void getProducts(boolean result, ArrayList<Product> products) {
        mProducts = products;
        mCardAdapter = new CCCardArrayMultiChoiceAdapter(getActivity(), generateCards(products));

        if (listView!=null){
            listView.setAdapter(mCardAdapter);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        }
    }

    public class CustomCardThumbnail extends CardThumbnail {

        private String mUrl;
        public CustomCardThumbnail(Context ctx, String url)
        {
            super(ctx, R.layout.custom_thumb);
            mUrl =  url;
        }


        @Override
        public void setupInnerViewElements(ViewGroup parent, View viewImage) {

            //((NetworkImageView)viewImage).setImageUrl("http://www.ticoiescolme.org.es/4inf/famrsj/feliz.jpg",mImageLoader);
            viewImage.getLayoutParams().width =500;
            viewImage.getLayoutParams().height = 350;
            Ion.with((ImageView) viewImage)
                    .resize(128, 128)
                    .centerInside()
                    .load(mUrl);


        }
    }
}
