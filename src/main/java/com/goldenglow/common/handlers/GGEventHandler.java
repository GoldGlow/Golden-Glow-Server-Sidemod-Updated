package com.goldenglow.common.handlers;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.battles.CustomNPCBattle;
import com.goldenglow.common.battles.DoubleNPCBattle;
import com.goldenglow.common.battles.raids.RaidBattleRules;
import com.goldenglow.common.data.OOPlayerProvider;
import com.goldenglow.common.inventory.CustomInventory;
import com.goldenglow.common.inventory.CustomInventoryData;
import com.goldenglow.common.music.SongManager;
import com.goldenglow.common.tiles.ICustomScript;
import com.goldenglow.common.tiles.TileEntityCustomApricornTree;
import com.goldenglow.common.tiles.TileEntityCustomBerryTree;
import com.goldenglow.common.util.NPCFunctions;
import com.goldenglow.common.util.PixelmonBattleUtils;
import com.goldenglow.common.util.Reference;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.ApricornEvent;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.BerryEvent;
import com.pixelmonmod.pixelmon.api.events.LevelUpEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.api.events.spawning.PixelmonSpawnerEvent;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.blocks.apricornTrees.BlockApricornTree;
import com.pixelmonmod.pixelmon.blocks.enums.EnumBlockPos;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityApricornTree;
import com.pixelmonmod.pixelmon.comm.packetHandlers.PlayerDeath;
import com.pixelmonmod.pixelmon.config.PixelmonBlocksApricornTrees;
import com.pixelmonmod.pixelmon.enums.battle.BattleResults;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import com.pixelmonmod.pixelmon.items.ItemApricorn;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import noppes.npcs.EventHooks;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.wrapper.ItemScriptedWrapper;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.items.ItemScripted;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.inventory.InventoryArchetypes;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GGEventHandler {

    Map<UUID, Instant> playerTimes = new HashMap<>();

    @SubscribeEvent
    public void onPixelmonSpawner(PixelmonSpawnerEvent event){
        if(event.spawner.getWorld().isDaytime()){
            for(String pokemon:GoldenGlow.pixelmonSpawnerHandler.nightPokemon){
                if(event.spec.name.equals(pokemon))
                    event.setCanceled(true);
            }
        }
        else{
            for(String pokemon:GoldenGlow.pixelmonSpawnerHandler.dayPokemon){
                if(event.spec.name.equals(pokemon))
                    event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void playerLoginEvent(PlayerEvent.PlayerLoggedInEvent event) {
        if(!event.player.getEntityData().hasKey("RouteNotification"))
            event.player.getEntityData().setInteger("RouteNotification", 0);
        if(!event.player.getEntityData().hasKey("theme_wild"))
            event.player.getEntityData().setString("theme_wild", GoldenGlow.songManager.wildBattleSong);
        if(!event.player.getEntityData().hasKey("theme_trainer"))
            event.player.getEntityData().setString("theme_trainer", GoldenGlow.songManager.trainerBattleSong);
        if(!event.player.getEntityData().hasKey("theme_pvp"))
            event.player.getEntityData().setString("theme_pvp", GoldenGlow.songManager.trainerBattleSong);
        if(!event.player.getEntityData().hasKey("PlayTime"))
            event.player.getEntityData().setLong("PlayTime", 0);
        if(!event.player.getEntityData().hasKey("safeZone"))
            event.player.getEntityData().setString("safeZone", "Home");
        playerTimes.put(event.player.getUniqueID(), Instant.now());
    }

    @SubscribeEvent
    public void playerLogoutEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        /*Instant loginTime = playerTimes.get(event.player.getUniqueID());
        playerTimes.remove(event.player.getUniqueID());
        Instant logoutTime = Instant.now();
        Duration sessionDuration = Duration.between(loginTime, logoutTime);
        Duration totalDuration = Duration.ofSeconds(event.player.getEntityData().getLong("PlayTime")+sessionDuration.getSeconds());
        event.player.getEntityData().setLong("PlayTime", totalDuration.getSeconds());
        String time = String.format("%d:%02d:%02d", (int)totalDuration.getSeconds() / 3600, ((int)totalDuration.getSeconds() % 3600) / 60, ((int)totalDuration.getSeconds() % 60));
        int dex = Pixelmon.storageManager.getParty((EntityPlayerMP)event.player).pokedex.countCaught();
        int badges = 0; //ToDo: Change this when we decide how to handle badges.
        GoldenGlow.dataHandler.sendData(event.player.getName(), dex, badges, time);*/
    }

    @SubscribeEvent
    public void itemDroppedEvent(ItemTossEvent event){
        for(String id: GoldenGlow.phoneItemListHandler.itemIDs) {
            int item=Item.getIdFromItem(Item.getByNameOrId(id));
            if (Item.getIdFromItem(event.getEntityItem().getItem().getItem()) ==item){
                ItemStack itemStack = event.getEntityItem().getItem();
                event.setCanceled(true);
                event.getPlayer().inventory.addItemStackToInventory(itemStack);
                event.getPlayer().sendMessage(new TextComponentString(Reference.red+"Cannot drop this item!"));
            }
        }
    }

    @Listener
    public void itemStoredEvent(ClickInventoryEvent event){
        if(event.getTargetInventory() instanceof CustomInventory){
            event.setCancelled(true);
        }
        else if(event.getTargetInventory().getArchetype()!= InventoryArchetypes.PLAYER){
            for(String id: GoldenGlow.phoneItemListHandler.itemIDs) {
                if(event.getTransactions().get(0).getFinal().getType().getName().equals(id)){
                    event.setCancelled(true);
                }
            }
        }
    }

    /*@SubscribeEvent
    public void onEvolution(EvolveEvent event){
        if(event instanceof EvolveEvent.PostEvolve){
            NPCFunctions.stopSound(event.player, "music", GoldenGlow.songManager.evolutionSong);
            NPCFunctions.playSound(event.player, "music", GoldenGlow.routeManager.getRoute(event.player).song);
        }
        else {
            NPCFunctions.stopSound(event.player, "music", GoldenGlow.routeManager.getRoute(event.player).song);
            NPCFunctions.playSound(event.player, "music", GoldenGlow.songManager.evolutionSong);
            if (event.isCanceled()) {
                NPCFunctions.stopSound(event.player, "music", GoldenGlow.songManager.evolutionSong);
                NPCFunctions.playSound(event.player, "music", GoldenGlow.routeManager.getRoute(event.player).song);
            }
        }
    }*/

    @SubscribeEvent
    public void onBattleStart(BattleStartedEvent event){
        BattleParticipant[] participants=event.participant1;
        BattleParticipant[] opponents=event.participant2;
        if(event.bc.rules instanceof CustomNPCBattle) {
            for (BattleParticipant participant : participants) {
                if (participant instanceof PlayerParticipant) {
                    SongManager.setToTrainerMusic(((PlayerParticipant) participant).player);
                }
            }
        }
        else{
            boolean wildBattle= PixelmonBattleUtils.isWildBattle(opponents);
            if(wildBattle){
                for (BattleParticipant participant : participants) {
                    if (participant instanceof PlayerParticipant) {
                        SongManager.setToWildMusic(((PlayerParticipant) participant).player);
                    }
                }
            }
            if(!wildBattle){
                for(BattleParticipant participant: participants){
                    if(participant instanceof PlayerParticipant){
                        SongManager.setToPvpMusic(((PlayerParticipant) participant).player, opponents);
                    }
                }
                for(BattleParticipant participant: opponents){
                    if(participant instanceof PlayerParticipant){
                        SongManager.setToPvpMusic(((PlayerParticipant) participant).player, participants);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onLevelUp(LevelUpEvent event){
        SongManager.playSound(event.player, "neutral", GoldenGlow.songManager.levelUpSound);
    }

    @SubscribeEvent
    public void onBattleEnd(BattleEndEvent event)
    {
        if(event.bc.rules instanceof RaidBattleRules) {
            GoldenGlow.raidHandler.endBattle(event.bc);
        }
        else if(event.bc.rules instanceof CustomNPCBattle) {
            EntityPlayerMP mcPlayer = event.getPlayers().get(0);
            BattleResults results = event.results.get(event.bc.participants.get(0));
            Pixelmon.instance.network.sendTo(new PlayerDeath(), mcPlayer);
            CustomNPCBattle battle = (CustomNPCBattle) event.bc.rules;
            BattleRegistry.deRegisterBattle(event.bc);
            if (results == BattleResults.VICTORY) {
                SongManager.setCurrentSong(mcPlayer, GoldenGlow.songManager.victorySong);
                NoppesUtilServer.openDialog(mcPlayer, battle.getNpc(), battle.getWinDialog());
            }
            if (results == BattleResults.DEFEAT) {
                SongManager.setToRouteSong(mcPlayer);
                NoppesUtilServer.openDialog(mcPlayer, battle.getNpc(), battle.getLoseDialog());
            }
        }
        else if(event.bc.rules instanceof DoubleNPCBattle){
            DoubleNPCBattle rules=(DoubleNPCBattle)event.bc.rules;
            rules.getFirstNpc().getEntityData().setBoolean("inBattle", false);
            rules.getSecondNpc().getEntityData().setBoolean("inBattle", false);
            rules.getSecondPokemon().unloadEntity();
            rules.getFirstPokemon().unloadEntity();
        }
        else{
            for(BattleParticipant participant:event.bc.participants){
                if(participant instanceof PlayerParticipant){
                    if(event.results.get(participant)==BattleResults.DEFEAT){
                        NPCFunctions.warpToSafeZone(new PlayerWrapper(((PlayerParticipant) participant).player));
                    }
                    else {
                        SongManager.setToRouteSong(((PlayerParticipant) participant).player);
                    }
                }
            }
        }
        /*else if(event.battleController instanceof FactoryBattle && CustomBattleHandler.factoryBattles.contains(event.battleController)){
            EntityPlayerMP mcPlayer= event.player;
            Pixelmon.instance.network.sendTo(new PlayerDeath(), mcPlayer);
            FactoryBattle battle = (FactoryBattle)event.battleController;
            BattleRegistry.deRegisterBattle(battle);
            CustomBattleHandler.factoryBattles.remove(battle);
            PlayerWrapper player = new PlayerWrapper(mcPlayer);
            if(event.result==BattleResults.VICTORY){
                player.addFactionPoints(11, 1);
                NoppesUtilServer.openDialog(mcPlayer, battle.getNpc(), battle.getWinDialog());
            }
            else if(event.result==BattleResults.DEFEAT){
                player.addFactionPoints(11, -player.getFactionPoints(11));
                NoppesUtilServer.openDialog(mcPlayer, battle.getNpc(), battle.getLoseDialog());
            }
        }*/
    }

    @SubscribeEvent
    public void onPickApricorn(ApricornEvent.PickApricorn event) {
        if(event.tree instanceof TileEntityCustomApricornTree) {
            event.setCanceled(true);
            runOnPickEvent((TileEntityCustomApricornTree)event.tree, event);
        }
    }

    @SubscribeEvent
    public void onPickBerry(BerryEvent.PickBerry event) {
        event.player.sendMessage(new TextComponentString(""+event.tree));
        if(event.tree instanceof TileEntityCustomBerryTree) {
            event.setCanceled(true);
            runOnPickEvent((TileEntityCustomBerryTree)event.tree, event);
        }
    }

    @SubscribeEvent
    public void onPhoneItemRightClick(PlayerInteractEvent.RightClickItem event){
        for(String id: GoldenGlow.phoneItemListHandler.itemIDs) {
            int item=Item.getIdFromItem(Item.getByNameOrId(id));
            if (Item.getIdFromItem(event.getItemStack().getItem()) ==item){
                event.setCanceled(true);
                for(CustomInventoryData inventoryData:GoldenGlow.customInventoryHandler.inventories){
                    if(inventoryData.getName().equals("PokeHelper")){
                        CustomInventory.openCustomInventory((EntityPlayerMP) event.getEntityPlayer(), inventoryData);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onBlockRightClick(PlayerInteractEvent.RightClickBlock event) {
        if(event.getHand()==EnumHand.MAIN_HAND && event.getUseBlock()!=Event.Result.DENY && event.getWorld().getTileEntity(event.getPos())!=null
                && event.getWorld().getTileEntity(event.getPos()) instanceof ICustomScript) {
            ICustomScript tile = (ICustomScript)event.getWorld().getTileEntity(event.getPos());
            if (event.getItemStack().getItem() instanceof ItemScripted && !event.getEntityPlayer().isSneaking()) { // && new PlayerWrapper((EntityPlayerMP)event.getEntityPlayer()).hasPermission("goldglow.scripting")) {
                ItemScriptedWrapper item = (ItemScriptedWrapper)NpcAPI.Instance().getIItemStack(event.getEntityPlayer().getHeldItemMainhand());
                tile.getScriptedTile().setNBT(item.getScriptNBT(new NBTTagCompound()));
                tile.getScriptedTile().setEnabled(true);
                event.getEntityPlayer().sendMessage(new TextComponentString("Applied Script!"));
            } else {
                EventHooks.onScriptBlockInteract(tile.getScriptedTile(), event.getEntityPlayer(), 0, event.getPos().getX(),event.getPos().getY(),event.getPos().getZ());
            }
        }
    }

    void runOnPickEvent(ICustomScript tile, Event event) {
        for (final ScriptContainer scriptContainer4 : tile.getScriptedTile().scripts) {
            scriptContainer4.run("onPick", event);
        }
    }

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if(!(event.getObject() instanceof EntityPlayer)) return;

        event.addCapability(new ResourceLocation("obscureobsidian", "playerdata"), new OOPlayerProvider());
    }
}
