package com.goldenglow.common.guis;

import com.goldenglow.common.data.player.IPlayerData;
import com.goldenglow.common.data.player.OOPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.inventory.CustomItem;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiButtonWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiComponentWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiScrollWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.controllers.CustomGuiController;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.ArrayList;
import java.util.UUID;

public class FriendRequestsMenu implements EssentialsGuis {
    private static final int id=6005;
    private ArrayList<EssentialsButton> buttons=new ArrayList<EssentialsButton>();
    private int index;
    private String[] requestNames;

    public FriendRequestsMenu(){
        this.addButton(new EssentialsButton(2, new ActionData("ACCEPT_FRIEND", "")));
        this.addButton(new EssentialsButton(3, new ActionData("SCROLL", "-1")));
    }

    public int getId(){
        return this.id;
    }

    public ArrayList<EssentialsButton> getButtons(){
        return this.buttons;
    }

    public void addButton(EssentialsButton button){
        this.buttons.add(button);
    }

    public void setIndex(String name){
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

    public void init(EntityPlayerMP player, EntityNPCInterface npc){
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

    public void updateScroll(EntityPlayerMP player){
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        CustomGuiWrapper gui= CustomGuiController.getOpenGui(player);
        if(this.index==-1){
            CustomGuiScrollWrapper scroll= new CustomGuiScrollWrapper(200, 126, 20, 124, 216, this.requestNames);
            if(gui.getComponent(1)!=null){
                gui.getComponents().remove(gui.getComponent(1));
            }
            if(gui.getComponent(2)!=null){
                gui.getComponents().remove(gui.getComponent(2));
            }
            if(gui.getComponent(3)!=null){
                gui.getComponents().remove(gui.getComponent(3));
            }
            CustomGuiController.updateGui(playerWrapper, gui);
        }
        else{
            CustomGuiComponentWrapper[] components=new CustomGuiComponentWrapper[4];
            if(gui.getComponent(1)!=null){
                gui.getComponents().remove(gui.getComponent(1));
            }
            if(gui.getComponent(2)!=null){
                gui.getComponents().remove(gui.getComponent(2));
            }
            if(gui.getComponent(3)!=null){
                gui.getComponents().remove(gui.getComponent(3));
            }
            if(gui.getComponent(200)!=null){
                gui.getComponents().remove(gui.getComponent(200));
            }
            gui.addScroll(200, 126, 20, 124, 216, this.requestNames).setDefaultSelection(this.index);
            gui.addButton(1, "Check Profile", 16, 32, 96, 20);
            gui.addButton(2, "Accept friend", 16, 64, 96, 20);
            gui.addButton(3, "Reject friend", 16, 96, 96, 20);
            CustomGuiController.updateGui(playerWrapper, gui);
        }
    }
}
