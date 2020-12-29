package com.goldenglow.common.util.actions.types.trainer;

import com.goldenglow.common.guis.essentials.OOTrainerGui;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import com.pixelmonessentials.common.guis.TrainerDataGui;
import com.pixelmonessentials.common.guis.battles.SelectTeamGui;
import com.pixelmonessentials.common.util.EssentialsLogger;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiTextFieldWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;

public class OOReturnToTrainerAction extends ActionBase {
    public OOReturnToTrainerAction() {
        super("RETURN_TRAINER");
    }

    public void doAction(EntityPlayerMP playerMP, ActionData data) {
        EssentialsGuis gui = PixelmonEssentials.essentialsGuisHandler.getGui(playerMP);
        if (gui instanceof SelectTeamGui) {
            String[] previousChoices = ((SelectTeamGui)gui).getParentChoices();
            OOTrainerGui dataGui = (OOTrainerGui) ((SelectTeamGui)gui).getParentGui();
            CustomGuiWrapper guiWrapper = dataGui.getGui();
            ((CustomGuiTextFieldWrapper)guiWrapper.getComponent(401)).setText(previousChoices[0]);
            ((CustomGuiTextFieldWrapper)guiWrapper.getComponent(402)).setText(previousChoices[1]);
            ((CustomGuiTextFieldWrapper)guiWrapper.getComponent(403)).setText(previousChoices[2]);
            ((CustomGuiTextFieldWrapper)guiWrapper.getComponent(405)).setText(previousChoices[3]);
            ((CustomGuiTextFieldWrapper)guiWrapper.getComponent(406)).setText(previousChoices[4]);
            ((CustomGuiTextFieldWrapper)guiWrapper.getComponent(407)).setText(previousChoices[5]);
            (new PlayerWrapper(playerMP)).showCustomGui(guiWrapper);
            PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(playerMP, dataGui);
        }

    }
}
