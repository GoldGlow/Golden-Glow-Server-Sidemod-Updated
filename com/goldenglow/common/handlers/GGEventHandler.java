package com.goldenglow.common.handlers;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.gyms.GymManager;
import com.goldenglow.common.routes.Area;
import com.goldenglow.common.util.BlockPos;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import noppes.npcs.scripted.ScriptNpc;

public class GGEventHandler
{
    @SubscribeEvent
    public void playerLogOut(PlayerEvent.PlayerLoggedOutEvent event)
    {
        if (GoldenGlow.instance.followerHandler.followMap.containsKey(event.player))
        {
            ((ScriptNpc)GoldenGlow.instance.followerHandler.followMap.get(event.player)).despawn();
            GoldenGlow.instance.followerHandler.followMap.remove(event.player);
        }
    }

    @SubscribeEvent
    public void playerLoginEvent(PlayerEvent.PlayerLoggedInEvent event)
    {
        GoldenGlow.instance.gymManager.checkPlayer(event.player);
    }

    @SubscribeEvent
    public void playerUseItem(PlayerInteractEvent event)
    {
        if (event.entityPlayer.func_71045_bC() != null)
        {
            if ((event.action.equals(PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)) && ((event.entityPlayer.func_71045_bC().func_77973_b() instanceof ItemAxe)))
            {
                BlockPos pos1 = new BlockPos(event.x, event.y, event.z);
                Area area = new Area();
                if (GoldenGlow.instance.tempHandler.playerBlocks.containsKey(event.entityPlayer)) {
                    area = (Area)GoldenGlow.instance.tempHandler.playerBlocks.get(event.entityPlayer);
                }
                area.pos1 = pos1;
                GoldenGlow.instance.tempHandler.playerBlocks.put(event.entityPlayer, area);
            }
            if ((event.action.equals(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)) && ((event.entityPlayer.func_71045_bC().func_77973_b() instanceof ItemAxe)))
            {
                BlockPos pos2 = new BlockPos(event.x, event.y, event.z);
                Area area = new Area();
                if (GoldenGlow.instance.tempHandler.playerBlocks.containsKey(event.entityPlayer)) {
                    area = (Area)GoldenGlow.instance.tempHandler.playerBlocks.get(event.entityPlayer);
                }
                area.pos2 = pos2;
                GoldenGlow.instance.tempHandler.playerBlocks.put(event.entityPlayer, area);
            }
        }
    }
}
