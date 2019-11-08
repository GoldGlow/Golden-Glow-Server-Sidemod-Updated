package com.goldenglow.http;

import com.goldenglow.common.util.Reference;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class OOStatsServer implements Runnable {

    Javalin server;

    public void run() {
        server = Javalin.create().start(7000);

        server.get("/badges/:uuid", ctx -> sendStat("badges", ctx.pathParam("uuid"), ctx));

        server.get("/dex/:uuid", ctx -> sendStat("dex", ctx.pathParam("uuid"), ctx));

        server.get("/time/:uuid", ctx -> sendStat("time", ctx.pathParam("uuid"), ctx));
    }

    void sendStat(String stat, String uuid, Context ctx) {
        File f = new File(Reference.statsDir, uuid+".json");
        if(!f.exists())
            ctx.result("");
        else {
            try {
                InputStream iStream = new FileInputStream(f);
                JsonObject json = new JsonParser().parse(new InputStreamReader(iStream, StandardCharsets.UTF_8)).getAsJsonObject();
                ctx.result(json.get(stat).getAsString());
            }
            catch (Exception e) {
                ctx.result("");
            }
        }
    }

}
