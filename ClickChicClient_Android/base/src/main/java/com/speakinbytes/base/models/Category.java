package com.speakinbytes.base.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bmjuan on 11/05/14.
 */
public class Category {

    @SerializedName("_id")
    private String id;

    @SerializedName("name_es")
    private String name_es;

    @SerializedName("__v")
    private String __v;


    public Category(String id, String name_es, String __v) {
        this.id = id;
        this.name_es = name_es;
        this.__v = __v;
    }

    public String get() {
        return __v;
    }

    public String getName() {
        return name_es;
    }

    public String getId() {
        return id;
    }
}
