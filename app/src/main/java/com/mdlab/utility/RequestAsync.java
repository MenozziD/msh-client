package com.mdlab.utility;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class RequestAsync extends AsyncTask<String,String,String>  {

    private String urlString;
    private String params;
    private String response;
    private String cookie;
    private String session;

    public RequestAsync (String urlString, String params) {
        this.urlString = urlString;
        this.params = params;
        this.session = null;
    }

    public RequestAsync (String urlString, String params, String session) {
        this.urlString = urlString;
        this.params = params;
        this.session = session;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(30000);
            conn.setConnectTimeout(30000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            if (this.session != null){
                conn.setRequestProperty("Cookie", session);
            }
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(os, StandardCharsets.UTF_8));
            writer.write(params);
            writer.flush();
            writer.close();
            os.close();
            System.out.println("REQUEST URL: " + urlString);
            System.out.println("PAYLOAD: " + params);
            System.out.println("---------------------------------------------------------------------------------------------------------");
            int responseCode=conn.getResponseCode(); // To Check for 200
            System.out.println("RESPONSE CODE: " + responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader in=new BufferedReader( new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = in.readLine()) != null) {
                    sb.append(line);
                }
                in.close();
                response = sb.toString();
                cookie = conn.getHeaderField("Set-Cookie");
                System.out.println("COOKIE: " + cookie);
                System.out.println("PAYLOAD: " + response);
            } else {
                response = Integer.toString(responseCode);
            }
            System.out.println("---------------------------------------------------------------------------------------------------------");
        }
        catch(Exception e){
            response = "Exception: " + e.getMessage();
        }
        return response;
    }

    public String getResponse(){
        return response;
    }

    public String getCookie(){
        return cookie;
    }
}
