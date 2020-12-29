package com.goldenglow.common.util.actions.types.trainer;

import com.goldenglow.common.guis.essentials.OOTrainerGui;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionNpcGuiData;
import com.pixelmonessentials.common.guis.TrainerDataGui;
import net.minecraft.entity.player.EntityPlayerMP;

public class OOEditTrainerAction extends ActionBase {
    public OOEditTrainerAction() {
        super("EDIT_TRAINER");
    }

    public void doAction(EntityPlayerMP player, ActionData data) {
        if (data instanceof ActionNpcGuiData) {
            OOTrainerGui gui = new OOTrainerGui();
            gui.setNpc(((ActionNpcGuiData)data).getNpc());
            gui.init(player);
        }
    }
}
