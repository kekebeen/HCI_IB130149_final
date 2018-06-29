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
import com.pretzel.ben.ib130149_flashcard.LoginActivity;
import com.pretzel.ben.ib130149_flashcard.ProfileActivity;
import com.pretzel.ben.ib130149_flashcard.helper.Config;
import com.pretzel.ben.ib130149_flashcard.helper.MyApplication;
import com.pretzel.ben.ib130149_flashcard.helper.MyRunnable;
import com.pretzel.ben.ib130149_flashcard.helper.Volley.GsonRequest;

import java.io.Serializable;

public class ProfilAPI {

    public static class ProfilVM implements Serializable
    {
        public int KorisnikId;
        public String Ulica;
        public String Grad;
        public String Opstina;
        public String BrojTelefona;

        public ProfilVM(int KorisnikId, String Ulica, String Grad, String Opstina, String BrojTelefona) {
            this.KorisnikId = KorisnikId;
            this.Ulica = Ulica;
            this.Grad = Grad;
            this.Opstina = Opstina;
            this.BrojTelefona = BrojTelefona;
        }
    }

    public static class ReturnType
    {
        public String poruka;
    }

    public static void GetProfile(int KorisnikId, final MyRunnable<ProfilVM> onSuccess)
    {

        RequestQueue queue = Volley.newRequestQueue(MyApplication.getContext());
        String url = "Profili?KorisnikId=" + KorisnikId;

        // Request a string response from the provided URL.
        final ProgressDialog dialog = ProgressDialog.show(MyApplication.getProfileActivity(), "Pristup podacima o profilu", "U toku");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.urlApi + url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        final Gson gson = new GsonBuilder().create();
                        final ProfilVM model = gson.fromJson(response, ProfilVM.class);
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

    public static void UpdateProfile(ProfilVM model, final MyRunnable<ReturnType> onSuccess)
    {
        RequestQueue queue = Volley.newRequestQueue(MyApplication.getContext());
        String url = "Profili/";

        // Request a string response from the provided URL.
        final ProgressDialog dialog = ProgressDialog.show(MyApplication.getProfileActivity(), "Spasavanje profila", "U toku");
        GsonRequest stringRequest = new GsonRequest(Request.Method.PUT,  Config.urlApi + url, ReturnType.class, null, model,
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
                VolleyLog.d("ERROR::: ", error);
                dialog.dismiss();
                Toast.makeText(MyApplication.getContext(), "Greška u komunikaciji sa serverom.", Toast.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
