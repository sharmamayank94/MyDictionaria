package com.example.mydictionaria;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;


public class PracticeActivity extends AppCompatActivity {
    private TextView question_view;
    private Button submit_next_view;
    private EditText answer_view;
    private String question;
    private String answer;
    private DataAccess dataAccess;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void askQuestion(int attempt ){
        if(attempt > 4)
        {
            question_view.setText("something went wrong");
            return;
        }


        String word = dataAccess.getPracticeWord();

        if(word.equals(""))
        {
            question_view.setText("You have no word saved to play with.");
            return;
        }


        dataAccess.getWordAPI(getApplicationContext(), word, new NetworkController() {
            @Override
            public void onSuccess(Word word) {
                if(word==null)
                {
                    askQuestion(attempt+1);
                    return;
                }
                question_view.setText(word.meaning);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice_layout);

        File folder = this.getFilesDir();
        dataAccess = new DataAccess(folder);

        question_view = findViewById(R.id.practice_sentence);
        answer_view = findViewById(R.id.practice_answer);
        submit_next_view = findViewById(R.id.practice_submit);

        askQuestion(0);

    }
}
