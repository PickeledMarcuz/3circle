package io.markryan.a3circle;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * Created by Mark on 07/08/2017.
 */

public class HttpHandler {

    public HttpHandler() {
    }


    private static final String TAG = HttpHandler.class.getSimpleName();

    public String makeServiceCall(String... urls) {

        String result = null;
        HttpURLConnection httpURLConnection;

        try {

            // Open URL defined in venueURL
            URL url = new URL(urls[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            // read the response
            InputStream inputStream = httpURLConnection.getInputStream();
            result = streamConvert(inputStream);


        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return result;
    }

    private String streamConvert(InputStream inputStream) {
        String result = "";
        InputStreamReader reader = new InputStreamReader(inputStream);
        try {
            int data = reader.read();
            while (data != -1) {
                result += (char) data;
                data = reader.read();
            }
            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}






