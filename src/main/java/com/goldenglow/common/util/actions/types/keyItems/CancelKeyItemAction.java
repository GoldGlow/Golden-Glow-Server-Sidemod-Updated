package com.goldenglow.common.util.actions.types.keyItems;

import com.goldenglow.common.guis.essentials.KeyItemGui;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;

public class CancelKeyItemAction extends ActionBase {
    public CancelKeyItemAction(){
        super("CANCEL_KEY");
    }

    @Override
    public void doAction(EntityPlayerMP playerMP, ActionData data){
        EssentialsGuis gui= PixelmonEssentials.essentialsGuisHandler.getGui(playerMP);
        if(gui instanceof KeyItemGui){
            gui.init(playerMP);
        }
    }
}
