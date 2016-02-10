package com.goldenglow.common.gyms;

import com.goldenglow.common.util.BlockPos;
import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.Reference;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GymManager
{
    public List<Gym> gymList = new ArrayList();

    public void init()
    {
        File dir = new File(Reference.configDir, "gyms.json");
        if (!dir.exists())
        {
            if (!dir.getParentFile().exists()) {
                dir.getParentFile().mkdirs();
            }
        }
        else {
            loadGyms();
        }
    }

    public Gym getGym(String name)
    {
        for (Gym gym : this.gymList) {
            if (gym.name.equalsIgnoreCase(name)) {
                return gym;
            }
        }
        return null;
    }

    public Gym getGym(EntityPlayer player)
    {
        for (Gym gym : this.gymList) {
            if ((gym.challengerList.contains(player)) || ((gym.currentChallenger != null) && (gym.currentChallenger.func_70028_i(player)))) {
                return gym;
            }
        }
        return null;
    }

    public void checkPlayer(EntityPlayer player)
    {
        for (Gym gym : this.gymList)
        {
            if (gym.gymLeaders.containsValue(player.getPersistentID()))
            {
                if (!gym.gymLeaders.containsKey(player.func_70005_c_())) {
                    for (String leader : gym.gymLeaders.keySet()) {
                        if (((UUID)gym.gymLeaders.get(leader)).equals(player.getPersistentID()))
                        {
                            gym.gymLeaders.remove(leader);
                            gym.gymLeaders.put(player.func_70005_c_(), player.getPersistentID());
                        }
                    }
                }
                Bukkit.broadcastMessage(Reference.gymMessagePrefix + "Gym Leader " + player.getDisplayName() + " has logged in!");
            }
            if (!gym.currentChallenger.func_70028_i(player)) {}
        }
    }

    public void loadGyms()
    {
        GGLogger.info("Loading Gyms...");
        try
        {
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader("config/GoldenGlow/gyms.json");
            Object obj = parser.parse(reader);
            JSONArray json = (JSONArray)obj;
            for (Object o : json)
            {
                JSONObject g = (JSONObject)o;
                String name = g.get("Name").toString();
                int lvlCap = Integer.parseInt(g.get("LvlCap").toString());

                JSONArray leadersObj = (JSONArray)g.get("Leaders");
                Map<String, UUID> leaders = new HashMap();
                for (Object leaderObj : leadersObj)
                {
                    JSONObject leader = (JSONObject)leaderObj;
                    String playerName = leader.get("PlayerName").toString();
                    UUID uuid = UUID.fromString(leader.get("UUID").toString());
                    leaders.put(playerName, uuid);
                }
                JSONObject warpObj = (JSONObject)g.get("WarpPos");
                Long xLong = (Long)warpObj.get("posX");
                int x = xLong.intValue();
                Long yLong = (Long)warpObj.get("posY");
                int y = yLong.intValue();
                Long zLong = (Long)warpObj.get("posZ");
                int z = zLong.intValue();
                BlockPos bp1 = new BlockPos(x, y, z);
                BlockPos warpPoint = new BlockPos(x, y, z);

                Gym gym = new Gym(name, lvlCap, warpPoint);
                gym.gymLeaders = leaders;

                this.gymList.add(gym);
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void saveGyms()
    {
        GGLogger.info("Saving Gyms...");
        JSONArray obj = new JSONArray();
        for (Gym gym : this.gymList)
        {
            JSONObject gymObj = new JSONObject();
            gymObj.put("Name", gym.name);
            gymObj.put("LvlCap", Integer.valueOf(gym.levelCap));
            JSONArray leadersObj = new JSONArray();
            for (String leader : gym.gymLeaders.keySet())
            {
                JSONObject leaderObj = new JSONObject();
                leaderObj.put("PlayerName", leader);
                leaderObj.put("UUID", ((UUID)gym.gymLeaders.get(leader)).toString());
                leadersObj.add(leaderObj);
            }
            JSONObject posObj = new JSONObject();
            posObj.put("posX", Integer.valueOf(gym.warpPoint.x));
            posObj.put("posY", Integer.valueOf(gym.warpPoint.y));
            posObj.put("posZ", Integer.valueOf(gym.warpPoint.z));
            gymObj.put("WarpPos", posObj);
            obj.add(gymObj);
        }
        try
        {
            File dir = new File(Reference.configDir, "gyms.json");
            if (!dir.exists()) {
                dir.createNewFile();
            }
            FileWriter file = new FileWriter(dir);
            file.write(obj.toJSONString());
            file.flush();
            file.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
