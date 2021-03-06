package com.goldenglow.common.routes;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.music.SongManager;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.requirement.RequirementData;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import noppes.npcs.Server;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.QuestController;

import java.util.ArrayList;
import java.util.List;

public class Route {
    public String unlocalizedName;
    public String displayName = "";
    public String song;
    public int priority;
    public Polygonal2DRegion region;
    public List<RequirementData> requirements = new ArrayList<>();
    public boolean isSafeZone = false;
    public boolean kickWarp = false;
    public int warpX=0;
    public int warpY=100;
    public int warpZ=0;
    public int kickWarpX=0;
    public int kickWarpY=100;
    public int kickWarpZ=0;
    public World world;
    public List<SpawnPokemon> apricornPokemon=new ArrayList<>();
    public FishingSpawns fishingPokemon=new FishingSpawns();
    public List<SpawnPokemon> spawnList=new ArrayList<>();
    public List<SpawnPokemon> specialSpawnList=new ArrayList<>();

    List<EntityPlayerMP> players = new ArrayList<>();

    public Route(String name, Polygonal2DRegion region, World world) {
        this(name, "", region, 0, world);
    }

    public Route(String name, String songName, Polygonal2DRegion region, World world) {
        this(name, songName, region, 0, world);
    }

    public Route(String name, String song, Polygonal2DRegion region, int priority, World world) {
        this.unlocalizedName = name;
        this.song = song;
        this.region = region;
        this.priority = priority;
        this.world = world;
    }

    public void warp(EntityPlayer player){
        player.setPosition(this.warpX, this.warpY, this.warpZ);
    }

    public void addPlayer(EntityPlayerMP playerMP) {
        if (!this.players.contains(playerMP)) {
            this.players.add(playerMP);
        }
        playerMP.getCapability(OOPlayerProvider.OO_DATA, null).setRoute(this);
        SongManager.setCurrentSong(playerMP, this.song);
        if (this.displayName != null && !this.displayName.isEmpty())
            Server.sendData(playerMP, EnumPacketClient.MESSAGE, this.displayName, "", playerMP.getCapability(OOPlayerProvider.OO_DATA, null).getNotificationScheme());
    }

    public void removePlayer(EntityPlayerMP playerMP) {
        this.players.remove(playerMP);
    }

    public List<EntityPlayerMP> getPlayersInRoute() {
        return this.players;
    }

    public boolean canPlayerEnter(EntityPlayerMP playerMP) {
        return PixelmonEssentials.requirementHandler.checkRequirements(this.requirements, playerMP);
    }

    public TextComponentString getRequirementMessage(EntityPlayerMP player) {
        TextComponentString msg = new TextComponentString("You don't meet the requirements for this area!");
        msg.getStyle().setBold(true);

        StringBuilder s = new StringBuilder();
        for(RequirementData requirement : this.requirements) {
            if(!PixelmonEssentials.requirementHandler.checkRequirement(requirement, player)) {
                if(s.length()>0)
                    s.append("\n");
                switch (requirement.name) {
                    case "QUEST_STARTED":
                        s.append("Start Quest: ").append(QuestController.instance.get(Integer.parseInt(requirement.value)).getName());
                        break;
                    case "QUEST_FINISHED":
                        s.append("Finish Quest: ").append(QuestController.instance.get(Integer.parseInt(requirement.value)).getName());
                        break;
                    case "TIME":
                        s.append("Time: ").append(requirement.value);
                        break;
                    case "DIALOG":
                        s.append("Read Dialog: ").append(DialogController.instance.get(Integer.parseInt(requirement.value)).getName());
                        break;
                }
            }
        }
        if(!s.toString().isEmpty()) {
            TextComponentString hoverMsg = new TextComponentString(s.toString());
            hoverMsg.getStyle().setColor(TextFormatting.DARK_RED);
            msg.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverMsg));
        }
        return msg;
    }

    public SpawnPokemon[] getSpawnsList(){
        return (SpawnPokemon[]) this.spawnList.toArray();
    }

    public TextComponentString getRequirementHoverText() {
        TextComponentString text = new TextComponentString("");

        for (RequirementData r : this.requirements) {
            if(!text.getText().isEmpty())
                text.appendText("\n");
            text.appendText(r.toString());
        }

        return text;
    }
}
