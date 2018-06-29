package com.pretzel.ben.ib130149_flashcard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pretzel.ben.ib130149_flashcard.Model.KorisnikAPI;
import com.pretzel.ben.ib130149_flashcard.helper.Global;
import com.pretzel.ben.ib130149_flashcard.helper.MyApplication;
import com.pretzel.ben.ib130149_flashcard.helper.MyRunnable;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    // set variables
    private EditText inputUsername;
    private EditText inputPassword;
    private TextView btnRegister;
    private RelativeLayout btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        MyApplication.setLoginActivity(this);

        // bind buttons and form items
        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // log values
                Log.d("login username", inputUsername.getText().toString());
                Log.d("login pass", inputPassword.getText().toString());
                String username = inputUsername.getText().toString();
                String password = inputPassword.getText().toString();

                if (isLoginFieldsSet()) {

                    KorisnikAPI.Provjeri(username, password, new MyRunnable<KorisnikAPI.KorisnikVM>() {
                        @Override
                        public void run(KorisnikAPI.KorisnikVM result) {
                            if (result != null) {
                                Toast.makeText(MyApplication.getContext(), "Korisničko ime ili lozinka VALIDNi" +
                                        "", Toast.LENGTH_SHORT).show();

                                Global.setLogiraniKorisnik(result);
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                i.putExtra("fragmentID", "R.id.fragment_deck_list");
                                i.putExtra("korisnik", result);
                                startActivity(i);

                            } else {
                                Toast.makeText(MyApplication.getContext(), "Korisničko ime ili lozinka nisu validni" +
                                        "", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(MyApplication.getContext(), "Korisničko ime ili lozinka nisu uneseni" +
                            "", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LOGIN::: ", "should open login now");
                // make new transaction
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                LoginActivity.this.overridePendingTransition(0,0);
            }
        });

    }

    private boolean isLoginFieldsSet() {
        return !inputUsername.getText().toString().isEmpty()
                && !inputPassword.getText().toString().isEmpty();
    }

}
