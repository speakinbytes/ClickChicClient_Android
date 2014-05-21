package com.speakinbytes.base.managers;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.speakinbytes.base.base.AppBase;
import com.speakinbytes.base.models.Category;
import com.speakinbytes.base.models.Product;
import com.speakinbytes.base.utils.Constants;
import com.speakinbytes.base.utils.VolleyErrorHelper;
import com.speakinbytes.base.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bmjuan on 11/05/14.
 */
public class CategoryManager {

    public static void getCategories(final CategoriesListener listener)
    {
        final String URL = Constants.API_BASE+Constants.GET_CATEGORIES;
        // pass second argument as "null" for GET requests
        JsonObjectRequest req = new JsonObjectRequest(URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray array = response.getJSONArray("categories");
                            ArrayList<Category> categories = new ArrayList<Category>();

                            Gson gson = new Gson();

                            for(int i = 0;i<array.length(); i++)
                            {
                                Type type = new TypeToken<Category>(){}.getType();

                                Category p = gson.fromJson(array.get(i).toString(), type);

                                if(p!=null)
                                    categories.add(p);

                            }
                            if(categories!=null && listener!=null)
                                listener.getCategories(categories.size()>0?true:false, categories);


                        } catch (JSONException e) {
                            listener.getCategories(false, null);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                VolleyErrorHelper.getMessage(error, AppBase.getAppContext());
                listener.getCategories(false, null);
            }
        });

        // add the request object to the queue to be executed
        VolleySingleton.getInstance().addToRequestQueue(req);
    }

    public interface CategoriesListener
    {
        public void getCategories(boolean result, ArrayList<Category> cats);
    }
}
