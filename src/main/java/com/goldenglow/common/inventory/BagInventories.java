package com.goldenglow.common.inventory;

import com.goldenglow.common.data.player.OOPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.Reference;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import com.pixelmonessentials.common.api.requirement.RequirementData;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.items.ItemTM;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;

import java.util.ArrayList;
import java.util.List;

public class BagInventories {
    public static void openKeyItems(EntityPlayerMP player) {
    }

    public static void openTMMenu(EntityPlayerMP player, String attackName){
        PlayerPartyStorage partyStorage = Pixelmon.storageManager.getParty(player);
        ItemStack[] party=CustomInventory.getPartyIcons(player);
        CustomInventoryData data=new CustomInventoryData(1, "TMCase", "TM "+attackName, new CustomItem[6][], new RequirementData[0]);
        InventoryBasic chestInventory=new InventoryBasic("TM "+attackName, true, 9);
        for(int i=0;i<6;i++){
            if(partyStorage.get(i)!=null) {
                ArrayList<String> lore=new ArrayList<>();
                if(canLearnTM(partyStorage.get(i), attackName)){
                    if(partyStorage.get(i).getMoveset().hasAttack(new Attack(attackName))){
                        party[i].setStackDisplayName(Reference.red + "Already learned");
                        data.items[i] = new CustomItem[]{new CustomItem(party[i], null)};
                        ActionData teachMove = new ActionStringData("TEACH_MOVE", i + ":" + attackName);
                    }
                    else {
                        party[i].setStackDisplayName(Reference.green + "Can learn");
                        data.items[i] = new CustomItem[]{new CustomItem(party[i], null)};
                        ActionData teachMove = new ActionStringData("TEACH_MOVE", i + ":" + attackName);
                        data.items[i][0].setRightClickActions(new ActionData[]{teachMove});
                        data.items[i][0].setLeftClickActions(new ActionData[]{teachMove});
                    }
                }
                else {
                    party[i].setStackDisplayName(Reference.red+"Cannot learn");
                    data.items[i]=new CustomItem[]{new CustomItem(party[i], null)};
                }
                chestInventory.setInventorySlotContents(i, party[i]);
            }
        }
        player.getNextWindowId();
        player.connection.sendPacket(new SPacketOpenWindow(player.currentWindowId, "minecraft:container", new TextComponentString("TM Case"), 9));
        player.openContainer = new CustomInventory(player.inventory, chestInventory, player);
        ((CustomInventory)player.openContainer).setData(data);
        player.openContainer.windowId = player.currentWindowId;
        player.openContainer.addListener(player);
        MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(player, player.openContainer));
    }

    public static boolean canLearnTM(Pokemon pokemon, String moveName){
        List<Attack> tms= pokemon.getBaseStats().tmMoves;
        for(Attack tm:tms){
            if(tm.getActualMove().getAttackName().equals(moveName)){
                return true;
            }
        }
        return false;
    }
}
