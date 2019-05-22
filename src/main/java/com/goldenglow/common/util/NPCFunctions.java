package com.goldenglow.common.util;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.battles.CustomBattleHandler;
import com.goldenglow.common.handlers.TickHandler;
import com.goldenglow.common.inventory.InstancedContainer;
import com.goldenglow.common.music.SongManager;
import com.goldenglow.common.routes.Route;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.sun.jna.Library;
import io.netty.buffer.Unpooled;
import moe.plushie.armourers_workshop.common.library.LibraryFile;
import moe.plushie.armourers_workshop.common.skin.cache.CommonSkinCache;
import moe.plushie.armourers_workshop.common.skin.data.Skin;
import moe.plushie.armourers_workshop.common.skin.data.SkinDescriptor;
import moe.plushie.armourers_workshop.common.skin.data.SkinDye;
import moe.plushie.armourers_workshop.common.skin.data.SkinIdentifier;
import moe.plushie.armourers_workshop.utils.SkinIOUtils;
import moe.plushie.armourers_workshop.utils.SkinNBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.Server;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.api.wrapper.BlockScriptedWrapper;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.PixelmonHelper;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.QuestData;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.HashMap;

public class NPCFunctions {

	public static void playSound(EntityPlayerMP player, String source, String path){
        SongManager.playSound(player, source, path);
    }

    public static void playSong(EntityPlayerMP player, String path){
	    SongManager.playSong(player, path);
    }

    public static void stopSong(EntityPlayerMP player){
	    SongManager.stopSong(player);
    }

    public static void openStarterGui(EntityPlayerMP player) {
        //Pixelmon.instance.network.sendTo(new SelectPokemonListPacket(StarterList.getStarterList()), player);
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

    public static void setCamera(EntityPlayerMP player, int posX, int posY, int posZ, int targetX, int targetY, int targetZ) {
        //Pixelmon.network.sendTo(new ClientCameraPacket(posX, posY, posZ, targetX, targetY, targetZ), player);
    }

    public static void releaseCamera(EntityPlayer player)
    {
        //BetterStorage.networkChannel.sendTo(new PacketGGSidemod(EnumGGPacketType.RESET_CAMERA), player);
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

    public static void setPixelmon(EntityCustomNpc npc, String name) {
        npc.modelData.setEntityClass(EntityPixelmon.class);
        npc.modelData.extra.setString("Name", name);
        try {
            EntityPixelmon mon = EntityPixelmon.class.getConstructor(new Class[]{World.class}).newInstance(new Object[]{npc.world});
            mon.readEntityFromNBT(npc.modelData.extra);
        } catch (Exception e) {
            e.printStackTrace();
        }
        npc.display.setSkinTexture(((EntityPixelmon)npc.modelData.getEntity(npc)).getTexture().toString());
    }

    public static void checkRoute(EntityPlayerMP playerMP) {
        Route currentRoute = null;
	    if(playerMP.getEntityData().hasKey("Route")) {
	        currentRoute = GoldenGlow.routeManager.getRoute(playerMP.getEntityData().getString("Route"));
        }
        Route actualRoute = GoldenGlow.routeManager.getRoute(playerMP);
	    if(actualRoute!=null && currentRoute != actualRoute) {
	        actualRoute.addPlayer(playerMP);
            NPCFunctions.stopSong(playerMP);
            NPCFunctions.playSong(playerMP, actualRoute.song);
//            if(actualRoute.displayName!=null && !actualRoute.displayName.isEmpty())
//                Server.sendData(playerMP, EnumPacketClient.MESSAGE, actualRoute.displayName, "", Integer.valueOf(playerMP.getEntityData().getInteger("RouteNotification")));
            Server.sendData(playerMP, EnumPacketClient.MESSAGE, (actualRoute.displayName!=null && actualRoute.displayName.isEmpty()) ? actualRoute.displayName : actualRoute.unlocalizedName, "", Integer.valueOf(playerMP.getEntityData().getInteger("RouteNotification")));
	        if(currentRoute!=null)
	            currentRoute.removePlayer(playerMP);
        } else if(actualRoute==null) {
	        playerMP.getEntityData().removeTag("Route");
        }
    }

    public static void removeRouteLogout(EntityPlayerMP playerMP) {
        GoldenGlow.routeManager.getRoute(playerMP.getEntityData().getString("Route")).removePlayer(playerMP);
        playerMP.getEntityData().removeTag("Route");
    }

    public static void joinRaid(EntityPlayerMP player) {
	    GoldenGlow.raidHandler.createRaidBattle(player);
    }
}
