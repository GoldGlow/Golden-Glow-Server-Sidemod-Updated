package com.goldenglow.common.guis.essentials;

import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.guis.battles.SelectTeamGui;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiTextFieldWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.controllers.CustomGuiController;

public class OOTeamGui extends SelectTeamGui {
    String[] parentChoices;

    @Override
    public String[] getParentChoices(){
        return this.parentChoices;
    }

    @Override
    public void init(EntityPlayerMP playerMP){
        CustomGuiWrapper parentGui = CustomGuiController.getOpenGui(playerMP);
        this.parentChoices = new String[6];
        this.parentChoices[0] = ((CustomGuiTextFieldWrapper)parentGui.getComponent(401)).getText();
        this.parentChoices[1] = ((CustomGuiTextFieldWrapper)parentGui.getComponent(402)).getText();
        this.parentChoices[2] = ((CustomGuiTextFieldWrapper)parentGui.getComponent(403)).getText();
        this.parentChoices[3] = ((CustomGuiTextFieldWrapper)parentGui.getComponent(405)).getText();
        this.parentChoices[4] = ((CustomGuiTextFieldWrapper)parentGui.getComponent(406)).getText();
        this.parentChoices[5] = ((CustomGuiTextFieldWrapper)parentGui.getComponent(407)).getText();
        CustomGuiWrapper gui = this.getGui();
        PlayerWrapper playerWrapper = new PlayerWrapper(playerMP);
        playerWrapper.showCustomGui(gui);
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(playerMP, this);
    }
}
