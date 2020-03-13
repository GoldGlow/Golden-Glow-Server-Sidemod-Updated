package com.goldenglow.common.util.actions.types;

import com.goldenglow.common.util.actions.Action;
import net.minecraft.command.ICommandManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.network.rcon.RConConsoleSource;
import noppes.npcs.api.wrapper.PlayerWrapper;

public class GiveItemAction implements Action {
    public final String name="GIVEITEM";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        try {
            player.inventory.addItemStackToInventory(new ItemStack(JsonToNBT.getTagFromJson(value)));
        } catch (NBTException e) {
            e.printStackTrace();
        }
    }
}
