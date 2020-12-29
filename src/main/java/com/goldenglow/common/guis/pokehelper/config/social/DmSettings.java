package com.goldenglow.common.guis.pokehelper.config.social;

import com.goldenglow.common.data.player.OOPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionIdData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import com.pixelmonessentials.common.api.gui.bases.EssentialsGuiBase;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.ArrayList;

public class DmSettings extends EssentialsGuiBase {
    EntityPlayerMP player;

    public DmSettings(){
        super(6107);
        ActionData backButtonAction=new ActionIdData("OPEN_GUI", 6100);
        this.addButton(new EssentialsButton(500, backButtonAction));
        ActionData reloadAction=new ActionData("RELOAD");
        ActionData anyoneDmAction=new ActionStringData("SET_DM", "ANYONE");
        this.addButton(new EssentialsButton(501, new ActionData[]{anyoneDmAction, reloadAction}));
        ActionData friendsDmAction=new ActionStringData("SET_DM", "FRIENDS");
        this.addButton(new EssentialsButton(502, new ActionData[]{friendsDmAction, reloadAction}));
    }

    @Override
    public void init(EntityPlayerMP player){
        this.player=player;
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        CustomGuiWrapper gui= this.getGui();
    }

    @Override
    public CustomGuiWrapper getGui(){
        OOPlayerData data = (OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null);
        CustomGuiWrapper gui= new CustomGuiWrapper(this.getId(), 256, 256, false);
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
        return gui;
    }
}
