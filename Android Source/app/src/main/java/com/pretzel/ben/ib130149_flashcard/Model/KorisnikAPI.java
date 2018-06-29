package com.pretzel.ben.ib130149_flashcard.Model;

import android.app.ProgressDialog;
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
import com.pretzel.ben.ib130149_flashcard.helper.Config;
import com.pretzel.ben.ib130149_flashcard.helper.MyApplication;
import com.pretzel.ben.ib130149_flashcard.helper.MyRunnable;
import com.pretzel.ben.ib130149_flashcard.helper.Volley.GsonRequest;

import java.io.Serializable;

public class KorisnikAPI {

    public static class KorisnikVM implements Serializable
    {
        public int KorisnikId;
        public String Ime;
        public String Prezime;
        public String Email;
        public String Lozinka;
        public String KorisnickoIme;

        public KorisnikVM(String Ime, String Prezime, String Email, String KorisnickoIme, String Lozinka) {
            this.Ime = Ime;
            this.Prezime = Prezime;
            this.Email = Email;
            this.KorisnickoIme = KorisnickoIme;
            this.Lozinka = Lozinka;
        }
    }


    public static class Korisnik implements Serializable
    {
        public Integer KorisnikId ;
        public String Ime ;
        public String Prezime ;
        public String Email ;
        public String Lozinka ;
        public String KorisnickoIme;
        public int ProfilId;
    }

    public static class ReturnType
    {
        public String poruka;
    }

    public static void Provjeri(String username, String password, final MyRunnable<KorisnikVM> onSuccess)
    {

        RequestQueue queue = Volley.newRequestQueue(MyApplication.getContext());
        String url = "Auth/Prijava?username="+username+"&password="+password;

        // Request a string response from the provided URL.
        final ProgressDialog dialog = ProgressDialog.show(MyApplication.getLoginActivity(), "Pristup podacima", "U toku");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.urlApi + url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        final Gson gson = new GsonBuilder().create();
                        final KorisnikVM model = gson.fromJson(response, KorisnikVM.class);
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

    public static void Registracija(KorisnikVM korisnik, final MyRunnable<ReturnType> onSuccess)
    {
        RequestQueue queue = Volley.newRequestQueue(MyApplication.getContext());
        String url = "Auth/";

        // Request a string response from the provided URL.
        final ProgressDialog dialog = ProgressDialog.show(MyApplication.getRegisterActivity(), "Registracija korisnika", "U toku");
        GsonRequest stringRequest = new GsonRequest(Request.Method.POST,  Config.urlApi + url, ReturnType.class, null, korisnik,
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
