package com.goldenglow.common.guis.pokehelper.social;

import com.goldenglow.common.data.player.IPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionIdData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsScrollGui;
import com.pixelmonessentials.common.api.gui.bases.EssentialsScrollGuiBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiComponentWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiScrollWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.controllers.CustomGuiController;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.ArrayList;
import java.util.UUID;

public class FriendRequestsMenu extends EssentialsScrollGuiBase {
    private int index;
    private String[] requestNames;

    public FriendRequestsMenu(){
        super(6005);
        this.addButton(new EssentialsButton(502, new ActionData("ACCEPT_FRIEND")));
        this.addButton(new EssentialsButton(503, new ActionIdData("SCROLL", -1)));
    }

    public void setIndex(int id, String name){
        for(int i=0;i<this.requestNames.length;i++){
            if(name.equals(this.requestNames[i])){
                this.index=i;
                return;
            }
        }
    }

    public void setIndex(int index){
        this.index=index;
    }

    public int getIndex(){
        return this.index;
    }

    public String[] getRequestNames(){
        return this.requestNames;
    }

    public void init(EntityPlayerMP player){
        this.index=-1;
        IPlayerData playerData = player.getCapability(OOPlayerProvider.OO_DATA, null);
        String[] requestNames=new String[playerData.getFriendRequests().size()];
        int index=0;
        for(UUID request: playerData.getFriendRequests()){
            if(UsernameCache.containsUUID(request)&& FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(request)!=null){
                requestNames[index]=UsernameCache.getLastKnownUsername(request);
                index++;
            }
            else {
                playerData.getFriendRequests().remove(request);
            }
        }
        this.requestNames=requestNames;
        CustomGuiWrapper gui=new CustomGuiWrapper();
        gui.setBackgroundTexture("customnpcs:textures/gui/bgfilled.png");
        gui.addLabel(100, "Friend requests", 100, 0, 128, 20);
        gui.addScroll(200, 126, 20, 124, 216, this.requestNames);
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        playerWrapper.showCustomGui(gui);
    }

    public void updateScroll(int id, EntityPlayerMP player){
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        CustomGuiWrapper gui= CustomGuiController.getOpenGui(player);
        if(this.index==-1){
            CustomGuiScrollWrapper scroll= new CustomGuiScrollWrapper(200, 126, 20, 124, 216, this.requestNames);
            gui.removeComponent(501);
            gui.removeComponent(502);
            gui.removeComponent(503);
            gui.update(playerWrapper);
        }
        else{
            CustomGuiComponentWrapper[] components=new CustomGuiComponentWrapper[4];
            gui.removeComponent(501);
            gui.removeComponent(502);
            gui.removeComponent(503);
            gui.removeComponent(200);
            gui.addScroll(200, 126, 20, 124, 216, this.requestNames).setDefaultSelection(this.index);
            gui.addButton(501, "Check Profile", 16, 32, 96, 20);
            gui.addButton(502, "Accept friendList", 16, 64, 96, 20);
            gui.addButton(503, "Reject friendList", 16, 96, 96, 20);
            gui.update(playerWrapper);
        }
    }
}
