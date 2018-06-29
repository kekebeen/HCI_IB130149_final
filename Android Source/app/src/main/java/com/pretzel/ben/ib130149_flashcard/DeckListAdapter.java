package com.pretzel.ben.ib130149_flashcard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pretzel.ben.ib130149_flashcard.Model.DeckAPI;
import com.pretzel.ben.ib130149_flashcard.helper.Global;
import com.pretzel.ben.ib130149_flashcard.helper.MyApplication;
import com.pretzel.ben.ib130149_flashcard.helper.MyRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeckListAdapter extends BaseAdapter {

    private String[] imageLabels;
    private LinearLayout item;
    public Context context;
    public LayoutInflater thisInflater;
    public List<DeckAPI.DeckVM> itemLabels;
    private Fragment parentFragment;

    public Integer[] itemImages = {
            R.drawable.calendar,
            R.drawable.family_time,
            R.drawable.friends,
            R.drawable.lovely_time,
            R.drawable.me_time,
            R.drawable.team_time
    };

    private DialogInterface intrfs;


    public DeckListAdapter(Context con, List<DeckAPI.DeckVM> decks, Fragment parentFragment) {
        this.context = con;
        this.thisInflater = LayoutInflater.from(con);
        this.itemLabels = decks;
        this.parentFragment = parentFragment;
    }


    @Override
    public int getCount() {
        return itemLabels.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if(convertView == null) {
            convertView = thisInflater.inflate( R.layout.deck_grid_item, parent, false );

            // find grid item components
            TextView textHeading = convertView.findViewById(R.id.item_title);
            ImageView thumbnailImage = convertView.findViewById(R.id.item_img);
            final Button btnItem = convertView.findViewById(R.id.btnItem);
            // set items
            int randomPosition = getRandom();
            //String categoryName = itemLabels.get(position).CategoryName;
            String categoryName = itemLabels.get(position).Kategorija;
            textHeading.setText( categoryName );
            thumbnailImage.setImageResource( itemImages[randomPosition] );
            // delete when api is ready (use generated id instead of list position)
            //final int itemID = itemLabels.get(position).DeckID;
            final int itemID = itemLabels.get(position).DeckId;
            //btnItem.setTag(itemLabels.get(position).DeckID);
            btnItem.setTag(itemLabels.get(position).DeckId);

            // set listener
            item = convertView.findViewById(R.id.item_card_parent);

            item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    // get position
                    final int pos = (Integer) btnItem.getTag();
                    // open dismiss
                    AlertDialog.Builder alert = new AlertDialog.Builder(view.getRootView().getContext());
                    alert.setTitle("ALERT !");
                    alert.setMessage("Are you sure you want to delete this item :: ?" + " " + itemID);
                    alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // call delete method now and pass id
                            intrfs = dialogInterface;
                            DeckAPI.DeleteDeckById(itemID, new MyRunnable<DeckAPI.DeckVM>() {
                                @Override
                                public void run(DeckAPI.DeckVM response) {
                                    //DeckListFragment.refetchDecks();
                                    refetchDecks();
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

                    return false;
                }
            });

            // on single click go to questions fragment and fetch questions by DeckID
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // get id
                    // get instance of deckListFragment

                    /*QuestionsFragment fragment = new QuestionsFragment();
                    Bundle arguments = new Bundle();
                    arguments.putInt( "DeckID" , itemID);
                    fragment.setArguments(arguments);

                    FragmentTransaction ft = ((Activity) view.getParent()).getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_deck_list, fragment);
                    ft.commit();*/
                    if(parentFragment instanceof DeckListFragment ) {
                        ((DeckListFragment) parentFragment).navigateToQuestionsFragment(itemID);
                    }
                    
                }
            });

        }

        return convertView;
    }

    public int getRandom() {
        final int min = 0;
        final int max = itemLabels.size() - 1;
        Random r = new Random();
        final int random = r.nextInt((max - min) + 1) + min;

        return random;

    }

    public void refetchDecks() {
        int loggedKorisnikId = Global.getLogiraniKorisnik().KorisnikId;
        com.pretzel.ben.ib130149_flashcard.Model.DeckAPI.GetDekovi(loggedKorisnikId, new MyRunnable<List<com.pretzel.ben.ib130149_flashcard.Model.DeckAPI.DeckVM>>() {
            @Override
            public void run(List<com.pretzel.ben.ib130149_flashcard.Model.DeckAPI.DeckVM> results) {
                if (results.size() >= 0) {
                    Toast.makeText(MyApplication.getContext(), "Dekovi uspjesno dobavljeni" +
                            "", Toast.LENGTH_SHORT).show();
                   itemLabels = results;
                   notifyDataSetChanged();
                } else {
                    Toast.makeText(MyApplication.getContext(), "Greska prilikom dobavljanja dekova" +
                            "", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
