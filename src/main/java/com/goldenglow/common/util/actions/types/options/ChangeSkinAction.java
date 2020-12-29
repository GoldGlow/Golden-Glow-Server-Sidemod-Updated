package com.goldenglow.common.util.actions.types.options;

import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import net.minecraft.entity.player.EntityPlayerMP;

public class ChangeSkinAction extends ActionBase {
    public ChangeSkinAction(){
        super("CHANGESKIN");
    }

    @Override
    public void doAction(EntityPlayerMP player, ActionData data){
        if(data instanceof ActionStringData){
            String[] words=((ActionStringData) data).getValue().split(" ");
            String name="";
            player.getHeldItemMainhand().setItemDamage(Integer.parseInt(words[0]));
            for(int i=1;i<words.length;i++){
                if(i>1){
                    name+=" ";
                }
                name+=words[i];
            }
            player.getHeldItemMainhand().setStackDisplayName(name);
        }
    }
}
