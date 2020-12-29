package com.goldenglow.common.util.actions.types.keyItems;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.guis.essentials.KeyItemGui;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;

public class DeleteKeyItemAction extends ActionBase {
    public DeleteKeyItemAction(){
        super("DELETE_KEY");
    }

    @Override
    public void doAction(EntityPlayerMP playerMP, ActionData data){
        EssentialsGuis gui= PixelmonEssentials.essentialsGuisHandler.getGui(playerMP);
        if(gui instanceof KeyItemGui){
            GoldenGlow.customItemManager.keyItems.remove(((KeyItemGui) gui).getKeyItem());
            gui.init(playerMP);
        }
    }
}
