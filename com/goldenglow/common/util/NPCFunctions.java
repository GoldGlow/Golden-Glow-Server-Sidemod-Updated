package com.goldenglow.common.routes;

import com.goldenglow.common.util.BlockPos;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import noppes.npcs.Server;
import noppes.npcs.constants.EnumPacketClient;

public class Route
{
    public final String name;
    public final String desc;
    public List<EntityPlayerMP> playerList = new ArrayList();
    public List<Area> areas = new ArrayList();
    public List<String> pokeList = new ArrayList();
    public int minLvl;
    public int maxLvl;

    public Route(String name, String desc)
    {
        this.name = name;
        this.desc = desc;
    }

    public Route(String name, String desc, Area area)
    {
        this.name = name;
        this.desc = desc;
        this.areas.add(area);
    }

    public void addPlayer(EntityPlayerMP player)
    {
        this.playerList.add(player);
        Server.sendData(player, EnumPacketClient.MESSAGE, new Object[] { this.name, this.desc });
    }

    public void playerCheck(World world)
    {
        for (Area area : this.areas)
        {
            List players = new ArrayList();
            for (Object obj : world.func_72872_a(EntityPlayerMP.class, AxisAlignedBB.func_72330_a(area.pos1.x, area.pos1.y - 5, area.pos1.z, area.pos2.x, area.pos2.y + 5, area.pos2.z)))
            {
                EntityPlayerMP player = (EntityPlayerMP)obj;
                if (!players.contains(player.func_70005_c_())) {
                    players.add(player.func_70005_c_());
                }
                if (!this.playerList.contains(player)) {
                    addPlayer(player);
                }
            }
            Object i = this.playerList.iterator();
            while (((Iterator)i).hasNext())
            {
                EntityPlayerMP player = (EntityPlayerMP)((Iterator)i).next();
                if (!players.contains(player.func_70005_c_())) {
                    ((Iterator)i).remove();
                }
            }
        }
    }
}
