package com.speakinbytes.base.adapters;

/**
 * Created by bmjuan on 10/04/14.
 */
import java.util.LinkedHashMap;
import java.util.Map;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.speakinbytes.base.R;

public class NavigationListAdapter extends BaseAdapter
{
    public final Map<String, Adapter> sections = new LinkedHashMap<String, Adapter>();
    public final ArrayAdapter<String> headers;
    public final static int TYPE_SECTION_HEADER = 0;
    private final static int[] icons= new int[]{R.drawable.abc_ic_go, R.drawable.ic_drawer, R.drawable.abc_ic_go, R.drawable.abc_ic_voice_search, R.drawable.abc_ic_search};

    public NavigationListAdapter(Context context)
    {
        headers = new ArrayAdapter<String>(context, R.layout.list_header);
    }

    public void addSection(String section, Adapter adapter)
    {
        this.headers.add(section);
        this.sections.put(section, adapter);
    }

    public Object getItem(int position)
    {
        for (Object section : this.sections.keySet())
        {
            Adapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;

            // check if position inside this section
            if (position == 0) return section;
            if (position < size) return adapter.getItem(position - 1);

            // otherwise jump into next section
            position -= size;
        }
        return null;
    }

    public int getCount()
    {
        // total together all sections, plus one for each section header
        int total = 0;
        for (Adapter adapter : this.sections.values())
            total += adapter.getCount() + 1;
        return total;
    }

    @Override
    public int getViewTypeCount()
    {
        // assume that headers count as one, then total all sections
        int total = 1;
        for (Adapter adapter : this.sections.values())
            total += adapter.getViewTypeCount();
        return total;
    }

    @Override
    public int getItemViewType(int position)
    {
        int type = 1;
        for (Object section : this.sections.keySet())
        {
            Adapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;

            // check if position inside this section
            if (position == 0) return TYPE_SECTION_HEADER;
            if (position < size) return type + adapter.getItemViewType(position - 1);

            // otherwise jump into next section
            position -= size;
            type += adapter.getViewTypeCount();
        }
        return -1;
    }

    public boolean areAllItemsSelectable()
    {
        return false;
    }

    @Override
    public boolean isEnabled(int position)
    {
        return (getItemViewType(position) != TYPE_SECTION_HEADER);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        int sectionnum = 0;
        for (Object section : this.sections.keySet())
        {
            Adapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;

            // check if position inside this section
            if (position == 0) return headers.getView(sectionnum, convertView, parent);
            if (position < size){
            View v = adapter.getView(position - 1, convertView, parent);
            TextView textView= (TextView) v.findViewById(R.id.list_item_title);
            textView.setCompoundDrawablesWithIntrinsicBounds(
                   icons[position-1],
                    0,
                    0,
                    0);
             return v;
            }

            // otherwise jump into next section
            position -= size;
            sectionnum++;
        }
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

}

