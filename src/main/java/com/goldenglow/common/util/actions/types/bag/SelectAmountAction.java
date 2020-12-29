package com.goldenglow.common.util.actions.types.bag;

import com.goldenglow.common.guis.pokehelper.bag.BagMenu;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;

public class SelectAmountAction extends ActionBase {
    public SelectAmountAction(){
        super("SELECT_AMOUNT");
    }

    @Override
    public void doAction(EntityPlayerMP player, ActionData data){
        EssentialsGuis gui= PixelmonEssentials.essentialsGuisHandler.getGui(player);
        if(gui instanceof BagMenu){
            CustomGuiWrapper selectAmountGui=((BagMenu) gui).getStoreWithdrawGui(player);
            selectAmountGui.update(new PlayerWrapper(player));
        }
    }
}
