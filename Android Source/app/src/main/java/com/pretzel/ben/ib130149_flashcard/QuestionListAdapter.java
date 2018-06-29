package com.pretzel.ben.ib130149_flashcard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pretzel.ben.ib130149_flashcard.Model.DeckAPI;
import com.pretzel.ben.ib130149_flashcard.Model.QuestionAPI;
import com.pretzel.ben.ib130149_flashcard.helper.Global;
import com.pretzel.ben.ib130149_flashcard.helper.MyApplication;
import com.pretzel.ben.ib130149_flashcard.helper.MyRunnable;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionListAdapter extends RecyclerView.Adapter<QuestionListAdapter.MyViewHolder> {

    public Context context;
    public LayoutInflater thisInflater;
    public List<com.pretzel.ben.ib130149_flashcard.Model.QuestionAPI.QuestionIndexVM> questions;
    private Fragment parentFragment;
    private DialogInterface intrfs;
    private Integer currentDeckId;

    public QuestionListAdapter(Context context, List<com.pretzel.ben.ib130149_flashcard.Model.QuestionAPI.QuestionIndexVM> items, Fragment parentFragment) {
        this.context = context;
        this.thisInflater = LayoutInflater.from(context);
        this.questions = items;
        this.parentFragment = parentFragment;
    }

   class MyViewHolder extends RecyclerView.ViewHolder{
       EasyFlipView flipView;

       MyViewHolder(View view) {
           super(view);
           flipView = view.findViewById(R.id.flipView);
       }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (holder.flipView.getCurrentFlipState() == EasyFlipView.FlipState.FRONT_SIDE && questions.get(
                position).isFlipped) {
            holder.flipView.setFlipDuration(0);
            holder.flipView.flipTheView();
        } else if (holder.flipView.getCurrentFlipState() == EasyFlipView.FlipState.BACK_SIDE
                && !questions.get(position).isFlipped) {
            holder.flipView.setFlipDuration(0);
            holder.flipView.flipTheView();
        }

        // set front and back
        TextView cardQuestion = holder.flipView.findViewById(R.id.question_question);
        cardQuestion.setText(questions.get(position).Pitanje);

        TextView cardAnswer = holder.flipView.findViewById(R.id.question_answer);
        cardAnswer.setText(questions.get(position).Odgovor);

        // set random pastel color on card
        final Random mRandom = new Random(System.currentTimeMillis());
        //holder.flipView.setBackgroundColor(generateRandomColor(mRandom));
        int bg = generateRandomColor(mRandom);
        holder.flipView.findViewById(R.id.question_question).setBackgroundColor(bg);
        holder.flipView.findViewById(R.id.question_answer).setBackgroundColor(bg);

        holder.flipView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questions.get(position).isFlipped = !questions.get(position).isFlipped;
                holder.flipView.setFlipDuration(700);
                holder.flipView.flipTheView();
            }
        });

        holder.flipView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String nesto = String.valueOf(questions.get(position).PitanjeId);
                Log.d("long press", "LONG PRESS " + nesto);

                AlertDialog.Builder alert = new AlertDialog.Builder(view.getRootView().getContext());
                alert.setTitle("ALERT !");
                alert.setMessage("Are you sure you want to delete this item :: ?" + " " + questions.get(position).PitanjeId);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // call delete method now and pass id
                        intrfs = dialogInterface;
                        currentDeckId = questions.get(position).DeckId;
                        Integer pitanjeId = questions.get(position).PitanjeId;

                        QuestionAPI.DeletePitanje(currentDeckId, pitanjeId, new MyRunnable<QuestionAPI.QuestionCreateVM>() {
                            @Override
                            public void run(QuestionAPI.QuestionCreateVM response) {
                                //DeckListFragment.refetchDecks();
                                refetchQuestions();
                                intrfs.dismiss();
                            }
                        });
                    }
                });

                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                alert.show();

                return true;
            }
        });
    }

    public int generateRandomColor(Random mRandom) {
        // This is the base color which will be mixed with the generated one
        final int baseColor = Color.WHITE;

        final int baseRed = Color.red(baseColor);
        final int baseGreen = Color.green(baseColor);
        final int baseBlue = Color.blue(baseColor);

        final int red = (baseRed + mRandom.nextInt(256)) / 2;
        final int green = (baseGreen + mRandom.nextInt(256)) / 2;
        final int blue = (baseBlue + mRandom.nextInt(256)) / 2;

        return Color.rgb(red, green, blue);
    }

    public void refetchQuestions() {
        QuestionAPI.GetPitanjaByDeck(currentDeckId, new MyRunnable<List<QuestionAPI.QuestionIndexVM>>() {
            @Override
            public void run(List<QuestionAPI.QuestionIndexVM> results) {
                if (results.size() >= 0) {
                    Toast.makeText(MyApplication.getContext(), "Dekovi uspjesno dobavljeni" +
                            "", Toast.LENGTH_SHORT).show();
                    questions = results;
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(MyApplication.getContext(), "Greska prilikom dobavljanja dekova" +
                            "", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

}