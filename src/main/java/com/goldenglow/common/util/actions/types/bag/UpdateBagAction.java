package com.goldenglow.common.util.actions.types.bag;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.guis.pokehelper.bag.BagMenu;
import com.goldenglow.common.guis.trading.TradeItemGui;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import com.sk89q.worldedit.util.command.binding.Switch;
import net.minecraft.entity.player.EntityPlayerMP;

public class UpdateBagAction extends ActionBase {
    public UpdateBagAction(){
        super("UPDATE_BAG");
    }

    @Override
    public void doAction(EntityPlayerMP player, ActionData data){
        if(data instanceof ActionStringData){
            EssentialsGuis gui= PixelmonEssentials.essentialsGuisHandler.getGui(player);
            if(gui instanceof BagMenu){
                String value=((ActionStringData) data).getValue();
                ((BagMenu) gui).setCategory(GoldenGlow.categoryManager.getCategory(value));
                ((BagMenu) gui).update(player);
            }
            else if(gui instanceof TradeItemGui){
                String value=((ActionStringData) data).getValue();
                ((TradeItemGui) gui).setCategory(GoldenGlow.categoryManager.getCategory(value));
                ((TradeItemGui) gui).update(player);
            }
        }
    }
}
