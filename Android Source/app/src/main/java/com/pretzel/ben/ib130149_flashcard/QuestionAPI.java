package com.pretzel.ben.ib130149_flashcard;

import java.io.Serializable;

public class QuestionAPI {

    //deck indexvm
    public static class QuestionIndexVM implements Serializable {
        public int DeckID;
        public int QuestionID;
        public String DeckName;
        public String QuestionName;
        public String Question;
        public String Answer;
        public Boolean isFlipped;

        public QuestionIndexVM(String Name, String Question, String Answer, Boolean isFlipped) {
            this.QuestionName = Name;
            this.Question = Question;
            this.Answer = Answer;
            this.isFlipped = isFlipped;
        }
    }
    // deck createvm

    //deck model
    public static class Question implements Serializable {
        public Integer QuestionID;
        public String QuestionName;
        public String Question;
        public String Answer;
        public String Deck;
        public Boolean isFlipped;
    }
}
