package com.goldenglow.common.util.actions.types;

import com.goldenglow.common.guis.config.OptionListMenu;
import com.goldenglow.common.guis.config.optionsTypes.OptionTypeManager;
import com.pixelmonessentials.common.api.action.Action;
import net.minecraft.entity.player.EntityPlayerMP;

public class OpenOptionAction implements Action {
    private final String name="OPEN_OPTION";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        OptionTypeManager.EnumOptionType optionType=OptionTypeManager.EnumOptionType.valueOf(value);
        OptionListMenu gui=new OptionListMenu();
        gui.setOptionType(optionType);
        gui.init(player, null);
    }
}
