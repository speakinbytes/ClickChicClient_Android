package com.speakinbytes.base.models;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by bmjuan on 11/05/14.
 */
public class Product implements Serializable{

    @SerializedName("_id")
    private String id;

    @SerializedName("model")
    private String model;

    @SerializedName("description")
    private String description;

    @SerializedName("seller_id")
    private String seller_id;

    @SerializedName("category_id")
    private String category_id;

    @SerializedName("subcategory_id")
    private String subcategory_id;

    @SerializedName("price")
    private int price;

    @SerializedName("images")
    private String images[];

    @SerializedName("colour")
    private String colour;

    @SerializedName("units")
    private int units;

    @SerializedName("gender")
    private String gender;

  /*  @SerializedName("created_at")
    private Date created_at;

    @SerializedName("modified_at")
    private Date modified_at;*/


    public Product(String id, String model, String description, String seller_id, String category_id, int price, String subcategory_id, String[] images, String colour, int units, String gender) {
        this.id = id;
        this.model = model;
        this.description = description;
        this.seller_id = seller_id;
        this.category_id = category_id;
        this.price = price;
        this.subcategory_id = subcategory_id;
        this.images = images;
        this.colour = colour;
        this.units = units;
        this.gender = gender;
    }



    public String getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getDescription() {
        return description;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getSubcategory_id() {
        return subcategory_id;
    }

    public int getPrice() {
        return price;
    }

    public String[] getImages() {
        return images;
    }

    public String getColour() {
        return colour;
    }

    public int getUnits() {
        return units;
    }

    public String getGender() {
        return gender;
    }

}
