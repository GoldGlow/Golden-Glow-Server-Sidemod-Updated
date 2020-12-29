package com.goldenglow.common.util.actions.types.bag;

import com.goldenglow.common.data.player.IPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.guis.pokehelper.bag.BagMenu;
import com.goldenglow.common.util.GGLogger;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import moe.plushie.armourers_workshop.api.common.skin.type.ISkinType;
import moe.plushie.armourers_workshop.common.init.items.ItemSkin;
import moe.plushie.armourers_workshop.common.skin.type.chest.SkinChest;
import moe.plushie.armourers_workshop.common.skin.type.feet.SkinFeet;
import moe.plushie.armourers_workshop.common.skin.type.head.SkinHead;
import moe.plushie.armourers_workshop.common.skin.type.legs.SkinLegs;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ChangeClothesAction extends ActionBase {
    public ChangeClothesAction(){
        super("CHANGE_CLOTHES");
    }

    @Override
    public void doAction(EntityPlayerMP player, ActionData actionData){
        EssentialsGuis gui=PixelmonEssentials.essentialsGuisHandler.getGui(player);
        /*if(gui instanceof BagMenu){
            IPlayerData data=player.getCapability(OOPlayerProvider.OO_DATA, null);
            ItemStack item=data.getAWItems().get(((BagMenu) gui).getIndex());
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

                ItemStack armor = null;
                if(slot!=null) {
                    armor = player.getItemStackFromSlot(slot);
                    if(armor!=ItemStack.EMPTY){
                        if(armor.hasTagCompound()) {
                            NBTTagCompound armourNBT = armor.getTagCompound();
                            if (armourNBT.equals(nbt)) {
                                player.setItemStackToSlot(slot, ItemStack.EMPTY);
                                return;
                            }
                        }
                        player.setItemStackToSlot(slot, ItemStack.EMPTY);
                    }
                    armor=getItemBySlot(slot);
                    armor.setTagCompound(nbt);
                    GGLogger.info(armor.serializeNBT());
                    player.setItemStackToSlot(slot, armor);
                }
            }
        }*/
    }

    public static ItemStack getItemBySlot(EntityEquipmentSlot slot){
        switch(slot){
            case HEAD:
                return new ItemStack(Item.getByNameOrId("minecraft:iron_helmet"));
            case CHEST:
                return new ItemStack(Item.getByNameOrId("minecraft:iron_chestplate"));
            case LEGS:
                return new ItemStack(Item.getByNameOrId("minecraft:iron_leggings"));
            case FEET:
                return new ItemStack(Item.getByNameOrId("pixelmon:old_running_boots"));
            default:
                return null;
        }
    }
}
