package com.example.mydictionaria;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class WordlistActivity extends AppCompatActivity {
    public static JSONObject jsonfiledata;
    private File file;
    private View.OnClickListener listener;
    private View.OnClickListener deletelistener;
    private View.OnClickListener descriptionlistener;
    private int currentNightMode;
    private boolean isNight;
    void getWords() throws JSONException {

        currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                isNight = false;
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                isNight = true;
                break;
        }


        JSONArray names = jsonfiledata.names();
        if(names == null) return;


        ArrayList<String> ar = new ArrayList<>();
        LinearLayout ln = findViewById(R.id.linearwordList);

        for(int i = 0; i < names.length(); i++)
        {
            ar.add(names.getString(i));
        }
        Collections.sort(ar);

        for(int i = 0; i <ar.size();  i++)
        {
            String s = ar.get(i);
            LinearLayout lhl = new LinearLayout(WordlistActivity.this);
            lhl.setOrientation(LinearLayout.HORIZONTAL);
            if(!isNight)
            {
                lhl.setBackgroundResource(R.drawable.card_drawable);
            }
            else
            {
                lhl.setBackgroundResource(R.drawable.card_drawable_night);
            }

            Button tv = new Button(WordlistActivity.this);
            tv.setText(s);
            tv.setAllCaps(false);
            tv.setBackground(null);
            tv.setOnClickListener(listener);
            tv.setWidth(860);

            Button description = new Button(WordlistActivity.this);
            description.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
            Drawable description_icon = ContextCompat.getDrawable(WordlistActivity.this, R.drawable.ic_baseline_description_24);
            description.setBackground(description_icon);
            description.setPadding(0, 0, 0, 0);
            description.setOnClickListener(descriptionlistener);

            Button delete = new Button(WordlistActivity.this);
            delete.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
            delete.setPadding(0, 0, 0, 0);
            Drawable delete_icon = ContextCompat.getDrawable(WordlistActivity.this, R.drawable.ic_baseline_delete_24);
            delete.setBackground(delete_icon);
            delete.setOnClickListener(deletelistener);


            lhl.addView(tv);
            lhl.addView(description);
            lhl.addView(delete);

            ln.addView(lhl);


        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Button b = (Button) v;
                try {
                    Toast.makeText(WordlistActivity.this, jsonfiledata.get(b.getText().toString()).toString(), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        deletelistener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Button b = (Button)v;

                LinearLayout delete = (LinearLayout)v.getParent();
                TextView data = (TextView)delete.getChildAt(0);

                jsonfiledata.remove(data.getText().toString());


                try {
                    FileWriter fl = new FileWriter(file);
                    BufferedWriter bw = new BufferedWriter(fl);

                    bw.write(jsonfiledata.toString());
                    bw.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }


                ViewParent vpc = delete.getParent();
                ((ViewGroup)vpc).removeView(delete);


            }
        };

        descriptionlistener = new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Button des = (Button)v;
                LinearLayout vp = (LinearLayout)des.getParent();
                TextView tv = (TextView) vp.getChildAt(0);


                Intent in = new Intent(WordlistActivity.this, WordDescriptionActivity.class);
                in.putExtra("word", tv.getText().toString());
                startActivity(in);
            }
        };

        jsonfiledata = MainActivity.jsonfiledata;

        file = new File(this.getFilesDir(), MainActivity.FILE_NAME);
        try {
            getWords();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
