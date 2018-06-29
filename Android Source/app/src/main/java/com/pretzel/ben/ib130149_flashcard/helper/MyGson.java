package com.pretzel.ben.ib130149_flashcard.helper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MyGson
{
    public static Gson build()
    {
        GsonBuilder builder = new GsonBuilder();
        return builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
    }
}
