package com.example.mydictionaria;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AddQuote extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_quote);

        Button addQuoteButton = findViewById(R.id.add_quote_button);

        addQuoteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }

        });
    }
}
