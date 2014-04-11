package com.speakinbytes.base.adapters;

/**
 * Created by bmjuan on 09/04/14.
 */

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.speakinbytes.base.R;

import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayMultiChoiceAdapter;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayMultiChoiceAdapter;
import it.gmariotti.cardslib.library.view.CardView;

public class CCCardArrayMultiChoiceAdapter extends CardArrayMultiChoiceAdapter {

    private ActionMode mActionMode;
    public CCCardArrayMultiChoiceAdapter(Context context, List<Card> cards) {
        super(context, cards);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        super.onCreateActionMode(mode, menu);

        mActionMode=mode; // to manage mode in your Fragment/Activity

        //If you would like to inflate your menu
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.carddemo_multichoice, menu);

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if (item.getItemId() == R.id.menu_share) {
            Toast.makeText(getContext(), "Share;" + formatCheckedCard(), Toast.LENGTH_SHORT).show();
            return true;
        }
        if (item.getItemId() == R.id.menu_discard) {
            discardSelectedItems(mode);
            return true;
        }
        return false;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked, CardView cardView, Card card) {
        Toast.makeText(getContext(), "Click;" + position + " - " + checked, Toast.LENGTH_SHORT).show();
    }

    private void discardSelectedItems(ActionMode mode) {
        ArrayList<Card> items = getSelectedCards();
        for (Card item : items) {
            remove(item);
        }
        mode.finish();
    }


    private String formatCheckedCard() {

        SparseBooleanArray checked = mCardListView.getCheckedItemPositions();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < checked.size(); i++) {
            if (checked.valueAt(i) == true) {
                sb.append("\nPosition=" + checked.keyAt(i));
            }
        }
        return sb.toString();
    }

}
