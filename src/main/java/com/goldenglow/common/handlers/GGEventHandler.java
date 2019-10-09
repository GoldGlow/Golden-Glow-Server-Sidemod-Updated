package com.goldenglow.common.handlers;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.battles.CustomNPCBattle;
import com.goldenglow.common.battles.DoubleNPCBattle;
import com.goldenglow.common.data.IPlayerData;
import com.goldenglow.common.data.OOPlayerProvider;
import com.goldenglow.common.inventory.CustomInventory;
import com.goldenglow.common.music.SongManager;
import com.goldenglow.common.seals.SealManager;
import com.goldenglow.common.tiles.ICustomScript;
import com.goldenglow.common.tiles.TileEntityCustomApricornTree;
import com.goldenglow.common.tiles.TileEntityCustomBerryTree;
import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.PixelmonBattleUtils;
import com.goldenglow.common.util.Reference;
import com.goldenglow.common.util.scripting.OtherFunctions;
import com.goldenglow.common.util.scripting.WorldFunctions;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.*;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.api.events.spawning.PixelmonSpawnerEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBase;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.blocks.BlockBerryTree;
import com.pixelmonmod.pixelmon.blocks.apricornTrees.BlockApricornTree;
import com.pixelmonmod.pixelmon.blocks.enums.EnumBlockPos;
import com.pixelmonmod.pixelmon.comm.packetHandlers.PlayerDeath;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.BattleResults;
import com.pixelmonmod.pixelmon.pokedex.EnumPokedexRegisterStatus;
import com.pixelmonmod.pixelmon.pokedex.Pokedex;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import noppes.npcs.EventHooks;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.Server;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.event.BlockEvent;
import noppes.npcs.api.wrapper.ItemScriptedWrapper;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.WrapperNpcAPI;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.IScriptBlockHandler;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.items.ItemScripted;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.inventory.InventoryArchetypes;

import java.time.Instant;
import java.util.ArrayList;
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
    public void pokedexRegisteredEvent(PokedexEvent event){
        Map<Integer, EnumPokedexRegisterStatus> seen=Pixelmon.storageManager.getParty(event.uuid).pokedex.getSeenMap();
        GGLogger.info(seen.size());
        ArrayList<Integer> caught=new ArrayList<Integer>();
        int bugTypes=0;
        for(Map.Entry<Integer, EnumPokedexRegisterStatus> entry:seen.entrySet()){
            if(entry.getValue()==EnumPokedexRegisterStatus.caught){
                caught.add(entry.getKey());
            }
        }
        GGLogger.info("Caught in dex: "+caught.size());
        for(int species:caught) {
            for (EnumType type : EnumSpecies.getFromDex(species).getBaseStats().types) {
                if (type == EnumType.Bug) {
                    bugTypes++;
                    if(bugTypes>=5){
                        OtherFunctions.unlockBugCatcher(Pixelmon.storageManager.getParty(event.uuid).getPlayer());
                        return;
                    }
                }
            }
        }
        GGLogger.info("Bug types: "+bugTypes);
    }

    //ToDo: Change login Event considering we now use a different way for storing this info.
    @SubscribeEvent
    public void playerLoginEvent(PlayerEvent.PlayerLoggedInEvent event) {
        playerTimes.put(event.player.getUniqueID(), Instant.now());
    }

    @SubscribeEvent
    public void playerLogoutEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        IPlayerData playerData= event.player.getCapability(OOPlayerProvider.OO_DATA, null);
        if(playerData.getHasRouteDebug()){
            event.player.getWorldScoreboard().removeObjective(event.player.getWorldScoreboard().getObjective("RD_"+event.player.getName()));
        }
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
        if ((event.getEntityItem().getItem().getItem().getRegistryName() + "").equals("variedcommodities:diamond_dagger")) {
            ItemStack itemStack = event.getEntityItem().getItem();
            event.setCanceled(true);
            event.getPlayer().inventory.addItemStackToInventory(itemStack);
            event.getPlayer().sendMessage(new TextComponentString(Reference.red + "Cannot drop this item!"));
        }
    }

    @Listener
    public void itemStoredEvent(ClickInventoryEvent event){
        if(event.getTargetInventory() instanceof CustomInventory){
            event.setCancelled(true);
        }
        else if(event.getTargetInventory().getArchetype()!= InventoryArchetypes.PLAYER){
            if (event.getTransactions().get(0).getFinal().getType().getName().equals("variedcommodities:diamond_dagger")) {
                event.setCancelled(true);
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
        SongManager.playSound(event.player, "neutral", SongManager.levelUpDefault);
    }

    @SubscribeEvent
    public void onBattleEnd(BattleEndEvent event)
    {
        if(event.bc.rules instanceof CustomNPCBattle) {
            EntityPlayerMP mcPlayer = event.getPlayers().get(0);
            BattleResults results = event.results.get(event.bc.participants.get(0));
            Pixelmon.instance.network.sendTo(new PlayerDeath(), mcPlayer);
            CustomNPCBattle battle = (CustomNPCBattle) event.bc.rules;
            BattleRegistry.deRegisterBattle(event.bc);
            if (results == BattleResults.VICTORY) {
                SongManager.setCurrentSong(mcPlayer, SongManager.victoryDefault);
                NoppesUtilServer.openDialog(mcPlayer, battle.getNpc(), battle.getWinDialog());
            }
            if (results == BattleResults.DEFEAT) {
                SongManager.setRouteSong(mcPlayer);
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
        else {
            for(BattleParticipant participant:event.bc.participants){
                if(participant instanceof PlayerParticipant){
                    if(event.results.get(participant)==BattleResults.DEFEAT){
                        WorldFunctions.warpToSafeZone(new PlayerWrapper(((PlayerParticipant) participant).player));
                    }
                    else {
                        SongManager.setRouteSong(((PlayerParticipant) participant).player);
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
        }
    }

    @SubscribeEvent
    public void onPickBerry(BerryEvent.PickBerry event) {
        if(event.tree instanceof TileEntityCustomBerryTree) {
            event.setCanceled(true);
        }
    }

    //ToDo: Possibly update this code for efficiency/to use more appropriate Forge methods.
    @SubscribeEvent
    public void onPhoneItemRightClick(PlayerInteractEvent.RightClickItem event){
        if((event.getItemStack().getItem().getRegistryName()+"").equals("variedcommodities:diamond_dagger")) {
            if (event.getItemStack().getItemDamage() >= 100 && event.getItemStack().getItemDamage() < 200) {
                event.setCanceled(true);
                CustomInventory.openInventory("PokeHelper", (EntityPlayerMP) event.getEntityPlayer());
            }
        }
    }

    @SubscribeEvent
    public void onBlockRightClick(PlayerInteractEvent.RightClickBlock event) {
        IBlockState blockState = event.getWorld().getBlockState(event.getPos());
        if(!blockState.getBlock().onBlockActivated(event.getWorld(), event.getPos(), blockState, event.getEntityPlayer(), event.getHand(), event.getFace(), (float)event.getHitVec().x, (float)event.getHitVec().y, (float)event.getHitVec().z)) {
            if((event.getItemStack().getItem().getRegistryName()+"").equals("variedcommodities:diamond_dagger")) {
                if (event.getItemStack().getItemDamage() >= 100 && event.getItemStack().getItemDamage() < 200) {
                    event.setCanceled(true);
                    CustomInventory.openInventory("PokeHelper", (EntityPlayerMP) event.getEntityPlayer());
                }
            }
        }
        if(event.getHand()==EnumHand.MAIN_HAND && event.getUseBlock()!=Event.Result.DENY ) {
            TileEntity tile = null;
            if(blockState.getBlock() instanceof BlockApricornTree || blockState.getBlock() instanceof BlockBerryTree) {
                if(blockState.getValue(BlockApricornTree.BLOCKPOS) == EnumBlockPos.TOP) {
                    tile = event.getWorld().getTileEntity(event.getPos().down());
                } else {
                    tile = event.getWorld().getTileEntity(event.getPos());
                }
            }
            if(tile instanceof ICustomScript) {
                ICustomScript customTile = (ICustomScript)tile;
                if (event.getItemStack().getItem() instanceof ItemScripted && !event.getEntityPlayer().isSneaking()) { // && new PlayerWrapper((EntityPlayerMP)event.getEntityPlayer()).hasPermission("goldglow.scripting")) {
                    ItemScriptedWrapper item = (ItemScriptedWrapper)NpcAPI.Instance().getIItemStack(event.getEntityPlayer().getHeldItemMainhand());
                    customTile.getScriptedTile().setNBT(item.getScriptNBT(new NBTTagCompound()));
                    customTile.getScriptedTile().setEnabled(true);

                    final BlockEvent.InitEvent initEvent = new BlockEvent.InitEvent(customTile.getScriptedTile().getBlock());
                    customTile.getScriptedTile().runScript(EnumScriptType.INIT, initEvent);
                    WrapperNpcAPI.EVENT_BUS.post(initEvent);

                    event.getEntityPlayer().sendMessage(new TextComponentString("Applied Script!"));
                } else {
                    EventHooks.onScriptBlockInteract( customTile.getScriptedTile(), event.getEntityPlayer(), 0, event.getPos().getX(),event.getPos().getY(),event.getPos().getZ());
                }
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

    @SubscribeEvent
    public void entityJoinWorld(EntityJoinWorldEvent event) {
        if(event.getEntity() instanceof EntityPixelmon) {
            Pokemon p = ((EntityPixelmon)event.getEntity()).getPokemonData();
            if(p.getOwnerPlayer()!= null) {
                SealManager.loadedSeals.get(0).execute(event.getEntity());
            }
        }
    }
}
