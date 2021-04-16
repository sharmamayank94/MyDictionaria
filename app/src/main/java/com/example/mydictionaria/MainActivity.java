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
    public static final String FILE_NAME = "word-data.txt";
    private static final int READ_EXTERNAL_STORAGE= 23;
    private File file;
    private FileReader fileReader;
    private FileWriter fileWriter;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    public static JSONObject jsonfiledata;
    private View.OnClickListener listener;


    private void initJson()
    {
        try{

            StringBuffer str = new StringBuffer();
            fileReader = new FileReader(file.getAbsolutePath());
            bufferedReader = new BufferedReader(fileReader);

            String response = "";
            String line = "";

            while((line = bufferedReader.readLine()) != null)
            {
                str.append(line);
            }
            response = str.toString();

            bufferedReader.close();
            jsonfiledata = new JSONObject(response);
        } catch(IOException | JSONException e)
        {
            Toast.makeText(MainActivity.this, e.toString() , Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    private boolean isWordExist(String word)
    {
        return jsonfiledata.has(word);
    }

    private void saveWord(String word, String meaning)
    {
        try{

            //JSONObject jsonmeaning = new JSONObject(meaning);
            jsonfiledata.put(word, meaning);

            fileWriter = new FileWriter(file.getAbsolutePath());
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(jsonfiledata.toString());
            bufferedWriter.close();

        }catch(JSONException | IOException e)
        {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        fileReader = null;
        fileWriter = null;
        bufferedReader = null;
        bufferedWriter = null;

        Button addword = findViewById(R.id.addwordbutton);
        Button getwordbutton = findViewById(R.id.mywordlist);
        file = new File(this.getFilesDir(), FILE_NAME);



        if(!file.exists())
        {
            try{
                file.createNewFile();
                fileWriter = new FileWriter(file.getAbsoluteFile());
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write("{}");
                bufferedWriter.close();
            } catch(IOException e){
                System.out.println("This is herre9fjowf");
                e.printStackTrace();
            }
        }
        initJson();
        addword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextInputEditText word = findViewById(R.id.addwordtext);
                TextInputEditText meaning = findViewById(R.id.addmeaningtext);

                boolean isword = isWordExist(word.getText().toString());
                if(isword) return;

                saveWord(word.getText().toString(), meaning.getText().toString());
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