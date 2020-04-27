package com.goldenglow.common.util;

import net.minecraft.entity.player.EntityPlayerMP;

public interface PermissionUtils {
    void unsetPermissionsWithStart(EntityPlayerMP playerMP, String permission);
    void setPrefix(EntityPlayerMP playerMP, String prefix);
    void addPermissionNode(EntityPlayerMP playerMP, String node);
    boolean checkPermission(EntityPlayerMP playerMP, String node);
    String getPrefix(EntityPlayerMP playerMP);
    int getUnlockedPrefixTotal(EntityPlayerMP playerMP);
    int getPrefixTotal();
}
