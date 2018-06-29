package com.pretzel.ben.ib130149_flashcard;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pretzel.ben.ib130149_flashcard.helper.Global;
import com.pretzel.ben.ib130149_flashcard.helper.MyApplication;
import com.pretzel.ben.ib130149_flashcard.helper.MyRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class DeckListFragment extends Fragment {

    //private ArrayList<DeckAPI.DeckIndexVM> itemLabels;
    private ArrayList<com.pretzel.ben.ib130149_flashcard.Model.DeckAPI.DeckVM> itemLabels;
    private int loggedKorisnikId;
    private GridView grid;
    DeckListAdapter adapter;
    private Fragment parentF;
    ImageView btnCreateDeck;
    EditText inputDeckName;
    EditText inputCategory;

    public String[] deckNames = {
            "Name 1",
            "Name 2",
            "Name 3",
            "Name 4",
            "Name 5",
            "Name 6",
            "Name 7",
            "Name 8",
            "Name 9",
            "Name 10"
    };

    public String[] categoryNames = {
            "CAT 1",
            "CAT 2",
            "CAT 3",
            "CAT 4",
            "CAT 5",
            "CAT 6",
            "CAT 7",
            "CAT 8",
            "CAT 9",
            "CAT 10"
    };

    public DeckListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DECK LIST", "should fetch grid items for deck now");
        // get list of DeckIndexVM
        //itemLabels = new ArrayList<DeckAPI.DeckIndexVM>();
        // populate dummy labels (call api when implemented)
        //populateDummyLabels();

    }

    public void populateDummyLabels() {
        int LABEL_MAX = 5;
        String deckName = deckNames[getRandomDeckName()];
        String categoryName = categoryNames[getRandomCategoryName()];

        for(int i = 0; i < LABEL_MAX; i++) {
            DeckAPI.DeckIndexVM dumm = new DeckAPI.DeckIndexVM(deckName, categoryName);

            //itemLabels.add(dumm);
        }
    }

    public int getRandomDeckName() {
        final int min = 0;
        final int max = deckNames.length - 1;
        Random r = new Random();
        final int random = r.nextInt((max - min) + 1) + min;

        return random;
    }

    public int getRandomCategoryName() {
        final int min = 0;
        final int max = categoryNames.length - 1;
        Random r = new Random();
        final int random = r.nextInt((max - min) + 1) + min;

        return random;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_deck_list, container, false);
        grid = view.findViewById(R.id.deck_grid);
        parentF = this;
        btnCreateDeck = view.findViewById(R.id.btn_add_new_deck);
        /*adapter = new DeckListAdapter(MyApplication.getContext(), itemLabels, this);
        grid.setAdapter(adapter);*/

        loggedKorisnikId = Global.getLogiraniKorisnik().KorisnikId;
        com.pretzel.ben.ib130149_flashcard.Model.DeckAPI.GetDekovi(loggedKorisnikId, new MyRunnable<List<com.pretzel.ben.ib130149_flashcard.Model.DeckAPI.DeckVM>>() {
            @Override
            public void run(List<com.pretzel.ben.ib130149_flashcard.Model.DeckAPI.DeckVM> results) {
                if (results.size() >= 0) {
                    Toast.makeText(MyApplication.getContext(), "Dekovi uspjesno dobavljeni" +
                            "", Toast.LENGTH_SHORT).show();
                    adapter = new DeckListAdapter(MyApplication.getContext(), results, parentF);
                    grid.setAdapter(adapter);

                } else {
                    Toast.makeText(MyApplication.getContext(), "Greska prilikom dobavljanja dekova" +
                            "", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // add new deck callback
        btnCreateDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("add new deck", "add new deck now");
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.dialog_add_new_deck, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(MyApplication.getMainActivity());
                alert.setTitle("Info");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                inputCategory = alertLayout.findViewById(R.id.add_new_deck_kategorija);
                inputDeckName = alertLayout.findViewById(R.id.add_new_deck_name);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(false);
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                alert.setPositiveButton("Add Deck", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Integer logiraniKorisnikId = Global.getLogiraniKorisnik().KorisnikId;
                        String Ime = inputDeckName.getText().toString();
                        String Kategorija = inputCategory.getText().toString();
                        com.pretzel.ben.ib130149_flashcard.Model.DeckAPI.DeckVM model = new com.pretzel.ben.ib130149_flashcard.Model.DeckAPI.DeckVM(Ime, Kategorija, loggedKorisnikId);
                        // try to add new deck and then refetchDecks in success callback
                        com.pretzel.ben.ib130149_flashcard.Model.DeckAPI.AddNewDeck(model, new MyRunnable<com.pretzel.ben.ib130149_flashcard.Model.DeckAPI.ReturnType>() {
                            @Override
                            public void run(com.pretzel.ben.ib130149_flashcard.Model.DeckAPI.ReturnType response) {
                                // call refetch now
                                if(response != null) {
                                    refetchDecks();
                                } else {
                                    Toast.makeText(MyApplication.getContext(), "Greska prilikom dodavanja deka" +
                                            "", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });


        return view;
    }

    public void refetchDecks() {
        int loggedKorisnikId = Global.getLogiraniKorisnik().KorisnikId;
        com.pretzel.ben.ib130149_flashcard.Model.DeckAPI.GetDekovi(loggedKorisnikId, new MyRunnable<List<com.pretzel.ben.ib130149_flashcard.Model.DeckAPI.DeckVM>>() {
            @Override
            public void run(List<com.pretzel.ben.ib130149_flashcard.Model.DeckAPI.DeckVM> results) {
                if (results.size() >= 0) {
                    Toast.makeText(MyApplication.getContext(), "Dekovi uspjesno dobavljeni" +
                            "", Toast.LENGTH_SHORT).show();
                    adapter = new DeckListAdapter(MyApplication.getContext(), results, parentF);
                    grid.setAdapter(adapter);
                } else {
                    Toast.makeText(MyApplication.getContext(), "Greska prilikom dobavljanja dekova" +
                            "", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void navigateToQuestionsFragment(int itemID) {

        Bundle data = new Bundle();
        data.putInt("deckID", itemID);

        Fragment questionsFragment = new QuestionsFragment();
        questionsFragment.setArguments(data);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_deck_list, questionsFragment);
        ft.addToBackStack(null);
        ft.commit();
    }


}
