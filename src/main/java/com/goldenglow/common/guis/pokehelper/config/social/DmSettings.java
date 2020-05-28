package com.goldenglow.common.guis.pokehelper.config.social;

import com.goldenglow.common.data.player.OOPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.ArrayList;

public class DmSettings implements EssentialsGuis {
    private static final int id=6107;
    private ArrayList<EssentialsButton> buttons=new ArrayList<EssentialsButton>();

    public DmSettings(){
        ActionData backButtonAction=new ActionData("OPEN_GUI", "null@"+6100);
        this.addButton(new EssentialsButton(500, backButtonAction));
        ActionData reloadAction=new ActionData("OPEN_GUI", "null@"+6107);
        ActionData anyoneDmAction=new ActionData("SET_DM", "ANYONE");
        this.addButton(new EssentialsButton(501, new ActionData[]{anyoneDmAction, reloadAction}));
        ActionData friendsDmAction=new ActionData("SET_DM", "FRIENDS");
        this.addButton(new EssentialsButton(502, new ActionData[]{friendsDmAction, reloadAction}));
    }

    public int getId(){
        return this.id;
    }

    public ArrayList<EssentialsButton> getButtons(){
        return this.buttons;
    }

    public void addButton(EssentialsButton button) {
        this.buttons.add(button);
    }

    public void init(EntityPlayerMP player, EntityNPCInterface npc){
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        OOPlayerData data = (OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null);
        CustomGuiWrapper gui= new CustomGuiWrapper(id, 256, 256, false);
        gui.setBackgroundTexture("customnpcs:textures/gui/bgfilled.png");
        gui.addLabel(100, "DM Settings", 85, 4, 128, 20);
        gui.addLabel(101, "Anyone", 81, 30, 128, 20);
        if(data.canAnyoneDm()){
            gui.addTexturedButton(501, "", 148, 30, 20, 20, "obscureobsidian:textures/gui/phone/collective.png", 216, 89);
        }
        else{
            gui.addTexturedButton(501, "", 148, 30, 20, 20, "obscureobsidian:textures/gui/phone/collective.png", 236, 89);
        }
        gui.addLabel(102, "Friends", 81, 55, 128, 20);
        if(data.canFriendsDm()){
            gui.addTexturedButton(502, "", 148, 55, 20, 20, "obscureobsidian:textures/gui/phone/collective.png", 216, 89);
        }
        else{
            gui.addTexturedButton(502, "", 148, 55, 20, 20, "obscureobsidian:textures/gui/phone/collective.png", 236, 89);
        }
        gui.addButton(500, "Back", 30, 216, 64, 20);
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }
}
