package com.goldenglow.common.util.actions.types;

import com.goldenglow.common.util.actions.Action;
import net.minecraft.command.ICommandManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.rcon.RConConsoleSource;
import noppes.npcs.api.wrapper.PlayerWrapper;

public class CommandAction implements Action {
    public final String name="COMMAND";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP playerMP){
        PlayerWrapper playerWrapper=new PlayerWrapper(playerMP);
        String command=value.replace("@dp",playerMP.getName());
        ICommandManager icommandmanager = playerMP.getEntityWorld().getMinecraftServer().getCommandManager();
        icommandmanager.executeCommand(new RConConsoleSource(playerWrapper.getMCEntity().getEntityWorld().getMinecraftServer()), command);
    }
}
