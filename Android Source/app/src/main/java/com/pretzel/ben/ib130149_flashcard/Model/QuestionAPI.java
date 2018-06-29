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

public class QuestionAPI {

    public static class QuestionIndexVM implements Serializable
    {
        public int DeckId;
        public int PitanjeId;
        public String Pitanje;
        public String Odgovor;
        public Boolean isFlipped;

        public QuestionIndexVM(int DeckId, String Pitanje, String Odgovor, Boolean isFlipped) {
            this.DeckId = DeckId;
            this.Pitanje = Pitanje;
            this.Odgovor = Odgovor;
            this.isFlipped = isFlipped;
        }
    }

    public static class QuestionCreateVM implements Serializable {
        public int DeckId;
        public String Pitanje;
        public String Odgovor;
        public Boolean isFlipped;

        public QuestionCreateVM(int DeckId, String Pitanje, String Odgovor, Boolean isFlipped) {
            this.DeckId = DeckId;
            this.Pitanje = Pitanje;
            this.Odgovor = Odgovor;
            this.isFlipped = isFlipped;
        }
    }

    public static class ReturnType
    {
        public String poruka;
    }

    public static void GetPitanjaByDeck(int DeckId, final MyRunnable<List<com.pretzel.ben.ib130149_flashcard.Model.QuestionAPI.QuestionIndexVM>> onSuccess)
    {

        RequestQueue queue = Volley.newRequestQueue(MyApplication.getContext());
        String url = "Pitanja?DeckId=" + DeckId;

        // Request a string response from the provided URL.
        final ProgressDialog dialog = ProgressDialog.show(MyApplication.getMainActivity(), "Ucitavanje pitanja", "U toku");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.urlApi + url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Type type = new TypeToken<ArrayList<com.pretzel.ben.ib130149_flashcard.Model.QuestionAPI.QuestionIndexVM>>() {
                        }.getType();

                        final Gson gson = new GsonBuilder().create();
                        final List<com.pretzel.ben.ib130149_flashcard.Model.QuestionAPI.QuestionIndexVM> model = gson.fromJson(response, type);
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

    public static void DeletePitanje(int DeckId, int PitanjeId, final MyRunnable<QuestionCreateVM> onSuccess)
    {
        RequestQueue queue = Volley.newRequestQueue(MyApplication.getContext());
        String url = "Pitanja?DeckId=" + DeckId + "&PitanjeId=" + PitanjeId;

        // Request a string response from the provided URL.
        final ProgressDialog dialog = ProgressDialog.show(MyApplication.getMainActivity(), "Brisanje pitanja", "U toku");
        GsonRequest stringRequest = new GsonRequest(Request.Method.DELETE,  Config.urlApi + url, QuestionCreateVM.class, null, DeckId,
                new Response.Listener<QuestionCreateVM>()
                {
                    @Override
                    public void onResponse(QuestionCreateVM response)
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
    public static void AddNewQuestion(QuestionCreateVM model, final MyRunnable<QuestionCreateVM> onSuccess)
    {
        RequestQueue queue = Volley.newRequestQueue(MyApplication.getContext());
        String url = "Pitanja/";

        // Request a string response from the provided URL.
        final ProgressDialog dialog = ProgressDialog.show(MyApplication.getMainActivity(), "Kreiranje pitanja", "U toku");
        GsonRequest stringRequest = new GsonRequest(Request.Method.POST,  Config.urlApi + url, QuestionCreateVM.class, null, model,
                new Response.Listener<QuestionCreateVM>()
                {
                    @Override
                    public void onResponse(QuestionCreateVM response)
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
