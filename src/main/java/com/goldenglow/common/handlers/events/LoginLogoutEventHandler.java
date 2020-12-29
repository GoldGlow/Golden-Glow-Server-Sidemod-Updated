package com.goldenglow.common.handlers.events;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.data.player.IPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.gyms.Gym;
import com.goldenglow.common.gyms.GymLeaderUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.time.Instant;

public class LoginLogoutEventHandler {
    @SubscribeEvent
    public void playerLoginEvent(PlayerEvent.PlayerLoggedInEvent event) {
        event.player.getCapability(OOPlayerProvider.OO_DATA, null).setLoginTime(Instant.now());
        if(!event.player.getEntityData().hasKey("playtime")) {
            event.player.getEntityData().setLong("playtime", 0);
        }
    }

    @SubscribeEvent
    public void playerLogoutEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        IPlayerData playerData = event.player.getCapability(OOPlayerProvider.OO_DATA, null);
        if (GoldenGlow.tradeManager.alreadyTrading((EntityPlayerMP) event.player)) {
            GoldenGlow.tradeManager.cancelTrade((EntityPlayerMP) event.player);
        }
        if (GoldenGlow.gymManager.leadingGym((EntityPlayerMP) event.player) != null) {
            GymLeaderUtils.stopTakingChallengers(GoldenGlow.gymManager.leadingGym((EntityPlayerMP) event.player), (EntityPlayerMP) event.player);
        } else if (GoldenGlow.gymManager.challengingGym((EntityPlayerMP) event.player) != null) {
            GymLeaderUtils.nextInQueue(GoldenGlow.gymManager.challengingGym((EntityPlayerMP) event.player), GoldenGlow.gymManager.challengingGym((EntityPlayerMP) event.player).currentLeader);
        }
        for (Gym gym : GoldenGlow.gymManager.getGyms()) {
            if (GoldenGlow.permissionUtils.checkPermission(((EntityPlayerMP) event.player), "staff.gym_leader." + gym.getName().toLowerCase().replace(" ", "_"))) {
                if (GoldenGlow.gymManager.hasGymLeaderOnline(gym).size() == 1) {
                    GymLeaderUtils.closeGym(gym);
                }
            }
        }
        GoldenGlow.gymManager.removeFromQueues((EntityPlayerMP) event.player);
    }
}
