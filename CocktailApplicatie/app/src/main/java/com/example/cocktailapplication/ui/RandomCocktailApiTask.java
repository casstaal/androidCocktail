package com.example.cocktailapplication.ui;

import android.os.AsyncTask;
import android.util.Log;

import com.example.cocktailapplication.entities.Cocktail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class RandomCocktailApiTask extends AsyncTask<String, Void, ArrayList<Cocktail>> {

    private final String LOG_TAG = this.getClass().getSimpleName();

    private ArrayList<Cocktail> cocktails = new ArrayList<>();
    private OnRandomCocktailListener listener;

    private final String JSON_RESULTS = "drinks";


    public RandomCocktailApiTask(OnRandomCocktailListener listener) {
        super();
        Log.i(LOG_TAG, "Constructor");
        this.listener = listener;
    }

    @Override
    protected ArrayList<Cocktail> doInBackground(String... params) {

        String urlString = null;
        ArrayList<Cocktail> cocktails = new ArrayList<>();
        if(params[0] != null) {
            urlString = params[0];
        }
        Log.i(LOG_TAG, "doInBackground " + urlString);

        InputStream inputStream = null;
        int responsCode = -1;
        String response = null;

        try{
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();

            if (!(urlConnection instanceof HttpURLConnection)) {
                // Url
                return null;
            }

            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setInstanceFollowRedirects(true);
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            responsCode = httpConnection.getResponseCode();

            if (responsCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpConnection.getInputStream();
                response = getStringFromInputStream(inputStream);
                Log.d(LOG_TAG, "response = " + response);

                cocktails = jsonParseResponse(response);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "doInBackground MalformedURLEx " + e.getLocalizedMessage());
            return null;
        } catch (IOException e) {
            Log.e("TAG", "doInBackground IOException " + e.getLocalizedMessage());
            return null;
        }
        return cocktails;
    }

    @Override
    protected void onPostExecute(ArrayList<Cocktail> cocktails) {
        super.onPostExecute(cocktails);
        Log.d(LOG_TAG, "onPostExecute");
        // people terugsturen naar aanroepende activity
        listener.onCocktailAvailable(cocktails);
    }

    public interface OnRandomCocktailListener {
        public void onCocktailAvailable(ArrayList<Cocktail> cocktails);
    }

    private ArrayList<Cocktail> jsonParseResponse(String response) {
        Log.d(LOG_TAG, "jsonParseResponse");
        ArrayList<Cocktail> cocktailArrayList = new ArrayList<>();
//        Log.d(LOG_TAG, "jsonParseResponse test 1");
        try {
            Log.d(LOG_TAG, "jsonParseResponse 2");
            JSONObject jsonObject = new JSONObject(response);
            Log.d(LOG_TAG, "jsonParseResponse test 3");

            JSONArray results = jsonObject.getJSONArray(JSON_RESULTS);

            Log.d(LOG_TAG, "jsonParseResponse test 4");


            for(int i = 0; i < results.length(); i++) {
                // Stap door cocktails heen
                JSONObject cocktail = (JSONObject) results.get(i);

                String name = cocktail.getString("strDrink");
                String category = cocktail.getString("strCategory");
                String url = cocktail.getString("strDrinkThumb");
                String alcoholic = cocktail.getString("strAlcoholic");
                String stringId = cocktail.getString("idDrink");

                int id = Integer.parseInt(stringId);

                cocktailArrayList.add(new Cocktail(name, category, url, alcoholic, id));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        return cocktailArrayList;
    }

    private static String getStringFromInputStream(InputStream inputStream){
        Log.i("RandomCocktailApiTask", "getStringFromInputStream");
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
