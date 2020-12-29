package com.goldenglow.common.guis.pokehelper.bag.category;

import com.goldenglow.common.data.player.IPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.guis.pokehelper.bag.BagMenu;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import moe.plushie.armourers_workshop.api.common.skin.type.ISkinType;
import moe.plushie.armourers_workshop.common.init.items.ItemSkin;
import moe.plushie.armourers_workshop.common.library.LibraryFile;
import moe.plushie.armourers_workshop.common.skin.cache.CommonSkinCache;
import moe.plushie.armourers_workshop.common.skin.data.Skin;
import moe.plushie.armourers_workshop.common.skin.data.SkinDescriptor;
import moe.plushie.armourers_workshop.common.skin.data.SkinIdentifier;
import moe.plushie.armourers_workshop.common.skin.type.chest.SkinChest;
import moe.plushie.armourers_workshop.common.skin.type.feet.SkinFeet;
import moe.plushie.armourers_workshop.common.skin.type.head.SkinHead;
import moe.plushie.armourers_workshop.common.skin.type.legs.SkinLegs;
import moe.plushie.armourers_workshop.utils.SkinIOUtils;
import moe.plushie.armourers_workshop.utils.SkinNBTHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;

import java.util.List;

public class ArmourersItems extends ItemCategoryBase{

    public ArmourersItems(){
        super("Clothes");
    }

    @Override
    public String[] getItemNames(EntityPlayerMP player){
        IPlayerData data=player.getCapability(OOPlayerProvider.OO_DATA, null);
        List<String> awItems=data.getAWItems();
        String[] items=new String[awItems.size()];
        for(int i=0;i<awItems.size();i++){
            String[] split=awItems.get(i).split("/");
            items[i]=split[split.length-1];
        }
        return items;
    }

    @Override
    public CustomGuiWrapper generateButtons(EntityPlayerMP player, CustomGuiWrapper gui, EssentialsGuis essentialsGui){
        if(essentialsGui instanceof BagMenu){
            int index=((BagMenu) essentialsGui).getIndex();
            if(isWearing(player, index)){
                gui.addTexturedButton(509, "Remove", 0, 180, 128, 20, "obscureobsidian:textures/gui/arrow_select.png", 0, 0);
            }
            else{
                gui.addTexturedButton(509, "Wear", 0, 180, 128, 20, "obscureobsidian:textures/gui/arrow_select.png", 0, 0);
            }
            gui.addTexturedButton(510, "Cancel", 0, 200, 128, 20, "obscureobsidian:textures/gui/arrow_select.png", 0, 0);
        }
        return gui;
    }

    @Override
    public int[] getButtonIds(){
        return new int[]{509, 510};
    }

    public boolean isWearing(EntityPlayerMP player, int index){
        IPlayerData data=player.getCapability(OOPlayerProvider.OO_DATA, null);
        String itemName=data.getAWItems().get(index);
        ItemStack item=createAWItem(itemName);
        if(item.getItem() instanceof ItemSkin){
            NBTTagCompound nbt=item.getTagCompound();
            ISkinType type=((ItemSkin) item.getItem()).getSkinType(item);
            EntityEquipmentSlot slot=null;
            if(type instanceof SkinHead){
                slot=EntityEquipmentSlot.HEAD;
            }
            else if(type instanceof SkinChest){
                slot=EntityEquipmentSlot.CHEST;
            }
            else if(type instanceof SkinLegs){
                slot=EntityEquipmentSlot.LEGS;
            }
            else if(type instanceof SkinFeet){
                slot=EntityEquipmentSlot.FEET;
            }
            if(slot!=null) {
                ItemStack armor = player.getItemStackFromSlot(slot);
                if(armor!=ItemStack.EMPTY){
                    if(armor.hasTagCompound()) {
                        return armor.getTagCompound().equals(nbt);
                    }
                }
            }
        }
        return false;
    }

    public ItemStack createAWItem(String name){
        LibraryFile file=new LibraryFile(name);
        Skin skin = SkinIOUtils.loadSkinFromLibraryFile(file);
        CommonSkinCache.INSTANCE.addEquipmentDataToCache(skin, file);
        SkinIdentifier identifier = new SkinIdentifier(0, file, 0, skin.getSkinType());
        return SkinNBTHelper.makeEquipmentSkinStack(new SkinDescriptor(identifier));
    }
}
