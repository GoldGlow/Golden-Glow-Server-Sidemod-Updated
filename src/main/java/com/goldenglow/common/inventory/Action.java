package com.goldenglow.common.inventory;

import com.goldenglow.common.util.Requirement;
import net.minecraft.command.ICommandManager;
import net.minecraft.entity.player.EntityPlayerMP;
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

    public ActionType getActionType(){
        return this.actionType;
    }

    public void setActionType(ActionType actionType){
        this.actionType=actionType;
    }

    public String getValue(){
        return this.value;
    }

    public void setValue(String value){
        this.value=value;
    }

    public Requirement[] getRequirements(){
        return this.requirements;
    }

    public void setRequirements(Requirement[] requirements){
        this.requirements=requirements;
    }

    public void doAction(EntityPlayerMP player){
        if(this.actionType==ActionType.COMMAND){
            PlayerWrapper playerWrapper=new PlayerWrapper(player);
            String command=this.getValue().replace("@dp",player.getName());
            ICommandManager icommandmanager = player.getEntityWorld().getMinecraftServer().getCommandManager();
            icommandmanager.executeCommand(new RConConsoleSource(playerWrapper.getMCEntity().getEntityWorld().getMinecraftServer()), command);
        }
        if(this.closeInv)
            player.closeScreen();
    }

    public enum ActionType{
        COMMAND
    }
}
