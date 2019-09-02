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
import com.goldenglow.common.routes.RouteDebugUtils;
import com.goldenglow.common.tiles.ICustomScript;
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
import net.minecraft.util.text.TextComponentString;
import noppes.npcs.Server;
import noppes.npcs.api.entity.data.IData;
import noppes.npcs.api.wrapper.BlockScriptedWrapper;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.WorldWrapper;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.QuestData;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.HashMap;

public class NPCFunctions {

    private static long lastDailyRefresh = 0;

	public static void playSound(EntityPlayerMP player, String source, String path){
        SongManager.playSound(player, source, path);
    }

    public static void playSong(EntityPlayerMP player){
	    if(player.getEntityData().hasKey("Song"))
            Server.sendData(player, EnumPacketClient.PLAY_MUSIC, player.getEntityData().getString("Song"));
    }

    public static void showAchievement(PlayerWrapper playerWrapper, String firstLine, String secondLine){
        playerWrapper.sendNotification(firstLine, secondLine, Integer.valueOf(playerWrapper.getMCEntity().getEntityData().getInteger("RouteNotification")));
    }

    public static void warpToSafeZone(PlayerWrapper playerWrapper){
        Route safeZone= GoldenGlow.routeManager.getRoute(playerWrapper.getMCEntity().getEntityData().getString("safeZone"));
        safeZone.warp((EntityPlayerMP)playerWrapper.getMCEntity());
        playerWrapper.message("You whited out!");
        ICommandManager icommandmanager = playerWrapper.getMCEntity().getEntityWorld().getMinecraftServer().getCommandManager();
        icommandmanager.executeCommand(new RConConsoleSource(playerWrapper.getMCEntity().getEntityWorld().getMinecraftServer()), "pokeheal "+playerWrapper.getName());
    }

    public static void createNPCBattle(NPCWrapper firstNPC, String firstTeamName, NPCWrapper secondNPC, String secondTeamName){
        EntityNPCInterface firstNpc=(EntityNPCInterface) firstNPC.getMCEntity();
        EntityNPCInterface secondNpc=(EntityNPCInterface) secondNPC.getMCEntity();
        CustomBattleHandler.createCustomNPCBattle(firstNpc, firstTeamName, secondNpc, secondTeamName);
    }

    public static void createCustomBattle(PlayerWrapper playerWrapper, String teamName, int initDialogID, int winDialogID, int loseDialogID, NPCWrapper npcWrapper) {
        EntityNPCInterface npc=(EntityNPCInterface) npcWrapper.getMCEntity();
        EntityPlayerMP player=(EntityPlayerMP)playerWrapper.getMCEntity();
        if(playerWrapper.hasPermission("hard")){
            teamName+="-hard";
        }
        CustomBattleHandler.createCustomBattle(player, teamName, initDialogID, winDialogID, loseDialogID, npc);
    }

    public static void registerLOSBattle(NPCWrapper npc, int initDialogID) {
        TickHandler.battleNPCs.put(npc, initDialogID);
    }

    public static void createCustomChest(EntityPlayerMP playerMP, String inventoryName){
        CustomInventoryData data=null;
        for(CustomInventoryData inventoryData:GoldenGlow.customInventoryHandler.inventories){
            if(inventoryData.getName().equals(inventoryName)){
                data=inventoryData;
            }
        }
        if(data!=null){
        }
    }

    public static void createInstancedInv(EntityPlayerMP playerMP, String[] items, String containerName, int questID) {
        HashMap<Integer, QuestData> data = PlayerData.get(playerMP).questData.activeQuests;
        if(data.containsKey(questID)) {
            QuestData qData = data.get(questID);
            if(!qData.isCompleted) {
                //Create Inventory with specified items
                InventoryBasic inv = new InventoryBasic(containerName, false, (9+(9 % items.length)));
                for (String tag : items) {
                    try {
                        ItemStack stack = new ItemStack(JsonToNBT.getTagFromJson(tag));
                        if(stack!=null) {
                            inv.addItem(stack);
                        }
                    } catch (NBTException e) {
                        e.printStackTrace();
                    }
                }
                //Show inventory to player
                playerMP.getNextWindowId();
                playerMP.connection.sendPacket(new SPacketOpenWindow(playerMP.currentWindowId, "minecraft:container", inv.getDisplayName(), inv.getSizeInventory()));
                playerMP.openContainer = new InstancedContainer(playerMP.inventory, inv, playerMP);
                playerMP.openContainer.windowId = playerMP.currentWindowId;
                playerMP.openContainer.addListener(playerMP);
                net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerContainerEvent.Open(playerMP, playerMP.openContainer));
            }
        }
    }

    public static void setAWModel(BlockScriptedWrapper block, String awItem){
        LibraryFile file=new LibraryFile(awItem);
        Skin skin = SkinIOUtils.loadSkinFromLibraryFile(file);
        CommonSkinCache.INSTANCE.addEquipmentDataToCache(skin, file);
        SkinIdentifier identifier = new SkinIdentifier(0, file, 0, skin.getSkinType());
        ItemStack item = SkinNBTHelper.makeEquipmentSkinStack(new SkinDescriptor(identifier));
        TileScripted tile = (TileScripted) block.getWorld().getMCWorld().getTileEntity(block.getPos().getMCBlockPos());
        tile.setItemModel(item, null);
        tile.setRotation(330, 180, 0);
        tile.setScale(1.25f, 1.25f, 1.25f);
    }

    /* OLD METHOD, BEFORE CAPABILITIES
    public static void checkRoute(EntityPlayerMP playerMP, int lastPosX, int lastPosY, int lastPosZ) {
	    Route currentRoute = null;
	    Route actualRoute = GoldenGlow.routeManager.getRoute(playerMP);

	    if(playerMP.getEntityData().hasKey("Route"))
	        currentRoute = GoldenGlow.routeManager.getRoute(playerMP.getEntityData().getString("Route"));

	    if(actualRoute!=null) {
	        if(actualRoute.canPlayerEnter(playerMP)) {
                if (currentRoute != null && !currentRoute.unlocalizedName.equalsIgnoreCase(actualRoute.unlocalizedName)) {
                    currentRoute.removePlayer(playerMP);
                }
                if(actualRoute.isSafeZone){
                    playerMP.getEntityData().setString("safeZone", actualRoute.unlocalizedName);
                }
                actualRoute.addPlayer(playerMP);
            }
            else if(currentRoute.unlocalizedName.equalsIgnoreCase(actualRoute.unlocalizedName)){
	            currentRoute.warp(playerMP);
            }
	        else {
	            playerMP.setPositionAndUpdate(lastPosX, lastPosY, lastPosZ);
	            TextComponentString msg = actualRoute.getRequirementMessage(playerMP);
	            playerMP.sendMessage(msg);
            }
        }
	    else {
	        if(currentRoute!=null)
	            currentRoute.removePlayer(playerMP);
        }
    }
    */

    public static void checkRoute(EntityPlayerMP playerMP, int lastPosX, int lastPosY, int lastPosZ) {
        IPlayerData playerData = playerMP.getCapability(OOPlayerProvider.OO_DATA, null);
	    Route currentRoute = null;
	    Route actualRoute = GoldenGlow.routeManager.getRoute(playerMP);
        String playerName=playerMP.getName();
        if(playerName.length()>13){
            playerName=playerName.substring(0,12);
        }

	    if(playerData.hasRoute())
	        currentRoute = playerData.getRoute();

	    if(actualRoute!=null) {
	        if(actualRoute.canPlayerEnter(playerMP)) {
                if (currentRoute != null && !currentRoute.unlocalizedName.equalsIgnoreCase(actualRoute.unlocalizedName)) {
                    currentRoute.removePlayer(playerMP);
                    actualRoute.addPlayer(playerMP);
                }
                if(actualRoute.isSafeZone){
                    playerData.setSafezone(actualRoute.unlocalizedName);
                }
                RouteDebugUtils.updateRouteDisplayName(playerMP);
            }
            else if(currentRoute!=null){
                if(currentRoute.unlocalizedName.equalsIgnoreCase(actualRoute.unlocalizedName)) {
                    currentRoute.warp(playerMP);
                    RouteDebugUtils.updateRouteDisplayName(playerMP);
                }
            }
	        else {
	            playerMP.setPositionAndUpdate(lastPosX, lastPosY, lastPosZ);
	            TextComponentString msg = actualRoute.getRequirementMessage(playerMP);
	            playerMP.sendMessage(msg);
	            RouteDebugUtils.updateRouteDisplayName(playerMP);
            }
        }
	    else {
	        if(currentRoute!=null) {
                currentRoute.removePlayer(playerMP);
                playerData.clearRoute();
                if(playerData.getHasRouteDebug())
                    RouteDebugUtils.updateRouteDisplayName(playerMP);
            }
        }
    }

    public static void removeRouteLogout(EntityPlayerMP playerMP) {
	    if(GoldenGlow.routeManager.getRoute(playerMP)!=null) {
            GoldenGlow.routeManager.getRoute(playerMP).removePlayer(playerMP);
        }
        playerMP.getEntityData().removeTag("Route");
	    playerMP.getEntityData().removeTag("Song");
    }

    public static int getCurrentDay(WorldWrapper world) {
        return (int)(world.getTime() / 24000L % 2147483647L);
    }

    public static boolean newRLDay(WorldWrapper world) {
        IData worldData = world.getStoreddata();
        if(lastDailyRefresh==0) {
            if(worldData.has("dailyRefresh")) {
                lastDailyRefresh = (long)worldData.get("dailyRefresh");
            }
            else {
                lastDailyRefresh = System.currentTimeMillis();
                worldData.put("dailyRefresh", lastDailyRefresh);
            }
        }
        if(System.currentTimeMillis() - lastDailyRefresh >= 86400000L) {
            lastDailyRefresh = System.currentTimeMillis();
            worldData.put("dailyRefresh", lastDailyRefresh);
            return true;
        }
        return false;
    }
}
