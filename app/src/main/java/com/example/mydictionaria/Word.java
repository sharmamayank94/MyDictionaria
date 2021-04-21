package com.example.mydictionaria;

import java.util.ArrayList;

public class Word {
    public String word;
    public String meaning;
    public ArrayList<String> synonyms;
    public String usage;

    public Word(String word, String meaning, ArrayList<String> synonyms, String usage) {
        this.word = word;
        this.meaning = meaning;
        this.synonyms = synonyms;
        this.usage = usage;
    }
}
