package com.goldenglow.common.data;

import com.goldenglow.common.seals.SealManager;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
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

        if(instance.getEquippedSeals()!=null) {
            NBTTagList equippedSeals = new NBTTagList();
            for (int i = 0; i < instance.getEquippedSeals().length; i++) {
                String s = instance.getEquippedSeals()[i];
                if (s!=null && !s.isEmpty()) {
                    NBTTagCompound sealTag = new NBTTagCompound();
                    sealTag.setInteger("slot", i);
                    sealTag.setString("name", instance.getEquippedSeals()[i]);
                    equippedSeals.appendTag(sealTag);
                }
            }
            if (!equippedSeals.isEmpty())
                tag.setTag("equippedSeals", equippedSeals);
        }

        NBTTagList unlockedSeals = new NBTTagList();
        for(String s : instance.getUnlockedSeals()) {
            unlockedSeals.appendTag(new NBTTagString(s));
        }
        if(!unlockedSeals.isEmpty())
            tag.setTag("unlockedSeals", unlockedSeals);

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
        if(tag.hasKey("equippedSeals")) {
            NBTTagList list = tag.getTagList("equippedSeals", 8);
            for(NBTBase n : list) {
                NBTTagCompound seal = (NBTTagCompound)n;
                if(SealManager.loadedSeals.containsKey(seal.getString("name"))) {
                    String[] equipped = instance.getEquippedSeals();
                    equipped[seal.getInteger("slot")] = seal.getString("name");
                    instance.setPlayerSeals(equipped);
                }
            }
        }
        if(tag.hasKey("unlockedSeals")) {
            NBTTagList list = tag.getTagList("unlockedSeals", 8);
            for(NBTBase n : list) {
                NBTTagString string = (NBTTagString)n;
                if(SealManager.loadedSeals.containsKey(string.getString())) {
                    instance.unlockSeal(string.getString());
                }
            }
        }
    }
}
