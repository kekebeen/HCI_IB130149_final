package com.pretzel.ben.ib130149_flashcard.helper;

import android.content.SharedPreferences;

import com.pretzel.ben.ib130149_flashcard.Model.KorisnikAPI;

public class Global {
    private static final String PREFS_NAME = "DatotekaZaSharedPrefernces";
    public static final String KEY_LOGIRANI_KORISNIK = "logiraniKorisnikJson";

    // Auth helpers to set
    public static KorisnikAPI.KorisnikVM getLogiraniKorisnik() {
        SharedPreferences settings = MyApplication.getContext().getSharedPreferences(PREFS_NAME, 0);
        String str = settings.getString(KEY_LOGIRANI_KORISNIK, "");
        if (str.length() == 0)
            return null;
        return MyGson.build().fromJson(str, KorisnikAPI.KorisnikVM.class);
    }

    public static void setLogiraniKorisnik(KorisnikAPI.KorisnikVM logiraniKorisnik) {

        final String str = logiraniKorisnik!=null?MyGson.build().toJson(logiraniKorisnik):"";

        SharedPreferences settings = MyApplication.getContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(KEY_LOGIRANI_KORISNIK, str);

        editor.commit();
    }
}
