package com.example.mydictionaria;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class DataAccess {
    private static JSONObject jsondata;
    private static File file;
    public static final String FILE_NAME = "word-data.txt";
    private Word currentWord = null;

    private void createFile()
    {
        try{
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("{\n" +
                    "\twords: {\n" +
                    "\n" +
                    "\t},\n" +
                    "\tpractice: [],\n" +
                    "\tquotes: []\n" +
                    "}");
            bufferedWriter.close();
        } catch(IOException e){
            System.out.println("This is herre9fjowf");
            e.printStackTrace();
        }
    }

    public DataAccess(File folder)
    {
        ArrayList<String> words = new ArrayList<>();
        file = new File(folder, FILE_NAME);


        if(!file.exists()) {
            createFile();
        }


        try{

            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            StringBuffer response =  new StringBuffer();
            String line = "";
            while((line = br.readLine()) != null)
            {
                response.append(line);
            }
            br.close();
            String text_data = response.toString();

            jsondata = new JSONObject(text_data);
            System.out.println(jsondata.toString());
        }
        catch(IOException | JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void getWordAPI(Context app, String word, final NetworkController callback)
    {
        RequestQueue queue = Volley.newRequestQueue(app);
        String url = "https://api.dictionaryapi.dev/api/v2/entries/en_US/"+word;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            //Parsing data received into Word class object if data is not received word class object sends back null
                            JSONArray jsonword = new JSONArray(response);
                            JSONArray meanings = (JSONArray) ((JSONObject)jsonword.get(0)).get("meanings");
                            JSONObject definition_object =(JSONObject) ((JSONArray)((JSONObject)meanings.get(0)).get("definitions")).get(0);

                            String definition = definition_object.get("definition").toString();
                            String usage = "";
                            try{
                                 usage = definition_object.get("example").toString();
                            }catch(JSONException jo)
                            {

                            }
                            ArrayList<String> synonyms = new ArrayList<>();
                            if(definition_object.has("synonyms")){
                                JSONArray synonyms_array = (JSONArray) definition_object.get("synonyms");
                                for(int i = 0; i < synonyms_array.length(); i++)
                                {
                                    synonyms.add(synonyms_array.get(i).toString());
                                }
                            }

                            String word = ((JSONObject)jsonword.get(0)).getString("word");

                            currentWord = new Word(word, definition, synonyms, usage);
                            callback.onSuccess(currentWord);

                        }catch(JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onSuccess(null);
            }
        });


        queue.add(stringRequest);
    }

    public ArrayList<String> getWords(){
        ArrayList<String> result = new ArrayList<>();
        try{
            JSONObject wordobjects = jsondata.getJSONObject("words");
            JSONArray words = wordobjects.names();

            if(words == null) return result;

            for(int i = 0; i < words.length(); i++)
            {
                result.add(words.get(i).toString());
            }

        }catch(JSONException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public boolean containsWord(String word)
    {

        try {

            JSONObject word_meanings = jsondata.getJSONObject("words");
            return word_meanings.has(word);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }


    }

    public boolean addWord(String word, String meaning)
    {
        try{
            JSONArray practice = (JSONArray)jsondata.get("practice");
            practice.put(word);

            JSONObject words_object = (JSONObject) jsondata.get("words");

            JSONArray ja = new JSONArray();
            ja.put(meaning);
            words_object.put(word, ja);


            updateFile();
            return true;
        }
        catch(JSONException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void deleteWord(String word) {
        JSONObject json = null;
        try {
            json = (JSONObject) jsondata.get("words");
            json.remove(word);

            int ind = -1;
            JSONArray ja = (JSONArray)jsondata.get("practice");

            for(int i = 0; i < ja.length(); i++)
            {
                if(ja.get(i).equals(word)){
                    ind = i;
                    break;
                }
            }
            if(ind > -1)
            {
                ja.remove(ind);
            }

            updateFile();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    void initPractice()
    {
        try{
            JSONObject jo = (JSONObject)jsondata.get("words");
            JSONArray practice =(JSONArray) jsondata.get("practice");
            JSONArray temp = jo.names();
            if(temp == null) return;
            for(int i = 0; i < temp.length(); i++)
            {
                practice.put(temp.get(i));
            }
            updateFile();
        }catch(JSONException e)
        {
            e.printStackTrace();
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getPracticeWord() {
        try {
            JSONArray practice_words = (JSONArray) jsondata.get("practice");
            if(practice_words.length() == 0)
            {
                initPractice();
                practice_words = (JSONArray) jsondata.get("practice");
                if(practice_words.length() == 0){
                    return "";
                }
            }

            String word = practice_words.get(0).toString();
            practice_words.remove(0);

            updateFile();

            return word;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updateFile() {
        try{
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(jsondata.toString());
            bw.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }

    }
}
