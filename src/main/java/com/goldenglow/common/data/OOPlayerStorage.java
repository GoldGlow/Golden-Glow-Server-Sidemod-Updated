package com.goldenglow.common.data;

import com.goldenglow.common.seals.Seal;
import net.minecraft.nbt.*;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class OOPlayerStorage implements Capability.IStorage<IPlayerData> {

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IPlayerData> capability, IPlayerData instance, EnumFacing side) {
        final NBTTagCompound tag = new NBTTagCompound();
        if(instance.getWildTheme()!=null)
            tag.setString("theme_wild", instance.getWildTheme());
        if(instance.getTrainerTheme()!=null)
            tag.setString("theme_trainer", instance.getTrainerTheme());
        if(instance.getPVPTheme()!=null)
            tag.setString("theme_pvp", instance.getPVPTheme());
        tag.setInteger("notification_style", instance.getNotificationScheme());

        NBTTagList sealList = new NBTTagList();
        for(Seal s : instance.getPlayerSeals()) {
            //sealList.appendTag(new NBTTagInt(s.getID()));
        }
        if(!sealList.isEmpty())
            tag.setTag("seals", sealList);
        return tag;
    }

    @Override
    public void readNBT(Capability<IPlayerData> capability, IPlayerData instance, EnumFacing side, NBTBase nbt) {
        final NBTTagCompound tag = (NBTTagCompound)nbt;
        if(tag.hasKey("theme_wild"))
            instance.setWildTheme(tag.getString("theme_wild"));
        if(tag.hasKey("theme_trainer"))
            instance.setTrainerTheme(tag.getString("theme_trainer"));
        if(tag.hasKey("theme_pvp"))
            instance.setPVPTheme(tag.getString("theme_pvp"));
        instance.setNotificationScheme(tag.getInteger("notification_style"));
    }
}
