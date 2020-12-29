package com.goldenglow.common.guis.pokehelper.bag;

import com.goldenglow.common.keyItems.OOItem;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;

public interface BagCategory {
    String getName();
    String[] getItemNames(EntityPlayerMP player);
    CustomGuiWrapper generateButtons(EntityPlayerMP player, CustomGuiWrapper gui, EssentialsGuis essentialsGuis);
    int[] getButtonIds();
    void storeItem(EntityPlayerMP playerMP, OOItem item);
    void withdrawItem(EntityPlayerMP playerMP, OOItem item);
    CustomGuiWrapper displayItem(EntityPlayerMP player, CustomGuiWrapper gui);
    OOItem getItem(EntityPlayerMP player, int index);
    void useItem(OOItem item);
}
