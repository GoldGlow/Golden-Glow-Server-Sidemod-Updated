package com.goldenglow.common.handlers;

import com.goldenglow.common.routes.Area;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;

public class TempHandler {
    public Map<EntityPlayer, Area> playerBlocks = new HashMap<EntityPlayer, Area>();
}
