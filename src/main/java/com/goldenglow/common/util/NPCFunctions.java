package com.goldenglow.common.util;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.battles.CustomBattleHandler;
import com.goldenglow.common.teams.Team;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.SelectPokemonListPacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.ShopKeeperPacket;
import com.pixelmonmod.pixelmon.config.StarterList;
import com.pixelmonmod.pixelmon.entities.npcs.registry.EnumBuySell;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EnumSpecialTexture;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.enums.*;
import com.pixelmonmod.pixelmon.items.ItemPokeball;
import com.pixelmonmod.pixelmon.listener.EntityPlayerExtension;
import com.pixelmonmod.pixelmon.storage.*;
import net.minecraft.client.particle.Barrier;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.common.DimensionManager;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.Server;
import noppes.npcs.api.block.IBlockScripted;
import noppes.npcs.api.constants.RoleType;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleFollower;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NPCFunctions {

    public static void openStarterGui(EntityPlayerMP player) {
        //Pixelmon.instance.network.sendTo(new SelectPokemonListPacket(StarterList.getStarterList()), player);
    }

    public static void createCustomBattle(EntityPlayerMP player, String teamName, int winDialogID, int loseDialogID, EntityNPCInterface npc) {
        CustomBattleHandler.createCustomBattle(player, teamName, winDialogID, loseDialogID, npc);
    }

    public static void standardNPCBattle(EntityPlayerMP player, String teamName, int winDialogID, int loseDialogID, EntityNPCInterface npc){
        createCustomBattle(player, teamName, winDialogID, loseDialogID, npc);
    }

    public static void setCamera(EntityPlayerMP player, int posX, int posY, int posZ, int targetX, int targetY, int targetZ) {
        //Pixelmon.network.sendTo(new ClientCameraPacket(posX, posY, posZ, targetX, targetY, targetZ), player);
    }

    public static void releaseCamera(EntityPlayer player)
    {
        //BetterStorage.networkChannel.sendTo(new PacketGGSidemod(EnumGGPacketType.RESET_CAMERA), player);
    }
}
