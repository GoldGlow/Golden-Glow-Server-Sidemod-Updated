package com.goldenglow.common.inventory;

import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.Reference;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by JeanMarc on 6/25/2019.
 */
public class CustomInventoryHandler {
    public List<CustomInventoryData> inventories=new ArrayList<CustomInventoryData>();

    File dir;

    public void init() {
        /*dir = new File(Reference.inventoryDir);
        if(!dir.exists()) {
            if (!dir.getParentFile().exists())
                dir.getParentFile().mkdirs();
            try {
                dir.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            this.loadInventories();*/
    }

    public void loadInventories(){
        GGLogger.info("Loading Inventories...");
        try {
            for (File f : Objects.requireNonNull(dir.listFiles())) {
                if (f.getName().endsWith(".json")) {
                    loadInventory(f.getName().replace(".json", ""));
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadInventory(String inventoryName) throws IOException{
        InputStream iStream = new FileInputStream(new File(dir, inventoryName+".json"));
        JsonObject json = new JsonParser().parse(new InputStreamReader(iStream, StandardCharsets.UTF_8)).getAsJsonObject();
        String name = json.get("displayName").getAsString();
        int rows=json.get("rows").getAsInt();
    }
}
