package com.goldenglow.common.routes;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import noppes.npcs.Server;
import noppes.npcs.constants.EnumPacketClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Route {
    public final String name;
    public final String desc;

    public List<EntityPlayerMP> playerList = new ArrayList<EntityPlayerMP>();
    public List<Area> areas = new ArrayList<Area>();
    public List<SpawnPokemon> pokeList = new ArrayList<SpawnPokemon>();

    public Route(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public Route(String name, String desc, Area area) {
        this.name = name;
        this.desc = desc;
        this.areas.add(area);
    }

    public void addPlayer(EntityPlayerMP player) {
        playerList.add(player);
        Server.sendData(player, EnumPacketClient.MESSAGE, new Object[]{name, desc});
    }

    public void playerCheck(World world) {
        for(Area area : areas) {
            List players = new ArrayList<String>();
            for(Object obj : world.getEntitiesWithinAABB(EntityPlayerMP.class, AxisAlignedBB.fromBounds(area.pos1.getX(), area.pos1.getY() - 5, area.pos1.getZ(), area.pos2.getX(), area.pos2.getY() + 5, area.pos2.getZ()))) {
                EntityPlayerMP player = (EntityPlayerMP)obj;
                if(!players.contains(player.getName()))
                    players.add(player.getName());
                if(!playerList.contains(player))
                    addPlayer(player);
            }
            Iterator<EntityPlayerMP> i = playerList.iterator();
            while (i.hasNext()) {
                EntityPlayerMP player = i.next();
                if (!players.contains(player.getName())) {
                    i.remove();
                }
            }
        }
    }
}
