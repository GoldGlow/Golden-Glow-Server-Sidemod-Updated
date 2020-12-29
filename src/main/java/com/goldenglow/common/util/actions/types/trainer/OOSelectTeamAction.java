package com.goldenglow.common.util.actions.types.trainer;

import com.goldenglow.common.guis.essentials.OOTeamGui;
import com.goldenglow.common.guis.essentials.OOTrainerGui;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import com.pixelmonessentials.common.guis.TrainerDataGui;
import com.pixelmonessentials.common.guis.battles.SelectTeamGui;
import net.minecraft.entity.player.EntityPlayerMP;

public class OOSelectTeamAction extends ActionBase {
    public OOSelectTeamAction() {
        super("SELECT_TEAM");
    }

    public void doAction(EntityPlayerMP playerMP, ActionData data) {
        EssentialsGuis gui = PixelmonEssentials.essentialsGuisHandler.getGui(playerMP);
        if (gui instanceof OOTrainerGui) {
            OOTeamGui selectTeamGui = new OOTeamGui();
            selectTeamGui.setParentGui((TrainerDataGui)gui);
            selectTeamGui.init(playerMP);
        }

    }
}
