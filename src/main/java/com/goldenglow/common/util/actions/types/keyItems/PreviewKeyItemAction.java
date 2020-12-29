package com.goldenglow.common.util.actions.types.keyItems;

import com.goldenglow.common.guis.essentials.KeyItemGui;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiTextFieldWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.controllers.CustomGuiController;

public class PreviewKeyItemAction extends ActionBase {
    public PreviewKeyItemAction(){
        super("PREVIEW_KEY");
    }

    @Override
    public void doAction(EntityPlayerMP playerMP, ActionData data){
        EssentialsGuis gui= PixelmonEssentials.essentialsGuisHandler.getGui(playerMP);
        if(gui instanceof KeyItemGui){
            ((KeyItemGui) gui).setEditMode(true);
            CustomGuiWrapper guiWrapper= CustomGuiController.getOpenGui(playerMP);
            if(((CustomGuiTextFieldWrapper)guiWrapper.getComponent(401)).getText()==null){
                guiWrapper.removeComponent(104);
                guiWrapper.addTexturedRect(1, ((CustomGuiTextFieldWrapper)guiWrapper.getComponent(401)).getText(), 110, 34, 256, 256).setScale(0.125F);
            }
            else{
                guiWrapper.addLabel(104, "No texture was entered", 40, 20, 128, 20, 16711680);
            }
            guiWrapper.update(new PlayerWrapper(playerMP));
        }
    }
}
