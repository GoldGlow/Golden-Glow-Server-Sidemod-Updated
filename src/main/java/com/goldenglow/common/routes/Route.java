package com.goldenglow.common.routes;

import com.goldenglow.common.data.OOPlayerProvider;
import com.goldenglow.common.music.SongManager;
import com.goldenglow.common.util.NPCFunctions;
import com.goldenglow.common.util.Requirement;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
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
    public List<Requirement> requirements = new ArrayList<>();
    public boolean isSafeZone = false;
    public int warpX=0;
    public int warpY=100;
    public int warpZ=0;

    List<EntityPlayerMP> players = new ArrayList<>();

    public Route(String name, Polygonal2DRegion region) {
        this(name, "", region, 0);
    }

    public Route(String name, String songName, Polygonal2DRegion region) {
        this(name, songName, region, 0);
    }

    public Route(String name, String song, Polygonal2DRegion region, int priority) {
        this.unlocalizedName = name;
        this.song = song;
        this.region = region;
        this.priority = priority;
    }

    public void warp(EntityPlayer player){
        player.setPosition(this.warpX, this.warpY, this.warpZ);
    }

    public void addPlayer(EntityPlayerMP playerMP) {
        if(!this.players.contains(playerMP)) {
            playerMP.getCapability(OOPlayerProvider.OO_DATA, null).setRoute(this.unlocalizedName);
            this.players.add(playerMP);
            SongManager.setCurrentSong(playerMP, this.song);
            if(this.displayName!=null && !this.displayName.isEmpty())
                Server.sendData(playerMP, EnumPacketClient.MESSAGE, this.displayName, "", Integer.valueOf(playerMP.getEntityData().getInteger("RouteNotification")));
        }
    }

    public void removePlayer(EntityPlayerMP playerMP) {
        this.players.remove(playerMP);
    }

    public List<EntityPlayerMP> getPlayersInRoute() {
        return this.players;
    }

    public boolean canPlayerEnter(EntityPlayerMP playerMP) {
        return Requirement.checkRequirements(this.requirements, playerMP);
    }

    public TextComponentString getRequirementMessage(EntityPlayerMP player) {
        TextComponentString msg = new TextComponentString("You don't meet the requirements for this area!");
        msg.getStyle().setBold(true);

        StringBuilder s = new StringBuilder();
        for(Requirement requirement : this.requirements) {
            if(!Requirement.checkRequirement(requirement, player)) {
                switch (requirement.type) {
                    case QUEST_STARTED:
                        if(!s.toString().isEmpty())
                            s.append("\n");
                        s.append("Start Quest: ").append(QuestController.instance.get(requirement.id).getName());
                        break;
                    case QUEST_FINISHED:
                        if(!s.toString().isEmpty())
                            s.append("\n");
                        s.append("Finish Quest: ").append(QuestController.instance.get(requirement.id).getName()).append("\n");
                        break;
                    case TIME:
                        if(!s.toString().isEmpty())
                            s.append("\n");
                        s.append("Time: ").append(requirement.value).append("\n");
                        break;
                    case DIALOG:
                        if(!s.toString().isEmpty())
                            s.append("\n");
                        s.append("Read Dialog: ").append(DialogController.instance.get(requirement.id).getName()).append("\n");
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

    public TextComponentString getRequirementHoverText() {
        TextComponentString text = new TextComponentString("");

        for (Requirement r : this.requirements) {
            if(!text.getText().isEmpty())
                text.appendText("\n");
            text.appendText(r.toString());
        }

        return text;
    }
}
