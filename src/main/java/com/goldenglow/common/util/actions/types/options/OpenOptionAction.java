package com.goldenglow.common.util.actions.types.options;

import com.goldenglow.common.guis.pokehelper.config.OptionListMenu;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.OptionTypeManager;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import net.minecraft.entity.player.EntityPlayerMP;

public class OpenOptionAction extends ActionBase {
    public OpenOptionAction(){
        super("OPEN_OPTION");
    }

    @Override
    public void doAction(EntityPlayerMP player, ActionData data){
        if(data instanceof ActionStringData){
            OptionTypeManager.EnumOptionType optionType=OptionTypeManager.EnumOptionType.valueOf(((ActionStringData) data).getValue());
            OptionListMenu gui=new OptionListMenu();
            gui.setOptionType(optionType);
            gui.init(player);
        }
    }
}
