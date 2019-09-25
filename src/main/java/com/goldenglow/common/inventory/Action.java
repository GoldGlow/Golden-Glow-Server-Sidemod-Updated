package com.goldenglow.common.inventory;

import com.goldenglow.common.data.OOPlayerData;
import com.goldenglow.common.data.OOPlayerProvider;
import com.goldenglow.common.util.Requirement;
import net.minecraft.command.ICommandManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.network.rcon.RConConsoleSource;
import noppes.npcs.api.wrapper.PlayerWrapper;

/**
 * Created by JeanMarc on 6/19/2019.
 */
public class Action {
    public ActionType actionType;
    public String value;
    public Requirement[] requirements;
    public boolean closeInv;

    public Action(){
        this.actionType=ActionType.COMMAND;
        this.value="";
        this.requirements=new Requirement[0];
        this.closeInv=true;
    }

    public Action(ActionType type, String value) {
        this();
        this.actionType = type;
        this.value = value;
    }

    public ActionType getActionType(){
        return this.actionType;
    }

    public Action setActionType(ActionType actionType){
        this.actionType=actionType;
        return this;
    }

    public String getValue(){
        return this.value;
    }

    public Action setValue(String value){
        this.value=value;
        return this;
    }

    public Requirement[] getRequirements(){
        return this.requirements;
    }

    public void setRequirements(Requirement[] requirements){
        this.requirements=requirements;
    }

    public void doAction(EntityPlayerMP player){
        if(this.closeInv)
            player.closeScreen();
        if(this.actionType==ActionType.COMMAND){
            PlayerWrapper playerWrapper=new PlayerWrapper(player);
            String command=this.getValue().replace("@dp",player.getName());
            ICommandManager icommandmanager = player.getEntityWorld().getMinecraftServer().getCommandManager();
            icommandmanager.executeCommand(new RConConsoleSource(playerWrapper.getMCEntity().getEntityWorld().getMinecraftServer()), command);
        }
        else if(this.actionType==ActionType.GIVEITEM){
            try {
                player.inventory.addItemStackToInventory(new ItemStack(JsonToNBT.getTagFromJson(this.value)));
            } catch (NBTException e) {
                e.printStackTrace();
            }
        }
        else if(this.actionType==ActionType.CHANGESKIN){
            String[] words=this.value.split(" ");
            String name="";
            player.getHeldItemMainhand().setItemDamage(Integer.parseInt(words[0]));
            for(int i=1;i<words.length;i++){
                if(i>1){
                    name+=" ";
                }
                name+=words[i];
            }
            player.getHeldItemMainhand().setStackDisplayName(name);
        }
        else if(this.actionType==ActionType.OPEN_INV) {
            //player.closeScreen();
            CustomInventory.openInventory(this.value, player);
        }
        else if(this.actionType==ActionType.SEAL_SET) {
            OOPlayerData data = (OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null);
            String[] equippedSeals = data.getEquippedSeals();
            equippedSeals[Integer.parseInt(value.split(":")[1])] = value.split(":")[0];
            data.setPlayerSeals(equippedSeals);
            player.closeScreen();
            CustomInventory.openInventory("seals", player);
        }
    }

    public enum ActionType{
        COMMAND,
        GIVEITEM,
        CHANGESKIN,
        OPEN_INV,
        SEAL_SET
    }
}
