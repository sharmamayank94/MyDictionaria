package com.example.mydictionaria;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {


    private View.OnClickListener listener;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        try{
//
//            File folder = new File(this.getFilesDir(), "word-data.txt");
//            folder.delete();
//            folder.createNewFile();
//            FileWriter fw = new FileWriter(folder);
//            BufferedWriter bw = new BufferedWriter(fw);
//            bw.write("\n" +
//                    "{\n" +
//                    "\twords: {\n" +
//                    "\t\tperil:[\"\"],meager:[\"\"],convulsive:[\"\"],prolonged:[\"\"],simile:[\"\"],fatuous:[\"\"],incorrigible:[\"\"],stifling:[\"\"],vociferous:[\"\"],veneration:[\"\"],erudite:[\"\"],persiflage:[\"\"],petulant:[\"\"],yearning:[\"\"],estrangement:[\"\"],avow:[\"\"],Badegast:[\"\"],suprapersonal:[\"\"],servitors:[\"\"],there:[\"\"],shinichi:[\"\"],ignorant:[\"\"],melancholy:[\"\"],unsullied:[\"\"],horrendous:[\"\"],astray:[\"\"],instigator:[\"\"],seared:[\"\"],contrition:[\"\"],conan:[\"\"]\n" +
//                    "\t},\n" +
//                    "\tpractice: [\"peril\",\"meager\",\"convulsive\",\"prolonged\",\"simile\",\"fatuous\",\"incorrigible\",\"stifling\",\"vociferous\",\"veneration\",\"erudite\",\"persiflage\",\"petulant\",\"yearning\",\"estrangement\",\"avow\",\"Badegast\",\"suprapersonal\",\"servitors\",\"there\",\"shinichi\",\"ignorant\",\"melancholy\",\"unsullied\",\"horrendous\",\"astray\",\"instigator\",\"seared\",\"contrition\",\"conan\"],\n" +
//                    "\tquotes: []\n" +
//                    "}\n" +
//                    "\n");
//            bw.close();
//        }catch(IOException e)
//        {
//            e.printStackTrace();
//        }


        Button addword = findViewById(R.id.addwordbutton);
        Button getwordbutton = findViewById(R.id.mywordlist);


        addword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextInputEditText word = findViewById(R.id.addwordtext);
                TextInputEditText meaning = findViewById(R.id.addmeaningtext);

                File folder = MainActivity.this.getFilesDir();
                DataAccess da = new DataAccess(folder);

                boolean isword = da.containsWord(word.getText().toString());

                if(isword){
                    Toast.makeText(MainActivity.this, "Word already in the list", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean wordadded = da.addWord(word.getText().toString(), meaning.getText().toString());
                if(wordadded)
                {
                    word.setText("");
                    meaning.setText("");
                    Toast.makeText(MainActivity.this, "Word Added", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Some error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getwordbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, WordlistActivity.class);
                    startActivity(intent);
            }
        });

    }
}