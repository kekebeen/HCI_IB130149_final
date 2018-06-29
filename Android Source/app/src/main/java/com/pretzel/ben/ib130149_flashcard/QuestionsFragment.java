package com.pretzel.ben.ib130149_flashcard;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.pretzel.ben.ib130149_flashcard.Model.DeckAPI;
import com.pretzel.ben.ib130149_flashcard.helper.Global;
import com.pretzel.ben.ib130149_flashcard.helper.MyApplication;
import com.pretzel.ben.ib130149_flashcard.helper.MyRunnable;

import java.util.ArrayList;
import java.util.List;


public class QuestionsFragment extends Fragment {
    private ArrayList<QuestionAPI.QuestionIndexVM> questions;
    private RecyclerView grid;
    private ImageView btnCreateQuestion;
    private int loggedKorisnikId;
    private int currentDeckId;
    private Fragment parentF;
    private EditText inputPitanje;
    private EditText inputOdgovor;
    private ImageView btnQuestionGoBack;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get items from network
        questions = new ArrayList<QuestionAPI.QuestionIndexVM>();
        populateDummyQuestions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final int deckID = getArguments().getInt("deckID");
        Log.d("QUESTION FETCHING::: ", "" + deckID);
        parentF = this;
        currentDeckId = deckID;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_questions_list, container, false);
        btnCreateQuestion = view.findViewById(R.id.btn_add_new_question);
        btnQuestionGoBack = (ImageView) view.findViewById(R.id.btn_questions_goBack);
        // set list
        grid = view.findViewById(R.id.question_list);
        RecyclerView.LayoutManager lm = new GridLayoutManager(MyApplication.getContext(),1, LinearLayoutManager.VERTICAL,false);
        grid.setLayoutManager(lm);
        //grid.setAdapter(new QuestionListAdapter(MyApplication.getContext(), questions, this));

        // create new question callback
        btnCreateQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("add new question", "add new question now");
                Log.d("add new deck", "add new deck now");
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.dialog_add_new_question, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(MyApplication.getMainActivity());
                alert.setTitle("Info");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                inputPitanje = alertLayout.findViewById(R.id.add_new_question_pitanje);
                inputOdgovor = alertLayout.findViewById(R.id.add_new_question_odgovor);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(false);
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                alert.setPositiveButton("Add Question", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Integer logiraniKorisnikId = Global.getLogiraniKorisnik().KorisnikId;
                        String Pitanje = inputPitanje.getText().toString();
                        String Odgovor = inputOdgovor.getText().toString();
                        com.pretzel.ben.ib130149_flashcard.Model.QuestionAPI.QuestionCreateVM model = new com.pretzel.ben.ib130149_flashcard.Model.QuestionAPI.QuestionCreateVM(deckID, Pitanje, Odgovor, false);
                        // try to add new deck and then refetchDecks in success callback
                        com.pretzel.ben.ib130149_flashcard.Model.QuestionAPI.AddNewQuestion(model, new MyRunnable<com.pretzel.ben.ib130149_flashcard.Model.QuestionAPI.QuestionCreateVM>() {
                            @Override
                            public void run(com.pretzel.ben.ib130149_flashcard.Model.QuestionAPI.QuestionCreateVM response) {
                                // call refetch now
                                if(response != null) {
                                    refetchQuestions();
                                } else {
                                    Toast.makeText(MyApplication.getContext(), "Greska prilikom dodavanja pitanja" +
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

        // fetch questions
        loggedKorisnikId = Global.getLogiraniKorisnik().KorisnikId;
        com.pretzel.ben.ib130149_flashcard.Model.QuestionAPI.GetPitanjaByDeck(deckID, new MyRunnable<List<com.pretzel.ben.ib130149_flashcard.Model.QuestionAPI.QuestionIndexVM>>() {
            @Override
            public void run(List<com.pretzel.ben.ib130149_flashcard.Model.QuestionAPI.QuestionIndexVM> results) {
                if (results.size() >= 0) {
                    Toast.makeText(MyApplication.getContext(), "Pitanja uspjesno dobavljena" +
                            "", Toast.LENGTH_SHORT).show();
                    //adapter = new DeckListAdapter(MyApplication.getContext(), results, parentF);
                    grid.setAdapter(new QuestionListAdapter(MyApplication.getContext(), results, parentF));
                } else {
                    Toast.makeText(MyApplication.getContext(), "Greska prilikom dobavljanja pitanja" +
                            "", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // goBack
        btnQuestionGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getMainActivity().onBackPressed();
            }
        });

        return view;
    }

    private void populateDummyQuestions() {
        int LABEL_MAX = 5;
        String questionTitle = "test title";
        String questionName = "test question";
        String questionAnswer = "test answer test answer test answer test answer test answer test answer. - test answer test answer \n test answer test answer";
        Boolean isFlipped = false;

        for(int i = 0; i < LABEL_MAX; i++) {
            QuestionAPI.QuestionIndexVM dumm = new QuestionAPI.QuestionIndexVM(questionTitle, questionName, questionAnswer, isFlipped);

            questions.add(dumm);
        }
    }

    public void refetchQuestions() {
        com.pretzel.ben.ib130149_flashcard.Model.QuestionAPI.GetPitanjaByDeck(currentDeckId, new MyRunnable<List<com.pretzel.ben.ib130149_flashcard.Model.QuestionAPI.QuestionIndexVM>>() {
            @Override
            public void run(List<com.pretzel.ben.ib130149_flashcard.Model.QuestionAPI.QuestionIndexVM> results) {
                if (results.size() >= 0) {
                    Toast.makeText(MyApplication.getContext(), "Pitanja uspjesno dobavljena" +
                            "", Toast.LENGTH_SHORT).show();
                    QuestionListAdapter adapter = new QuestionListAdapter(MyApplication.getContext(), results, parentF);
                    grid.setAdapter(adapter);
                } else {
                    Toast.makeText(MyApplication.getContext(), "Greska prilikom dobavljanja dekova" +
                            "", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
