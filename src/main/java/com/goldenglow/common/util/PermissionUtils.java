package com.goldenglow.common.util;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.Tristate;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.common.node.factory.NodeFactory;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.List;

public class PermissionUtils {
    public static void unsetPermissionsWithStart(EntityPlayerMP player, String permissionStart){
        User user= LuckPerms.getApi().getUser(player.getName());
        List<Node> nodes=user.getOwnNodes();
        for(Node node: nodes){
            if(node.getPermission().startsWith(permissionStart)){
                user.unsetPermission(node);
            }
        }
    }

    public static void setPrefix(EntityPlayerMP player, String prefix){
        User user= LuckPerms.getApi().getUser(player.getName());
        Node.Builder node= NodeFactory.buildPrefixNode(3, prefix);
        user.setPermission(node.build());
    }

    public static void addPermissionNode(EntityPlayerMP player, String node){
        User user= LuckPerms.getApi().getUser(player.getName());
        user.setPermission(NodeFactory.make(node));
    }

    public static boolean checkPermission(EntityPlayerMP player, String node){
        User user= LuckPerms.getApi().getUser(player.getName());
        Tristate value= user.hasPermission(NodeFactory.make(node));
        if(value==Tristate.TRUE){
            return true;
        }
        return false;
    }
}
