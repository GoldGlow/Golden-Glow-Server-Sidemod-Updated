package com.goldenglow.common.guis.data;

import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.Reference;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import noppes.npcs.api.wrapper.gui.CustomGuiTexturedRectWrapper;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.io.*;
import java.util.Objects;

public class TutorialsManager {
    ArrayList<TutorialData> tutorials=new ArrayList<TutorialData>();
    File dir;

    public void init(){
        //this.tutorials.add(TutorialData.createMarksTutorial());
        dir=new File(Reference.tutorialsDir);
        if(!dir.exists()){
            if (!dir.getParentFile().exists())
                dir.getParentFile().mkdirs();
            dir.mkdirs();
        }
        else{
            this.loadTutorials();
        }
    }

    public void loadTutorials(){
        GGLogger.info("Loading Tutorials...");
        this.tutorials.clear();
        try {
            for (File f : Objects.requireNonNull(dir.listFiles())) {
                if (f.getName().endsWith(".json")) {
                    loadTutorial(f.getName().replace(".json", ""));
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTutorial(String tutorialName) throws IOException{
        InputStream iStream = new FileInputStream(new File(dir, tutorialName+".json"));
        JsonObject json = new JsonParser().parse(new InputStreamReader(iStream, StandardCharsets.UTF_8)).getAsJsonObject();
        String name=json.get("name").getAsString();
        String description=json.get("description").getAsString();
        TutorialData tutorialData=new TutorialData(name, description);
        JsonArray pages=json.getAsJsonArray("pages");
        int index=0;
        for(JsonElement element:pages){
            JsonObject object=element.getAsJsonObject();
            String texture=object.get("imageTexture").getAsString();
            int x=object.get("imageX").getAsInt();
            int y=object.get("imageY").getAsInt();
            CustomGuiTexturedRectWrapper texturedRect=new CustomGuiTexturedRectWrapper(100, texture, 64, 48, 128, 128, x, y);
            String text=object.get("text").getAsString();
            index++;
            tutorialData.addPage(new TutorialsInfo(texturedRect, text, index));
        }
        this.tutorials.add(tutorialData);
    }

    public TutorialData getTutorial(String name){
        for(TutorialData tutorial: tutorials){
            if(tutorial.getName().equals(name)){
                return tutorial;
            }
        }
        return null;
    }

    public String[] getTutorialList(){
        String[] tutorials=new String[this.tutorials.size()];
        for(int i=0;i<this.tutorials.size();i++){
            tutorials[i]=this.tutorials.get(i).getName();
        }
        return tutorials;
    }
}
