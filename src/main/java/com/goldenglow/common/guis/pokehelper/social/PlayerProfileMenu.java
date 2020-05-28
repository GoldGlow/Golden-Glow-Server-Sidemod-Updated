package com.goldenglow.common.guis.pokehelper.social;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.data.player.IPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.util.Reference;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.entity.EntityNPCInterface;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class PlayerProfileMenu implements EssentialsGuis {
    private static final int id=6004;
    private ArrayList<EssentialsButton> buttons = new ArrayList<EssentialsButton>();
    private EntityPlayerMP otherPlayer;

    public PlayerProfileMenu(EntityPlayerMP otherPlayer){
        this.otherPlayer=otherPlayer;
    }

    public int getId() {
        return this.id;
    }

    public ArrayList<EssentialsButton> getButtons() {
        return this.buttons;
    }

    public void addButton(EssentialsButton button) {
        this.buttons.add(button);
    }

    public void init(EntityPlayerMP player, EntityNPCInterface npc) {
        IPlayerData otherPlayerData = this.otherPlayer.getCapability(OOPlayerProvider.OO_DATA, null);
        IPlayerData playerData = player.getCapability(OOPlayerProvider.OO_DATA, null);
        PlayerWrapper playerWrapper = new PlayerWrapper(player);
        CustomGuiWrapper gui = new CustomGuiWrapper(id, 256, 256, false);
        gui.setBackgroundTexture("obscureobsidian:textures/gui/black_square.png");
        gui.addLabel(201, this.otherPlayer.getName(), 25, 20, 128, 20);
        gui.addLabel(202, GoldenGlow.permissionUtils.getPrefix(this.otherPlayer), 25, 35, 128, 20);
        //gui.addLabel(203, "Team (not soon)", 128, 35, 128, 20);
        gui.addLabel(204, Reference.darkGreen+GoldenGlow.routeManager.getRoute(this.otherPlayer).displayName, 128, 20, 128, 20);
        gui.addLabel(205, "Pokemon Seen: "+ Pixelmon.storageManager.getParty(this.otherPlayer).pokedex.countSeen(), 25, 50, 128, 20);
        gui.addLabel(206, "Pokemon Caught: "+Pixelmon.storageManager.getParty(this.otherPlayer).pokedex.countCaught(), 25, 65, 128, 20);
        long sessionTime = Math.abs(Duration.between(Instant.now(), otherPlayerData.getLoginTime()).getSeconds());
        long totalTime = this.otherPlayer.getEntityData().getLong("playtime") + sessionTime;
        gui.addLabel(207, "Play time: "+String.format("%sh:%sm", totalTime / 3600, (totalTime % 3600) / 60), 128, 50, 128, 20);
        gui.addLabel(208, "Titles: "+ GoldenGlow.permissionUtils.getUnlockedPrefixTotal(this.otherPlayer) +"/"+GoldenGlow.permissionUtils.getPrefixTotal(), 128, 65, 128, 20);
        if(!otherPlayerData.getFriendList().contains(player.getUniqueID())){
            if(playerData.getFriendRequests().contains(this.otherPlayer.getUniqueID())){
                gui.addButton(0, "Accept", 156, 100, 80, 20);
                gui.addButton(1, "Deny", 156, 135, 80, 20);
            }
            else if(!(playerData.getFriendRequests().contains(this.otherPlayer.getUniqueID())||otherPlayerData.getFriendRequests().contains(player.getUniqueID()))){
                gui.addButton(0, "Add Friend", 156, 100, 80, 20);
            }
            else{
                gui.addLabel(209, "Request pending", 156, 100, 80, 20);
            }
        }
        else {
            gui.addButton(0, "Remove Friend", 156, 100, 80, 20);
        }
        gui.addTexturedRect(100, "obscureobsidian:textures/gui/grey_square.png", 16, 100, 128, 64);
        if(GoldenGlow.permissionUtils.checkPermission(this.otherPlayer, "badge.sakura_gym.player")) {
            gui.addTexturedRect(101, "obscureobsidian:textures/items/badge/badge1.png", 16, 100, 256, 256).setScale(0.125F);
        }
        else if(GoldenGlow.permissionUtils.checkPermission(this.otherPlayer, "badge.sakura_gym.npc")){
            gui.addTexturedRect(101, "obscureobsidian:textures/items/badge/badge1.png", 16, 100, 256, 256).setScale(0.125F);
        }
        else{
            gui.addTexturedRect(101, "obscureobsidian:textures/items/badge_shadows/badge1.png", 16, 100, 256, 256).setScale(0.125F);
        }
        gui.addTexturedRect(102, "obscureobsidian:textures/items/badge_shadows/badge2.png", 48, 100, 256, 256).setScale(0.125F);
        gui.addTexturedRect(103, "obscureobsidian:textures/items/badge_shadows/badge3.png", 80, 100, 256, 256).setScale(0.125F);
        gui.addTexturedRect(104, "obscureobsidian:textures/items/badge_shadows/badge4.png", 112, 100, 256, 256).setScale(0.125F);
        gui.addTexturedRect(105, "obscureobsidian:textures/items/badge_shadows/badge5.png", 16, 132, 256, 256).setScale(0.125F);
        gui.addTexturedRect(106, "obscureobsidian:textures/items/badge_shadows/badge6.png", 48, 132, 256, 256).setScale(0.125F);
        gui.addTexturedRect(107, "obscureobsidian:textures/items/badge_shadows/badge7.png", 80, 132, 256, 256).setScale(0.125F);
        gui.addTexturedRect(108, "obscureobsidian:textures/items/badge_shadows/badge8.png", 116, 136, 256, 256).setScale(0.1F);
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }
}