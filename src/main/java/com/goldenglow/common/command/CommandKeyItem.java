package com.goldenglow.common.command;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.data.player.IPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.quests.Objective;
import com.pixelmonmod.pixelmon.util.PixelmonPlayerUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.api.wrapper.PlayerWrapper;

public class CommandKeyItem extends CommandBase {
    public String getName() {
        return "keyitem";
    }

    public String getUsage(ICommandSender sender) {
        return "/keyitem <add/remove/gui> <username> <item name>";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length==1){
            if(args[0].equals("gui")){
            }
        }
        else if(args.length>2){
            EntityPlayerMP playerMP= PixelmonPlayerUtils.getUniquePlayerStartingWith(args[1]);
            PlayerWrapper playerWrapper=new PlayerWrapper(playerMP);
            IPlayerData playerData=playerMP.getCapability(OOPlayerProvider.OO_DATA, null);
            String keyItem="";
            for(int i=2;i<args.length;i++){
                keyItem+=args[i];
                if(i!=args.length-1){
                    keyItem+=" ";
                }
            }
            if(GoldenGlow.customItemManager.getKeyItem(keyItem)!=null){
                if(args[0].equalsIgnoreCase("add")){
                    playerData.addKeyItem(keyItem);

                }
                else if(args[0].equalsIgnoreCase("remove")){
                    playerData.removeKeyItem(keyItem);
                }
                Objective objective=PixelmonEssentials.questsManager.getObjectiveFromName("KEY_ITEM");
                IQuest[] quests=playerWrapper.getActiveQuests();
                for(IQuest quest: quests){
                    if(objective.getQuestFromId(quest.getId())!=null){
                        objective.calculateProgress(objective.getQuestFromId(quest.getId()), playerMP);
                    }
                }
            }
        }
        else{
            throw new WrongUsageException(getUsage(sender), new Object[0]);
        }
    }
}
