package com.pretzel.ben.ib130149_flashcard.Model;

import android.app.ProgressDialog;
import android.graphics.Path;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.pretzel.ben.ib130149_flashcard.LoginActivity;
import com.pretzel.ben.ib130149_flashcard.ProfileActivity;
import com.pretzel.ben.ib130149_flashcard.helper.Config;
import com.pretzel.ben.ib130149_flashcard.helper.MyApplication;
import com.pretzel.ben.ib130149_flashcard.helper.MyRunnable;
import com.pretzel.ben.ib130149_flashcard.helper.Volley.GsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DeckAPI {

    public static class DeckVM implements Serializable
    {
        public int DeckId;
        public String Ime;
        public int KategorijaId;
        public String Kategorija;
        public int KorisnikId;

        public DeckVM(String Ime, String Kategorija, int KorisnikId) {
            this.KorisnikId = KorisnikId;
            this.Ime = Ime;
            this.Kategorija = Kategorija;
        }
    }

    public static class ReturnType
    {
        public String poruka;
    }

    public static void GetDekovi(int KorisnikId, final MyRunnable<List<DeckVM>> onSuccess)
    {

        RequestQueue queue = Volley.newRequestQueue(MyApplication.getContext());
        String url = "Dekovi?KorisnikId=" + KorisnikId;

        // Request a string response from the provided URL.
        final ProgressDialog dialog = ProgressDialog.show(MyApplication.getMainActivity(), "Ucitavanje dekova", "U toku");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.urlApi + url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Type type = new TypeToken<ArrayList<DeckVM>>() {
                        }.getType();

                        final Gson gson = new GsonBuilder().create();
                        final List<DeckVM> model = gson.fromJson(response, type);
                        dialog.dismiss();

                        onSuccess.run(model);
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.d("ERROR::: ", error);
                dialog.dismiss();
                Toast.makeText(MyApplication.getContext(), "Greška u komunikaciji sa serverom.", Toast.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public static void DeleteDeckById(int DeckId, final MyRunnable<DeckVM> onSuccess)
    {
        RequestQueue queue = Volley.newRequestQueue(MyApplication.getContext());
        String url = "Dekovi?DeckId=" + DeckId;

        // Request a string response from the provided URL.
        final ProgressDialog dialog = ProgressDialog.show(MyApplication.getMainActivity(), "Brisanje deka", "U toku");
        GsonRequest stringRequest = new GsonRequest(Request.Method.DELETE,  Config.urlApi + url, DeckVM.class, null, DeckId,
                new Response.Listener<DeckVM>()
                {
                    @Override
                    public void onResponse(DeckVM response)
                    {
                        dialog.dismiss();
                        onSuccess.run(response);
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.d("ERROR::: ", error);
                dialog.dismiss();
                Toast.makeText(MyApplication.getContext(), "Greška u komunikaciji sa serverom.", Toast.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    // add new deck
    public static void AddNewDeck(DeckVM deck, final MyRunnable<ReturnType> onSuccess)
    {
        RequestQueue queue = Volley.newRequestQueue(MyApplication.getContext());
        String url = "Dekovi/";

        // Request a string response from the provided URL.
        final ProgressDialog dialog = ProgressDialog.show(MyApplication.getMainActivity(), "Kreiranje deka", "U toku");
        GsonRequest stringRequest = new GsonRequest(Request.Method.POST,  Config.urlApi + url, ReturnType.class, null, deck,
                new Response.Listener<ReturnType>()
                {
                    @Override
                    public void onResponse(ReturnType response)
                    {
                        dialog.dismiss();
                        onSuccess.run(response);
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                Toast.makeText(MyApplication.getContext(), "Greška u komunikaciji sa serverom.", Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
