package com.TNF;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class Connection {
    private URL url;
    private URLConnection urlcon;
    public Connection(URL url) {
        this.url = url;
    }

    public URLConnection createConnection() throws IOException {
        urlcon = url.openConnection();
        return urlcon;
    }

    public List<String> readURL() throws IOException {
        List<String> l = new ArrayList<String>();
        InputStreamReader inputStream = new InputStreamReader(
                urlcon.getInputStream());
        BufferedReader reader = new BufferedReader(inputStream);
        for(String s;(s = reader.readLine()) != null; ) {
            l.add(s);
        }
        inputStream.close();
        reader.close();
        return l;
    }
}