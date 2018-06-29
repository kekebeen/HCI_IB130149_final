package com.pretzel.ben.ib130149_flashcard;

import java.io.Serializable;

public class DeckAPI {

    //deck indexvm
    public static class DeckIndexVM implements Serializable {
        public int DeckID;
        public String DeckName;
        public String CategoryName;

        public DeckIndexVM(String DeckName, String CategoryName) {
            this.DeckName = DeckName;
            this.CategoryName = CategoryName;
        }
    }
    // deck createvm

    //deck model
    public static class Deck implements Serializable {
        public Integer DeckID;
        public String DeckName;
    }
}
