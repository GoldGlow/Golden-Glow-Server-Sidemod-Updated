package com.goldenglow.common.util;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.battles.CustomBattleHandler;
import com.goldenglow.common.data.IPlayerData;
import com.goldenglow.common.data.OOPlayerProvider;
import com.goldenglow.common.handlers.TickHandler;
import com.goldenglow.common.inventory.CustomInventoryData;
import com.goldenglow.common.inventory.InstancedContainer;
import com.goldenglow.common.music.SongManager;
import com.goldenglow.common.routes.Route;
import moe.plushie.armourers_workshop.common.library.LibraryFile;
import moe.plushie.armourers_workshop.common.skin.cache.CommonSkinCache;
import moe.plushie.armourers_workshop.common.skin.data.Skin;
import moe.plushie.armourers_workshop.common.skin.data.SkinDescriptor;
import moe.plushie.armourers_workshop.common.skin.data.SkinIdentifier;
import moe.plushie.armourers_workshop.utils.SkinIOUtils;
import moe.plushie.armourers_workshop.utils.SkinNBTHelper;
import net.minecraft.command.ICommandManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.network.rcon.RConConsoleSource;
import noppes.npcs.Server;
import noppes.npcs.api.IWorld;
import noppes.npcs.api.block.IBlock;
import noppes.npcs.api.entity.data.IData;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.api.wrapper.BlockScriptedWrapper;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.WorldWrapper;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.QuestData;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.quests.QuestInterface;
import noppes.npcs.quests.QuestItem;

import java.util.HashMap;

public class NPCFunctions {

    public static void showAchievement(PlayerWrapper playerWrapper, String firstLine, String secondLine){
        playerWrapper.sendNotification(firstLine, secondLine, Integer.valueOf(playerWrapper.getMCEntity().getEntityData().getInteger("RouteNotification")));
    }

    public static void test() {
	    System.out.println("Test works");
    }
}
