package com.goldenglow.common.gyms;

import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.Reference;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import jdk.nashorn.internal.parser.JSONParser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentStyle;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.*;

public class GymManager {
    File dir;
    public List<Gym> gymList = new ArrayList<Gym>();
    ChatComponentStyle message;

    public GymManager() throws FileNotFoundException {
    }

    public void init() {
        dir = new File(Reference.configDir, "gyms.json");
        if(!dir.exists()) {
            if (!dir.getParentFile().exists())
                dir.getParentFile().mkdirs();
        }
        else
            loadGyms();
    }


    public Gym getGym(String name) {
        for(Gym gym : gymList) {
            if(gym.name.equalsIgnoreCase(name))
                return gym;
        }
        return null;
    }

    public Gym getGym(EntityPlayer player) {
        for(Gym gym : gymList) {
            if((gym.challengerList.contains(player)) || (gym.currentChallenger!=null && gym.currentChallenger.isEntityEqual(player)))
                return gym;
        }
        return null;
    }

    //Check if player is still in queue or is a gym leader.
    public void checkPlayer(EntityPlayer player) {
        for(Gym gym : gymList) {
            if(gym.gymLeaders.containsValue(player.getPersistentID())) {
                if(!gym.gymLeaders.containsKey(player.getName())) {
                    for(String leader : gym.gymLeaders.keySet()) {
                        if(gym.gymLeaders.get(leader).equals(player.getPersistentID())) {
                            gym.gymLeaders.remove(leader);
                            gym.gymLeaders.put(player.getName(), player.getPersistentID());
                        }
                    }
                }
                message.appendText(Reference.gymMessagePrefix+" Gym Leader "+player.getDisplayName()+" has logged in!");
                MinecraftServer.getServer().getConfigurationManager().sendChatMsg(message);
            }
            if(gym.currentChallenger.isEntityEqual(player)) {
                //Do Challenger Stuff
            }
        }
    }

    public void loadGyms() {
        GGLogger.info("Loading Gyms...");
        try {;
            InputStream iStream = new FileInputStream(dir);
            JsonArray json = new JsonParser().parse(new InputStreamReader(iStream, StandardCharsets.UTF_8)).getAsJsonArray();
            for(int i=0;i<json.size();i++) {
                JsonObject g = json.get(i).getAsJsonObject();
                String name = g.get("Name").getAsString();
                int lvlCap = g.get("LvlCap").getAsInt();

                JsonArray leadersObj = g.getAsJsonArray("Leaders");
                Map<String, UUID> leaders = new HashMap<String, UUID>();
                for(int j=0; j<leadersObj.size();j++) {
                    JsonObject leader = leadersObj.get(i).getAsJsonObject();
                    String playerName = leader.get("PlayerName").getAsString();
                    UUID uuid = UUID.fromString(leader.get("UUID").getAsString());
                    leaders.put(playerName, uuid);
                }
                JsonObject warpObj = g.get("WarpPos").getAsJsonObject();
                Long xLong = warpObj.get("posX").getAsLong();
                int x = xLong.intValue();
                Long yLong = warpObj.get("posY").getAsLong();
                int y = yLong.intValue();
                Long zLong = warpObj.get("posZ").getAsLong();
                int z = zLong.intValue();
                BlockPos bp1 = new BlockPos(x,y,z);
                BlockPos warpPoint = new BlockPos(x,y,z);

                Gym gym = new Gym(name, lvlCap, warpPoint);
                gym.gymLeaders = leaders;

                gymList.add(gym);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveGyms() {
        GGLogger.info("Saving Gyms...");
        try {
            File dir = new File(Reference.configDir, "gyms.json");
            if(!dir.exists())
                dir.createNewFile();
            JsonWriter file = new JsonWriter( new FileWriter(dir));
            file.beginArray();
            for(Gym gym : gymList) {
                file.beginObject();
                file.name("Name").value(gym.name);
                System.out.print(gym.name);
                file.name("LvlCap").value(gym.levelCap);
                file.name("Leaders");
                file.beginArray();
                for(String leader : gym.gymLeaders.keySet()) {
                    file.beginObject();
                    file.name("PlayerName").value(leader);
                    file.name("UUID").value(gym.gymLeaders.get(leader).toString());
                    file.endObject();
                }
                file.endArray();
                file.name("WarpPos");
                file.beginObject();
                file.name("posX").value(gym.warpPoint.getX());
                file.name("posY").value(gym.warpPoint.getY());
                file.name("posZ").value(gym.warpPoint.getZ());
                file.endObject();
                file.endObject();
            }
            file.endArray();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
