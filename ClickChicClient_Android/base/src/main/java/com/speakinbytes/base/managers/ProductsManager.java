package com.speakinbytes.base.managers;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.speakinbytes.base.base.AppBase;
import com.speakinbytes.base.models.Product;
import com.speakinbytes.base.utils.Constants;
import com.speakinbytes.base.utils.VolleyErrorHelper;
import com.speakinbytes.base.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bmjuan on 11/05/14.
 */
public class ProductsManager {

    private static ProgressDialog progressDialog;
    public static void getProduct(String id)
    {
        final String URL = Constants.API_BASE+ Constants.GET_PRODUCT+id;
        // pass second argument as "null" for GET requests
        JsonObjectRequest req = new JsonObjectRequest(URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Gson gson = new Gson();
                            Product p = gson.fromJson(response.toString(),Product.class);
                            VolleyLog.v("Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyErrorHelper.getMessage(error, AppBase.getAppContext());
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        // add the request object to the queue to be executed
        VolleySingleton.getInstance().addToRequestQueue(req);
    }


    public static void createProduct()
    {
        final String URL = Constants.API_BASE+ Constants.CREATE_PRODUCT;
        // Post params to be sent to the server
        //TODO crear el hashmap con los datos
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", "AbCdEfGh123456");

        JsonObjectRequest req = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyErrorHelper.getMessage(error, AppBase.getAppContext());
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

    // add the request object to the queue to be executed
        VolleySingleton.getInstance().addToRequestQueue(req);
    }

    public static void getProducts(final ProductsListener listener,final Context ctx)
    {
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setTitle("Cargando...");
        progressDialog.show();
        final String URL = Constants.API_BASE+ Constants.GET_PRODUCTS;
        // pass second argument as "null" for GET requests
        JsonObjectRequest req = new JsonObjectRequest(URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray array = response.getJSONArray("products");
                            ArrayList<Product> products = new ArrayList<Product>();

                            Gson gson = new Gson();

                            for(int i = 0;i<array.length(); i++)
                            {
                                Type type = new TypeToken<Product>(){}.getType();

                                Product p = gson.fromJson(array.get(i).toString(), type);

                                if(p!=null)
                                    products.add(p);

                            }
                            progressDialog.dismiss();
                            if(products!=null && listener!=null)
                                listener.getProducts(true, products);

                        } catch (JSONException e) {
                            listener.getProducts(false, null);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyErrorHelper.getMessage(error, AppBase.getAppContext());
                VolleyLog.e("Error: ", error.getMessage());
                error.printStackTrace();
                listener.getProducts(false, null);
                progressDialog.dismiss();
                Toast.makeText(ctx, "Se ha producido un error", Toast.LENGTH_SHORT).show();
            }
        });

        // add the request object to the queue to be executed
        VolleySingleton.getInstance().addToRequestQueue(req);
    }

    public interface ProductsListener
    {
        public void getProducts(boolean result, ArrayList<Product> cats);
    }
}
