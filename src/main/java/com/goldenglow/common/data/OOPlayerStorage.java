package com.goldenglow.common.data;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class OOPlayerStorage implements Capability.IStorage<IPlayerData> {

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IPlayerData> capability, IPlayerData instance, EnumFacing side) {
        final NBTTagCompound tag = new NBTTagCompound();
        tag.setString("theme_wild", instance.getWildTheme());
        tag.setString("theme_trainer", instance.getTrainerTheme());
        tag.setString("theme_pvp", instance.getPVPTheme());
        return tag;
    }

    @Override
    public void readNBT(Capability<IPlayerData> capability, IPlayerData instance, EnumFacing side, NBTBase nbt) {
        final NBTTagCompound tag = (NBTTagCompound)nbt;
        instance.setWildTheme(tag.getString("theme_wild"));
        instance.setTrainerTheme(tag.getString("theme_trainer"));
        instance.setPVPTheme(tag.getString("theme_pvp"));
    }
}
