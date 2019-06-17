package com.goldenglow.common.handlers;

import com.goldenglow.GoldenGlow;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

public class DataHandler extends Thread {

    public void sendData(String playerName, int dex, int badges, String time) {
        String payload = "{ \"badges\":"+badges+", \"dex\":"+dex+", \"time\":\""+time+"\" }";
        HttpClient client = HttpClientBuilder.create().build();
        HttpPut put = new HttpPut("https://oo-userdata.herokuapp.com/"+playerName);
        put.setHeader("Auth", GoldenGlow.configHandler.oAuthToken);
        put.setEntity(new StringEntity(payload, ContentType.APPLICATION_JSON));
        try {
            client.execute(put);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
