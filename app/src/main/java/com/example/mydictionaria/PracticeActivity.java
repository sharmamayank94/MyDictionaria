package com.example.mydictionaria;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
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
    private View.OnClickListener next;
    private View.OnClickListener submit;

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
                question = word.meaning;
                answer = word.word;

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


        submit = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(answer_view.getText().toString().equalsIgnoreCase(answer))
                {
                    TextView result = findViewById(R.id.practice_result);
                    result.setTextColor(Color.parseColor("#55FF88"));
                    result.setText("Right answer");
                }
                else
                {
                    TextView result = findViewById(R.id.practice_result);
                    result.setTextColor(Color.parseColor("#ff55bb"));
                    result.setText("Wrong answer!\nCorrect answer is: " + answer);
                }
                answer_view.setText("");
                submit_next_view.setText("Next");
                submit_next_view.setOnClickListener(next);

            }
        };

        next = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                askQuestion(0);
                submit_next_view.setText("Submit");
                submit_next_view.setOnClickListener(submit);
                TextView result = findViewById(R.id.practice_result);
                result.setText("");
            }
        };
        submit_next_view.setOnClickListener(submit);
        askQuestion(0);



    }
}
