package com.goldenglow.common.util.requirements;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.util.requirements.types.*;
import com.pixelmonessentials.PixelmonEssentials;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import java.util.List;

import java.util.ArrayList;

public class RequirementHandler {

    public static void init(){
        PixelmonEssentials.requirementHandler.addRequirement(new FriendOnlyRequirement());
        PixelmonEssentials.requirementHandler.addRequirement(new TimeRequirement());
    }
}
