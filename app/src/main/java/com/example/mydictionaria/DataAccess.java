package com.example.mydictionaria;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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

//            JSONObject obj = new JSONObject();
//            obj.put("words", words_object);
//            obj.put("practice", practice);
//            obj.put("quotes", quotes);

            FileWriter fr = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fr);
            bw.write(jsondata.toString());
            bw.close();
            return true;
        }
        catch(JSONException | IOException e)
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

            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(jsondata.toString());
            bw.close();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }
}
