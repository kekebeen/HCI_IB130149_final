package com.pretzel.ben.ib130149_flashcard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pretzel.ben.ib130149_flashcard.Model.KorisnikAPI;
import com.pretzel.ben.ib130149_flashcard.helper.MyApplication;
import com.pretzel.ben.ib130149_flashcard.helper.MyRunnable;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputName;
    private EditText inputPassword;
    private EditText inputLastname;
    private EditText inputEmail;
    private EditText inputUsername;

    private RelativeLayout btnRegister;
    private TextView btnLogin;

    public class User {
        private int Id;
        private String Name;
        private String Password;
        private String Lastname;
        private String Address;
        private String Username;
        private String Email;

        public User(String Name,
                    String Password, String Lastname, String Address, String Username, String Email) {
            this.Name = Name;
            this.Password = Password;
            this.Lastname = Lastname;
            this.Address = Address;
            this.Username = Username;
            this.Email = Email;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        MyApplication.setRegisterActivity(this);

        // set field values
        inputName = findViewById(R.id.inputName);
        inputPassword = findViewById(R.id.inputPassword);
        inputLastname = findViewById(R.id.inputLastname);
        inputEmail = findViewById(R.id.inputEmail);
        inputUsername = findViewById(R.id.inputUsername);

        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);

        // event listeners on buttons
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get values and call rest register

                KorisnikAPI.KorisnikVM user = new KorisnikAPI.KorisnikVM(
                        inputName.getText().toString(),
                        inputLastname.getText().toString(),
                        inputEmail.getText().toString(),
                        inputUsername.getText().toString(),
                        inputPassword.getText().toString()
                );

                if(isRegisterFieldsSet()) {
                    KorisnikAPI.Registracija(user, new MyRunnable<KorisnikAPI.ReturnType>() {
                        @Override
                        public void run(KorisnikAPI.ReturnType result) {
                            if(result != null) {
                                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                Toast.makeText(MyApplication.getContext(), "Korisnik uspjesno kreiran" +
                                        "", Toast.LENGTH_SHORT).show();
                                startActivity(i);
                            } else {
                                Toast.makeText(MyApplication.getContext(), "Podaci nisu validni" +
                                        "", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(MyApplication.getContext(), "Morate unijeti sva polja.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // intent to new activity called Login
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean isRegisterFieldsSet() {
        return !inputUsername.getText().toString().isEmpty()
                && !inputPassword.getText().toString().isEmpty()
                && !inputEmail.getText().toString().isEmpty()
                && !inputLastname.getText().toString().isEmpty()
                && !inputName.getText().toString().isEmpty();
    }

}

