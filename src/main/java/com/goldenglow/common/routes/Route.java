package com.goldenglow.common.routes;

import com.sk89q.worldedit.regions.Polygonal2DRegion;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import noppes.npcs.Server;
import noppes.npcs.constants.EnumPacketClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Route {
    public String unlocalizedName;
    public String displayName = "";
    public String song;
    public int priority;
    public Polygonal2DRegion region;

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

    public void addPlayer(EntityPlayerMP playerMP) {
        if(!players.contains(playerMP)) {
            playerMP.getEntityData().setString("Route", this.unlocalizedName);
            this.players.add(playerMP);
        }
    }

    public void removePlayer(EntityPlayerMP playerMP) {
        playerMP.getEntityData().removeTag("Route");
        this.players.remove(playerMP);
    }

    public List<EntityPlayerMP> getPlayersInRoute() {
        return this.players;
    }
}
