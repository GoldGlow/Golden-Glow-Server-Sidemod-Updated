package com.goldenglow.common.music;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.util.Reference;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import noppes.npcs.client.controllers.MusicController;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by JeanMarc on 5/9/2019.
 */
public class SongManager {
    File configFile=new File(Reference.configDir, "songs/config.cfg");
    File customThemes=new File(Reference.configDir, "songs/playerThemes.json");
    public String wildBattleSong;
    public String trainerBattleSong;
    public String victorySong;
    public String encounterSong;
    public String levelUpSound;
    public String evolutionSong;
    public ArrayList<Song> songs;
    public HashMap<String, String> playerThemes=new HashMap();

    public SongManager(){
    }

    public void init() {
        GoldenGlow.logger.info("Loading song config!");
        if (!configFile.exists()){
            if(!configFile.getParentFile().exists())
                configFile.getParentFile().mkdirs();
            try {
                createConfigFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try{
            readConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!customThemes.exists()){
            if(!customThemes.getParentFile().exists())
                customThemes.getParentFile().mkdirs();
            try {
                createCustomThemes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try{
            readCustomThemes();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void playSong(EntityPlayerMP player, String sound) {
        MusicController.Instance.playStreaming(sound, player);
    }

    public static void playSongFromName(EntityPlayerMP playerMP, String songName){
        MusicController.Instance.playStreaming(GoldenGlow.songManager.getSongFromName(songName).path, playerMP);
    }

    public Song getSongFromName(String name){
        for(Song song: this.songs){
            if(song.name.equals(name)){
                return song;
            }
        }
        return null;
    }

    public void readConfig() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(configFile));
        String readLine;
        while((readLine=reader.readLine())!=null){
            if(readLine.startsWith("wildBattleSong=")){
                wildBattleSong=readLine.replace("wildBattleSong=","").replace(" ","");
            }
            else if(readLine.startsWith("trainerBattleSong=")){
                trainerBattleSong=readLine.replace("trainerBattleSong=","").replace(" ","");
            }
            else if(readLine.startsWith("victorySong=")){
                victorySong=readLine.replace("victorySong=","").replace(" ","");
            }
            else if(readLine.startsWith("encounterSong=")){
                victorySong=readLine.replace("encounterSong=","").replace(" ","");
            }
        }
    }

    public void readCustomThemes() throws IOException {
        InputStream iStream = new FileInputStream(customThemes);
        JsonArray json = new JsonParser().parse(new InputStreamReader(iStream, StandardCharsets.UTF_8)).getAsJsonArray();
        for(int i=0;i<json.size();i++) {
            JsonObject customSongObj = json.get(i).getAsJsonObject();
            String user=customSongObj.get("Username").getAsString();
            String song=customSongObj.get("Song").getAsString();
            playerThemes.put(user, song);
        }
    }

    protected void createConfigFile() throws IOException {
        configFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(configFile);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        bw.write("wildBattleSong=customnpcs:songs.wildTheme");
        bw.newLine();
        bw.write("trainerBattleSong=customnpcs:songs.TrainerBattle");
        bw.newLine();
        bw.write("victorySong=customnpcs:songs.victory");
        bw.newLine();
        bw.write("levelUpSound=customnpcs:songs.rivaltest");
        bw.newLine();
        bw.write("evolutionSong=customnpcs:songs.evolution");

        bw.close();
    }

    protected void createCustomThemes() throws IOException{
        customThemes.createNewFile();
        JsonWriter file = new JsonWriter( new FileWriter(customThemes));
        file.setIndent("\t");
        file.beginArray();
        file.beginObject();
        file.name("Username").value("Spit_GoldenHeart");
        file.name("Song").value("customnpcs:songs.curbYourEnthusiasm");
        file.endObject();
        file.endArray();
        file.close();
    }
}
