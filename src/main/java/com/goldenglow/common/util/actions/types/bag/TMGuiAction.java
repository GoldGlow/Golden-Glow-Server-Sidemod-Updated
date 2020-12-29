package com.goldenglow.common.util.actions.types.bag;

import com.goldenglow.common.guis.pokehelper.bag.TeachTMMenu;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import net.minecraft.entity.player.EntityPlayerMP;

public class TMGuiAction extends ActionBase {
    public TMGuiAction(){
        super("TM_GUI");
    }

    @Override
    public void doAction(EntityPlayerMP player, ActionData data){
        if(data instanceof ActionStringData){
            TeachTMMenu menu=new TeachTMMenu(((ActionStringData) data).getValue());
            menu.init(player);
        }
    }
}