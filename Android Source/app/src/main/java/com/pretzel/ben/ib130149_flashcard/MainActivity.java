package com.pretzel.ben.ib130149_flashcard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pretzel.ben.ib130149_flashcard.helper.Global;
import com.pretzel.ben.ib130149_flashcard.helper.MyApplication;


public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // on load set login fragment
        /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fg1, new LoginFragment());
        ft.commit();*/
        // on load listen for param id and run fragment based on id
        MyApplication.setMainActivity(this);
        // get extra params from intent
        Intent i = getIntent();
        String id = i.getStringExtra("fragmentID");

        runFragmentById(id);

        // add Navigation bar to bottom
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.action_updateProfile:
                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_about:
                        runAboutDialog();
                        break;
                    case R.id.action_logout:
                        intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Global.setLogiraniKorisnik(null);
                        Toast.makeText(MyApplication.getContext(), "Uspješna odjava.", Toast.LENGTH_SHORT).show();
                        break;
                }

                return true;
            }
        });
    }

    public void runFragmentById(String id) {
        // check for Fragments
        Fragment fr = null;

        // Login was fragment at first(should go to login if no id is passed
        /*if( id == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fg1, new LoginFragment());
            ft.commit();
            return;
        }*/

        switch(id) {
            case "R.id.fragment_deck_list":
            {
                fr = new DeckListFragment();
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.fg1, fr).commit();
            }
        }

    }

    private void runAboutDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(MyApplication.getMainActivity());

        alert.setTitle("Info");
        // this is set the view from XML inside AlertDialog
        alert.setView(R.layout.about_layout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);

        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = alert.create();
        dialog.show();

    }
}
