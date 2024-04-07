package com.example.test_log;

import android.content.Context;
import android.content.res.AssetManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JSONReader {
    public static String loadJSONFromAsset(Context context, String filename) {
        String json;
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public static JSONObject loadJSONObjectFromAsset(Context context, String filename) {
        try {
            return new JSONObject(loadJSONFromAsset(context, filename));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

//    public static JSONArray loadJSONArrayFromAsset(Context context, String filename) {
//        try {
//            return new JSONArray(loadJSONFromAsset(context, filename));
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}