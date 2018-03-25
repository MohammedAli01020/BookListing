package com.example.mohamed.booklisting.data;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public static ArrayList<Book> fetchBookData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return extractBooks(jsonResponse);
    }

    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws UnsupportedEncodingException {
        StringBuilder jsonResponse = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        try {
            String line = reader.readLine();
            while (line != null) {
                jsonResponse.append(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonResponse.toString();
    }

    private static ArrayList<Book> extractBooks(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        ArrayList<Book> books = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray items = baseJsonResponse.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {

                JSONObject currentItem = items.getJSONObject(i);
                JSONObject volumeInfo = currentItem.getJSONObject("volumeInfo");
                JSONObject bookImageLinks = volumeInfo.getJSONObject("imageLinks");
                JSONArray authors = volumeInfo.getJSONArray("authors");

                String image = bookImageLinks.getString("smallThumbnail");
                String title = volumeInfo.getString("title");
                String author = "author: " + authors.getString(0);
                String previewLink = volumeInfo.getString("previewLink");

                books.add(new Book(image, title, author, previewLink));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return books;
    }
}

