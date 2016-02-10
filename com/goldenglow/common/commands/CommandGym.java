package com.goldenglow.common.commands;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.gyms.Gym;
import com.goldenglow.common.gyms.GymManager;
import com.goldenglow.common.util.BlockPos;
import com.goldenglow.common.util.Reference;
import java.util.List;
import java.util.Map;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandGym
        extends CommandBase
{
    public String func_71517_b()
    {
        return "gym";
    }

    public String func_71518_a(ICommandSender p_71518_1_)
    {
        return Reference.gymMessagePrefix + "Use '/gym help'";
    }

    public void func_71515_b(ICommandSender commandSender, String[] args)
    {
        if (args.length < 1)
        {
            commandSender.func_145747_a(new ChatComponentText(func_71518_a(commandSender)));
            return;
        }
        if (args[0].equalsIgnoreCase("help"))
        {
            if (isAdmin(commandSender)) {
                commandSender.func_145747_a(new ChatComponentText(Reference.gymMessagePrefix + "/gym list, /gym leaders <name>, /gym create <name> [levelCap], /gym remove <name>"));
            } else {
                commandSender.func_145747_a(new ChatComponentText(Reference.gymMessagePrefix + "/gym list, /gym leaders <name>"));
            }
        }
        else if (args[0].equalsIgnoreCase("list"))
        {
            if (GoldenGlow.instance.gymManager.gymList.isEmpty())
            {
                commandSender.func_145747_a(new ChatComponentText(Reference.messagePrefix + Reference.colorRed + "No gyms."));
                return;
            }
            StringBuilder list = new StringBuilder();
            for (Gym gym : GoldenGlow.instance.gymManager.gymList) {
                list.append(gym.name + Reference.bold + " [MaxLvl:" + gym.levelCap + "]" + EnumChatFormatting.RESET + ", ");
            }
            list.delete(list.length() - 2, list.length() - 1);
            commandSender.func_145747_a(new ChatComponentText(Reference.messagePrefix + list.toString()));
        }
        else
        {
            if (args[0].equalsIgnoreCase("join"))
            {
                if (args.length < 2)
                {
                    commandSender.func_145747_a(new ChatComponentText(Reference.gymMessagePrefix + Reference.colorRed + "/gym join <gymName>"));
                    return;
                }
                Gym gym = GoldenGlow.instance.gymManager.getGym(args[1]);
                if (gym == null)
                {
                    commandSender.func_145747_a(new ChatComponentText(Reference.gymMessagePrefix + Reference.colorRed + "Gym not found."));
                    return;
                }
                Object player = null;
                if ((args.length < 3) && ((commandSender instanceof EntityPlayer))) {
                    player = func_82359_c(commandSender, commandSender.func_70005_c_());
                } else {
                    player = func_82359_c(commandSender, args[2]);
                }
                if (gym.playerJoin((EntityPlayer)player)) {
                    commandSender.func_145747_a(new ChatComponentText(Reference.gymMessagePrefix + "Successfully added " + ((EntityPlayer)player).func_70005_c_() + " to gym queue."));
                } else {
                    commandSender.func_145747_a(new ChatComponentText(Reference.gymMessagePrefix + Reference.colorRed + "Unable to add player."));
                }
                return;
            }
            if (!args[0].equalsIgnoreCase("accept")) {
                if (args[0].equalsIgnoreCase("leader"))
                {
                    if (isGymLeader(commandSender))
                    {
                        if (!args[1].equalsIgnoreCase("next")) {}
                    }
                    else {
                        commandSender.func_145747_a(new ChatComponentText(Reference.gymMessagePrefix + Reference.colorRed + "You are not permitted to use these commands."));
                    }
                }
                else if (args[0].equalsIgnoreCase("admin")) {
                    if (isAdmin(commandSender))
                    {
                        if (args[1].equalsIgnoreCase("create"))
                        {
                            switch (args.length)
                            {
                                case 3:
                                    if (!(commandSender instanceof EntityPlayer)) {
                                        break;
                                    }
                                    GoldenGlow.instance.gymManager.gymList.add(new Gym(args[2], new BlockPos((int)((EntityPlayer)commandSender).field_70165_t, (int)((EntityPlayer)commandSender).field_70163_u, (int)((EntityPlayer)commandSender).field_70161_v)));
                                    commandSender.func_145747_a(new ChatComponentText(Reference.gymMessagePrefix + "Gym created! (Warp point set to current position. You still need to specify a level cap.)")); break;
                                case 4:
                                    if (!(commandSender instanceof EntityPlayer)) {
                                        break;
                                    }
                                    GoldenGlow.instance.gymManager.gymList.add(new Gym(args[2], Integer.parseInt(args[3]), new BlockPos((int)((EntityPlayer)commandSender).field_70165_t, (int)((EntityPlayer)commandSender).field_70163_u, (int)((EntityPlayer)commandSender).field_70161_v)));
                                    commandSender.func_145747_a(new ChatComponentText(Reference.gymMessagePrefix + "Gym created with level cap: " + Integer.parseInt(args[3]) + "! (Warp point set to current position.)")); break;
                                default:
                                    commandSender.func_145747_a(new ChatComponentText(Reference.gymMessagePrefix + Reference.colorRed + "/gym create <name> [levelCap]"));
                                    break;
                            }
                        }
                        else if (args[1].equalsIgnoreCase("remove"))
                        {
                            if (args.length < 3)
                            {
                                commandSender.func_145747_a(new ChatComponentText(Reference.gymMessagePrefix + Reference.colorRed + "/gym remove <name>"));
                                return;
                            }
                            GoldenGlow.instance.gymManager.gymList.remove(args[2]);
                            commandSender.func_145747_a(new ChatComponentText(Reference.gymMessagePrefix + "Successfully removed " + args[2]));
                        }
                        else if (args[1].equalsIgnoreCase("warp"))
                        {
                            switch (args.length)
                            {
                                case 3:
                                    if ((commandSender instanceof EntityPlayer))
                                    {
                                        EntityPlayer player1 = func_82359_c(commandSender, commandSender.func_70005_c_());
                                        Gym gym1 = GoldenGlow.instance.gymManager.getGym(args[2]);
                                        if (gym1 == null)
                                        {
                                            commandSender.func_145747_a(new ChatComponentText(Reference.gymMessagePrefix + Reference.colorRed + "Gym does not exist."));
                                            return;
                                        }
                                        player1.func_70107_b(gym1.warpPoint.x, gym1.warpPoint.y, gym1.warpPoint.z);
                                        player1.func_146105_b(new ChatComponentText(Reference.gymMessagePrefix + "Teleported to gym: " + gym1.name));
                                    }
                                    else
                                    {
                                        commandSender.func_145747_a(new ChatComponentText(Reference.gymMessagePrefix + Reference.colorRed + "/gym admin warp <gymName> [playerName]"));
                                    }
                                    break;
                                case 4:
                                    EntityPlayer player = func_82359_c(commandSender, args[3]);
                                    Gym gym = GoldenGlow.instance.gymManager.getGym(args[2]);
                                    if (gym == null)
                                    {
                                        commandSender.func_145747_a(new ChatComponentText(Reference.gymMessagePrefix + Reference.colorRed + "Gym does not exist."));
                                        return;
                                    }
                                    player.func_70107_b(gym.warpPoint.x, gym.warpPoint.y, gym.warpPoint.z);
                                    player.func_146105_b(new ChatComponentText(Reference.gymMessagePrefix + "Teleported to gym: " + gym.name));
                                    break;
                                default:
                                    commandSender.func_145747_a(new ChatComponentText(Reference.gymMessagePrefix + Reference.colorRed + "/gym admin warp <gymName> [playerName]"));
                                    break;
                            }
                        }
                    }
                    else {
                        commandSender.func_145747_a(new ChatComponentText(Reference.gymMessagePrefix + Reference.colorRed + "You aren't permitted to use these commands."));
                    }
                }
            }
        }
    }

    boolean isAdmin(ICommandSender commandSender)
    {
        if ((commandSender instanceof EntityPlayer))
        {
            EntityPlayer player = func_82359_c(commandSender, commandSender.func_70005_c_());
            if ((Bukkit.getPlayer(player.getPersistentID()).hasPermission("com.goldenglow.commands.CommandGym")) || (MinecraftServer.func_71276_C().func_71203_ab().func_152596_g(player.func_146103_bH()))) {
                return true;
            }
        }
        if ((commandSender instanceof CommandBlockLogic)) {
            return true;
        }
        return false;
    }

    boolean isGymLeader(ICommandSender commandSender)
    {
        EntityPlayer player;
        if ((commandSender instanceof EntityPlayer))
        {
            player = func_82359_c(commandSender, commandSender.func_70005_c_());
            for (Gym gym : GoldenGlow.instance.gymManager.gymList) {
                if (gym.gymLeaders.containsKey(player)) {
                    return true;
                }
            }
        }
        return false;
    }
}
