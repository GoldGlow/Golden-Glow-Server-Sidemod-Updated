package com.goldenglow.common.commands;
//Not Done
import com.goldenglow.GoldenGlow;
import com.goldenglow.common.gyms.Gym;
import com.goldenglow.common.util.Reference;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class CommandGym extends CommandBase {
    @Override
    public String getCommandName() {
        return "gym";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return Reference.gymMessagePrefix+"Use '/gym help'";
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] args) throws PlayerNotFoundException {
        if(args.length<1) {
            commandSender.addChatMessage(new ChatComponentText(getCommandUsage(commandSender)));
            return;
        }
        if(args[0].equalsIgnoreCase("help")) {
            if(isAdmin(commandSender)) {
                commandSender.addChatMessage(new ChatComponentText(Reference.gymMessagePrefix+"/gym list, /gym leaders <name>, /gym create <name> [levelCap], /gym remove <name>"));
            }else{
                commandSender.addChatMessage(new ChatComponentText(Reference.gymMessagePrefix+"/gym list, /gym leaders <name>"));
            }
        }
        else if(args[0].equalsIgnoreCase("list")) {
            if(GoldenGlow.instance.gymManager.gymList.isEmpty()) {
                commandSender.addChatMessage(new ChatComponentText(Reference.messagePrefix + Reference.colorRed + "No gyms."));
                return;
            }
            StringBuilder list = new StringBuilder();
            for(Gym gym : GoldenGlow.instance.gymManager.gymList) {
                list.append(gym.name+Reference.bold+" [MaxLvl:"+gym.levelCap+"]"+EnumChatFormatting.RESET+", ");
            }
            list.delete(list.length() - 2, list.length() - 1);
            commandSender.addChatMessage(new ChatComponentText(Reference.messagePrefix + list.toString()));
        }
        else if(args[0].equalsIgnoreCase("leaders")){
            if(args.length<2||args.length>2){
                commandSender.addChatMessage(new ChatComponentText(Reference.gymMessagePrefix + Reference.colorRed + "/gym leaders <gymName>"));
                return;
            }
            Gym gym = GoldenGlow.instance.gymManager.getGym(args[1]);
            if(gym==null) {
                commandSender.addChatMessage(new ChatComponentText(Reference.gymMessagePrefix + Reference.colorRed + "Gym not found."));
                return;
            }
            else{
                String leaders="";
                for(String leader : gym.gymLeaders.keySet()){
                    leaders+=leader+" ";
                }
                commandSender.addChatMessage(new ChatComponentText(Reference.gymMessagePrefix + args[1] + " Gym leaders: "+leaders));
            }
        }
        else if(args[0].equalsIgnoreCase("join")) {
            if(args.length<2) {
                commandSender.addChatMessage(new ChatComponentText(Reference.gymMessagePrefix + Reference.colorRed + "/gym join <gymName>"));
                return;
            }
            Gym gym = GoldenGlow.instance.gymManager.getGym(args[1]);
            if(gym==null) {
                commandSender.addChatMessage(new ChatComponentText(Reference.gymMessagePrefix + Reference.colorRed + "Gym not found."));
                return;
            }
            EntityPlayer player = null;
            if(args.length<3 && commandSender instanceof EntityPlayer) {
                try {
                    player = getPlayer(commandSender, commandSender.getName());
                } catch (PlayerNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    player = getPlayer(commandSender, args[2]);
                } catch (PlayerNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if(gym.playerJoin(player)) {
                commandSender.addChatMessage(new ChatComponentText(Reference.gymMessagePrefix + "Successfully added "+player.getName()+" to gym queue."));
            } else {
                commandSender.addChatMessage(new ChatComponentText(Reference.gymMessagePrefix + Reference.colorRed + "Unable to add player."));
            }
            return;
        }
        else if(args[0].equalsIgnoreCase("accept")) {

        }
        else if(args[0].equalsIgnoreCase("leader")) {
            if(isGymLeader(commandSender)) {
                if(args[1].equalsIgnoreCase("next")) {

                }
            }
            else {
                commandSender.addChatMessage(new ChatComponentText(Reference.gymMessagePrefix+Reference.colorRed+"You are not permitted to use these commands."));
            }
        } else if(args[0].equalsIgnoreCase("admin")) {
            if(isAdmin(commandSender)) {
                if(args[1].equalsIgnoreCase("create")) {
                    switch(args.length) {
                        case 3:
                            if(commandSender instanceof EntityPlayer) {
                                GoldenGlow.instance.gymManager.gymList.add(new Gym(args[2], new BlockPos((int)((EntityPlayer)commandSender).posX, (int)((EntityPlayer)commandSender).posY, (int)((EntityPlayer)commandSender).posZ)));
                                commandSender.addChatMessage(new ChatComponentText(Reference.gymMessagePrefix + "Gym created! (Warp point set to current position. You still need to specify a level cap.)"));
                            }
                            break;
                        case 4:
                            if(commandSender instanceof EntityPlayer) {
                                GoldenGlow.instance.gymManager.gymList.add(new Gym(args[2], Integer.parseInt(args[3]), new BlockPos((int)((EntityPlayer)commandSender).posX, (int)((EntityPlayer)commandSender).posY, (int)((EntityPlayer)commandSender).posZ)));
                                commandSender.addChatMessage(new ChatComponentText(Reference.gymMessagePrefix + "Gym created with level cap: "+Integer.parseInt(args[3])+"! (Warp point set to current position.)"));
                            }
                            break;
                        default:
                            commandSender.addChatMessage(new ChatComponentText(Reference.gymMessagePrefix+Reference.colorRed+"/gym create <name> [levelCap]"));
                            break;
                    }
                }
                else if(args[1].equalsIgnoreCase("remove")) {
                    if(args.length<3) {
                        commandSender.addChatMessage(new ChatComponentText(Reference.gymMessagePrefix+Reference.colorRed+"/gym remove <name>"));
                        return;
                    }
                    GoldenGlow.instance.gymManager.gymList.remove(args[2]);
                    commandSender.addChatMessage(new ChatComponentText(Reference.gymMessagePrefix+"Successfully removed "+args[2]));
                }
                else if(args[1].equalsIgnoreCase("warp")) {
                    switch(args.length) {
                        case 3:
                            if(commandSender instanceof EntityPlayer) {
                                EntityPlayer player1 = getPlayer(commandSender, commandSender.getName());
                                Gym gym1 = GoldenGlow.instance.gymManager.getGym(args[2]);
                                if(gym1==null) {
                                    commandSender.addChatMessage(new ChatComponentText(Reference.gymMessagePrefix + Reference.colorRed + "Gym does not exist."));
                                    return;
                                }
                                player1.setPosition(gym1.warpPoint.getX(),gym1.warpPoint.getY(),gym1.warpPoint.getZ());
                                player1.addChatComponentMessage(new ChatComponentText(Reference.gymMessagePrefix + "Teleported to gym: "+gym1.name));
                            }
                            else {
                                commandSender.addChatMessage(new ChatComponentText(Reference.gymMessagePrefix + Reference.colorRed + "/gym admin warp <gymName> [playerName]"));
                            }
                            break;
                        case 4:
                            EntityPlayer player = getPlayer(commandSender, args[3]);
                            Gym gym = GoldenGlow.instance.gymManager.getGym(args[2]);
                            if(gym==null) {
                                commandSender.addChatMessage(new ChatComponentText(Reference.gymMessagePrefix + Reference.colorRed + "Gym does not exist."));
                                return;
                            }
                            player.setPosition(gym.warpPoint.getX(),gym.warpPoint.getY(),gym.warpPoint.getZ());
                            player.addChatComponentMessage(new ChatComponentText(Reference.gymMessagePrefix + "Teleported to gym: "+gym.name));
                            break;
                        default:
                            commandSender.addChatMessage(new ChatComponentText(Reference.gymMessagePrefix+Reference.colorRed+"/gym admin warp <gymName> [playerName]"));
                            break;
                    }
                }
            } else {
                commandSender.addChatMessage(new ChatComponentText(Reference.gymMessagePrefix+Reference.colorRed+"You aren't permitted to use these commands."));
            }
        }
    }
    public int getRequiredPermissionLevel(){
        return 2;
    }

    boolean isAdmin(ICommandSender commandSender) throws PlayerNotFoundException {
        if(commandSender instanceof EntityPlayer) {
            EntityPlayer player = getPlayer(commandSender, commandSender.getName());
            if( player.canCommandSenderUseCommand(getRequiredPermissionLevel(), "com.goldenglow.commands.CommandGym")) {
                return true;
            }
        }
        if(commandSender instanceof CommandBlockLogic)
            return true;
        return false;
    }

    boolean isGymLeader(ICommandSender commandSender) throws PlayerNotFoundException {
        if(commandSender instanceof EntityPlayer) {
            EntityPlayer player = getPlayer(commandSender, commandSender.getName());
            for(Gym gym : GoldenGlow.instance.gymManager.gymList) {
                if(gym.gymLeaders.containsKey(player))
                    return true;
            }
        }
        return false;
    }

}