package com.example.mydictionaria;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class WordDescriptionActivity extends AppCompatActivity {
    private int definitions_count = 0;
    private int current = 0;
    private String word;
    private JSONArray meanings = null;

    private void showDict() throws JSONException {
        JSONObject definitionandexampleandsynonym = (JSONObject) ((JSONArray)((JSONObject) meanings.get(current)).get("definitions")).get(0);

        String definition = definitionandexampleandsynonym.get("definition").toString();
        String example = definitionandexampleandsynonym.get("example").toString();
        String synonyms = definitionandexampleandsynonym.get("synonyms").toString();

        TextView definitionTextView = findViewById(R.id.meaningtextview);
        TextView exampleTextView = findViewById(R.id.exampletextview);
        TextView synonymTextView = findViewById(R.id.synonymtextview);

        definitionTextView.setText("Definiton:-\n\t" + definition);
        exampleTextView.setText("Usage:-\n\t" + example);
        synonymTextView.setText(synonyms);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_description);
        Intent intent = getIntent();


        String action = intent.getAction();
        String type = intent.getType();


        if(action.equals(Intent.ACTION_SEND) && "text/plain".equals(intent.getType()))
        {
            File folder = this.getFilesDir();
            DataAccess da = new DataAccess(folder);
            if(!da.containsWord(word))
            {
                String meaning = "";
                word = intent.getStringExtra(Intent.EXTRA_TEXT);
                boolean res = da.addWord(word, meaning);
                if(res)
                {
                    Toast.makeText(WordDescriptionActivity.this, "Word added", Toast.LENGTH_SHORT).show();
                }
            }

        }
        else{
            word = intent.getStringExtra("word");
        }


        TextView tv = findViewById(R.id.worddescriptionhead);

        tv.setText(word);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.dictionaryapi.dev/api/v2/entries/en_US/"+word;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray ja = new JSONArray(response);
                            meanings = (JSONArray) ja.getJSONObject(0).get("meanings");
                            definitions_count = meanings.length();

                            showDict();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tv.setText("That didn't work! Check your internet connection");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);







    }
}
