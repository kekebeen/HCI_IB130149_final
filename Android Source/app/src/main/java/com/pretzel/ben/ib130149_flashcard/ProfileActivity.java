package com.pretzel.ben.ib130149_flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pretzel.ben.ib130149_flashcard.Model.KorisnikAPI;
import com.pretzel.ben.ib130149_flashcard.Model.ProfilAPI;
import com.pretzel.ben.ib130149_flashcard.helper.Global;
import com.pretzel.ben.ib130149_flashcard.helper.MyApplication;
import com.pretzel.ben.ib130149_flashcard.helper.MyRunnable;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {

    TextView userName;
    TextView cityAndAddress;
    EditText opstina;
    EditText tel;
    EditText ulica;
    EditText grad;
    RelativeLayout btnUpdate;
    int logiraniKorisnikId;
    KorisnikAPI.KorisnikVM tmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        MyApplication.setProfileActivity(this);

        // get all items for later
        userName = findViewById(R.id.profile_name);
        cityAndAddress = findViewById(R.id.profile_city_and_address);
        opstina = findViewById(R.id.profile_opstina);
        tel = findViewById(R.id.profile_tel);
        ulica = findViewById(R.id.profile_ulica);
        grad = findViewById(R.id.profile_grad);
        RelativeLayout btnUpdate = findViewById(R.id.btn_profile_update);

        // api call and get initial user profile;
        getInitialProfile();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // make object
                // kid, pid ulica grad opstina telefon
                ProfilAPI.ProfilVM profil = new ProfilAPI.ProfilVM(logiraniKorisnikId, ulica.getText().toString(), grad.getText().toString(), opstina.getText().toString(), tel.getText().toString());
                // on click call put update
                    ProfilAPI.UpdateProfile(profil, new MyRunnable<ProfilAPI.ReturnType>() {
                        @Override
                        public void run(ProfilAPI.ReturnType result) {
                            if (result != null) {
                                Toast.makeText(MyApplication.getContext(), "Profil uspjesno spasen" +
                                        "", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(MyApplication.getProfileActivity(), MainActivity.class);
                                i.putExtra("fragmentID", "R.id.fragment_deck_list");
                                startActivity(i);
                            } else {
                                Toast.makeText(MyApplication.getContext(), "Greska prilikom updatea" +
                                        "", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }
        });
    }

    public void getInitialProfile() {
        //call rest and set initial values if there is profile assigned to user
        // get userID from shared preferences
        logiraniKorisnikId = Global.getLogiraniKorisnik().KorisnikId;
        tmp = Global.getLogiraniKorisnik();

        if(tmp != null) {
            ProfilAPI.GetProfile(logiraniKorisnikId, new MyRunnable<ProfilAPI.ProfilVM>() {
                @Override
                public void run(ProfilAPI.ProfilVM result) {
                    if (result != null) {
                        Toast.makeText(MyApplication.getContext(), "Profil uspjesno dobavljen" +
                                "", Toast.LENGTH_SHORT).show();
                        userName.setText(tmp.KorisnickoIme.toString());
                        cityAndAddress.setText(result.Grad + ", " + result.Ulica);
                        opstina.setText(result.Opstina);
                        tel.setText(result.BrojTelefona);
                        ulica.setText(result.Ulica);
                        grad.setText(result.Grad);
                        //Intent i = new Intent(ProfileActivity.this, MainActivity.class);
                        //startActivity(i);
                    } else {
                        Toast.makeText(MyApplication.getContext(), "Greska prilikom dobavljanja korisnika" +
                                "", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(MyApplication.getContext(), "Greska prilikom dobavljanja korisnika.", Toast.LENGTH_SHORT).show();
        }
    }

}
