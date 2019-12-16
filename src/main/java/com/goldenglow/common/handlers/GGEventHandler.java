package com.goldenglow.common.handlers;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.battles.npc.CustomNPCBattle;
import com.goldenglow.common.battles.npc.DoubleNPCBattle;
import com.goldenglow.common.data.IPlayerData;
import com.goldenglow.common.data.OOPlayerProvider;
import com.goldenglow.common.events.CNPCBattleEvent;
import com.goldenglow.common.gyms.Gym;
import com.goldenglow.common.gyms.GymBattleRules;
import com.goldenglow.common.gyms.GymLeaderUtils;
import com.goldenglow.common.inventory.BetterTrading.TradeManager;
import com.goldenglow.common.inventory.CustomInventory;
import com.goldenglow.common.inventory.CustomItem;
import com.goldenglow.common.music.SongManager;
import com.goldenglow.common.seals.SealManager;
import com.goldenglow.common.teams.PlayerParty;
import com.goldenglow.common.tiles.ICustomScript;
import com.goldenglow.common.tiles.TileEntityCustomApricornTree;
import com.goldenglow.common.tiles.TileEntityCustomBerryTree;
import com.goldenglow.common.util.*;
import com.goldenglow.common.util.scripting.OtherFunctions;
import com.goldenglow.common.util.scripting.WorldFunctions;
import com.google.gson.stream.JsonWriter;
import com.mrcrayfish.furniture.tileentity.TileEntityTV;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.*;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.api.events.battles.TurnEndEvent;
import com.pixelmonmod.pixelmon.api.events.spawning.PixelmonSpawnerEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.blocks.BlockBerryTree;
import com.pixelmonmod.pixelmon.blocks.MultiBlock;
import com.pixelmonmod.pixelmon.blocks.apricornTrees.BlockApricornTree;
import com.pixelmonmod.pixelmon.blocks.enums.EnumBlockPos;
import com.pixelmonmod.pixelmon.blocks.enums.EnumMultiPos;
import com.pixelmonmod.pixelmon.blocks.multiBlocks.BlockFridge;
import com.pixelmonmod.pixelmon.comm.packetHandlers.PlayerDeath;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.BattleResults;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;
import com.pixelmonmod.pixelmon.pokedex.EnumPokedexRegisterStatus;
import io.github.eufranio.spongybackpacks.backpack.Backpack;
import io.github.eufranio.spongybackpacks.data.DataManager;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;
import moe.plushie.armourers_workshop.common.blocks.BlockSkinnable;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import noppes.npcs.EventHooks;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.event.BlockEvent;
import noppes.npcs.api.wrapper.ItemScriptedWrapper;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.WrapperNpcAPI;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.items.ItemScripted;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.text.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GGEventHandler {

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
    public void onEvolutionStart(EvolveEvent.PreEvolve event){
        IPlayerData playerData= event.player.getCapability(OOPlayerProvider.OO_DATA, null);
        playerData.setEvolvingPokemon(true);
        SongManager.setCurrentSong(event.player, GoldenGlow.songManager.evolutionDefault);
    }

    @SubscribeEvent
    public void onEvolutionEnd(EvolveEvent.PostEvolve event){
        IPlayerData playerData= event.player.getCapability(OOPlayerProvider.OO_DATA, null);
        playerData.setEvolvingPokemon(false);
        SongManager.setRouteSong(event.player);
        if(playerData.getWaitToEvolve().size()>0){
            playerData.removePokemonWaiting(0);
            TradeManager.evolutionTest(event.player);
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

    @SubscribeEvent
    public void playerLoginEvent(PlayerEvent.PlayerLoggedInEvent event) {
        event.player.getCapability(OOPlayerProvider.OO_DATA, null).setLoginTime(Instant.now());
        if(!event.player.getEntityData().hasKey("playtime")) {
            event.player.getEntityData().setLong("playtime", 0);
            DataManager.getDataFor(event.player.getUniqueID()).addBackpack(Backpack.of(Text.of("Pocket1"), event.player.getUniqueID(), 6));
        }
    }

    @SubscribeEvent
    public void playerLogoutEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        IPlayerData playerData = event.player.getCapability(OOPlayerProvider.OO_DATA, null);
        if (GoldenGlow.tradeManager.alreadyTrading((EntityPlayerMP) event.player)) {
            GoldenGlow.tradeManager.cancelTrade((EntityPlayerMP) event.player);
        }
        if (GoldenGlow.gymManager.leadingGym((EntityPlayerMP) event.player) != null) {
            GymLeaderUtils.stopTakingChallengers(GoldenGlow.gymManager.leadingGym((EntityPlayerMP) event.player), (EntityPlayerMP) event.player);
        } else if (GoldenGlow.gymManager.challengingGym((EntityPlayerMP) event.player) != null) {
            GymLeaderUtils.nextInQueue(GoldenGlow.gymManager.challengingGym((EntityPlayerMP) event.player), GoldenGlow.gymManager.challengingGym((EntityPlayerMP) event.player).currentLeader);
        }
        for (Gym gym : GoldenGlow.gymManager.getGyms()) {
            if (PermissionUtils.checkPermission(((EntityPlayerMP) event.player), "staff.gym_leader." + gym.getName().toLowerCase().replace(" ", "_"))) {
                if (GoldenGlow.gymManager.hasGymLeaderOnline(gym).size() == 1) {
                    GymLeaderUtils.closeGym(gym);
                }
            }
        }
        GoldenGlow.gymManager.removeFromQueues((EntityPlayerMP) event.player);

        try {
            if (playerData != null) {
                User user = LuckPerms.getApi().getUser(event.player.getUniqueID());
                File f = new File(Reference.statsDir, event.player.getUniqueID().toString() + ".json");
                if (!f.exists()) f.createNewFile();
                JsonWriter writer = new JsonWriter(new FileWriter(f));
                writer.setIndent("\t");

                writer.beginObject();

                int badgeCount = 0;
                for (Node n : user.getPermissions()) {
                    if (n.getPermission().startsWith("badge.") && n.getPermission().endsWith("npc"))
                        badgeCount++;
                }

                if(playerData.getLoginTime()!=null) {
                    long sessionTime = Math.abs(Duration.between(Instant.now(), playerData.getLoginTime()).getSeconds());
                    long totalTime = event.player.getEntityData().getLong("playtime") + sessionTime;
                    event.player.getEntityData().setLong("playtime", totalTime);

                    writer.name("badges").value(Integer.toString(badgeCount));
                    writer.name("dex").value(Integer.toString(Pixelmon.storageManager.getParty(event.player.getUniqueID()).pokedex.countCaught()));
                    writer.name("time").value(String.format("%sh:%sm", totalTime / 3600, (totalTime % 3600) / 60));

                    writer.endObject();
                    writer.close();
                }
            }
        }
        catch (IOException e) {
            GoldenGlow.logger.error("Error occurred saving player stats.");
            e.printStackTrace();
        }
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
            if (event.getTransactions().get(0).getFinal().getType().getName().equals("variedcommodities:diamond_dagger")&&!PermissionUtils.checkPermission(((EntityPlayerMP)event.getSource()), "*")) {
                event.setCancelled(true);
            }
        }
    }

    @SubscribeEvent
    public void onBattleStart(BattleStartedEvent event){
        BattleParticipant[] participants=event.participant1;
        BattleParticipant[] opponents=event.participant2;
        if(event.bc.rules instanceof GymBattleRules){
            GymBattleRules gymBattle=(GymBattleRules)event.bc.rules;
            for (BattleParticipant participant : participants) {
                if (participant instanceof PlayerParticipant) {
                    if(gymBattle.getGym().getPlayerTeams().containsKey(((PlayerParticipant) participant).player.getUniqueID())){
                        SongManager.setToPvpMusic(((PlayerParticipant) participant).player, opponents);
                        gymBattle.getGym().setTimeOfChallenge(((PlayerParticipant) participant).player);
                    }
                }
            }
            for (BattleParticipant participant : opponents) {
                if (participant instanceof PlayerParticipant) {
                    if(gymBattle.getGym().getPlayerTeams().containsKey(((PlayerParticipant) participant).player.getUniqueID())){
                        SongManager.setToPvpMusic(((PlayerParticipant) participant).player, participants);
                    }
                }
            }
        }
        else if(event.bc.rules instanceof CustomNPCBattle) {
            CustomNPCBattle battle=(CustomNPCBattle)event.bc.rules;
            NBTTagCompound data=battle.getNpc().getEntityData();
            for (BattleParticipant participant : participants) {
                if (participant instanceof PlayerParticipant) {
                    if(data.hasKey("battleTheme")){
                        SongManager.setCurrentSong(((PlayerParticipant) participant).player, data.getString("battleTheme"));
                    }
                    else {
                        SongManager.setToTrainerMusic(((PlayerParticipant) participant).player);
                    }
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
        SongManager.playSound(event.player, "neutral", GoldenGlow.songManager.levelUpDefault);
    }

    @SubscribeEvent
    public void onBattleEnd(BattleEndEvent event)
    {
        if(event.bc.rules instanceof GymBattleRules){
            BattleResults results=event.results.get(event.bc.participants.get(0));
            if (results == BattleResults.VICTORY && event.cause!=EnumBattleEndCause.FORCE) {
                PermissionUtils.addPermissionNode(((PlayerParticipant)event.bc.participants.get(0).getParticipantList()[0]).player, "badge."+((GymBattleRules) event.bc.rules).getGym().getName().replace(" ","_").toLowerCase()+".player");
                SongManager.setRouteSong(((PlayerParticipant)event.bc.participants.get(0).getParticipantList()[0]).player);
            }
            else{
                ((GymBattleRules) event.bc.rules).getGym().challengingPlayer.getCapability(OOPlayerProvider.OO_DATA, null).setBackupFullpos(null);
                ((GymBattleRules) event.bc.rules).getGym().challengingPlayer=null;
                WorldFunctions.warpToSafeZone(new PlayerWrapper(((PlayerParticipant)event.bc.participants.get(0).getParticipantList()[0]).player));
            }
            SongManager.setRouteSong(((PlayerParticipant)event.bc.participants.get(1).getParticipantList()[0]).player);
            PlayerParty.emptyParty(((GymBattleRules) event.bc.rules).getGym().currentLeader);
        }
        else if(event.bc.rules instanceof CustomNPCBattle) {
            EntityPlayerMP mcPlayer = event.getPlayers().get(0);
            BattleResults results = event.results.get(event.bc.participants.get(0));
            Pixelmon.instance.network.sendTo(new PlayerDeath(), mcPlayer);
            CustomNPCBattle battle = (CustomNPCBattle) event.bc.rules;
            BattleRegistry.deRegisterBattle(event.bc);
            if (results == BattleResults.VICTORY) {
                if(battle.getNpc().getEntityData().hasKey("victoryTheme")){
                    SongManager.setCurrentSong(mcPlayer, battle.getNpc().getEntityData().getString("victoryTheme"));
                }
                else{
                    SongManager.setCurrentSong(mcPlayer, GoldenGlow.songManager.victoryDefault);
                }
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
                    if(event.results.get(participant)==BattleResults.DEFEAT || (event.cause== EnumBattleEndCause.FORCE&&!(event.results.get(participant)==BattleResults.VICTORY))){
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
                if(GoldenGlow.gymManager.leadingGym((EntityPlayerMP) event.getEntityPlayer())!=null)
                    CustomInventory.openInventory("GYM:"+GoldenGlow.gymManager.leadingGym((EntityPlayerMP)event.getEntityPlayer()), (EntityPlayerMP) event.getEntityPlayer());
                else
                    CustomInventory.openInventory("PokeHelper", (EntityPlayerMP) event.getEntityPlayer());
            }
            else if(event.getItemStack().getItemDamage()==201){
                event.getEntityPlayer().setItemStackToSlot(EntityEquipmentSlot.HEAD, event.getItemStack().copy());
                event.getItemStack().setCount(0);
            }
        }
    }

    @SubscribeEvent
    public void onBlockRightClick(PlayerInteractEvent.RightClickBlock event) {
        IBlockState blockState = event.getWorld().getBlockState(event.getPos());
        /*
        if(!blockState.getBlock().onBlockActivated(event.getWorld(), event.getPos(), blockState, event.getEntityPlayer(), event.getHand(), event.getFace(), (float)event.getHitVec().x, (float)event.getHitVec().y, (float)event.getHitVec().z)) {
            if ((event.getItemStack().getItem().getRegistryName() + "").equals("variedcommodities:diamond_dagger")) {
                if (event.getItemStack().getItemDamage() >= 100 && event.getItemStack().getItemDamage() < 200) {
                    event.setCanceled(true);
                    CustomInventory.openInventory("PokeHelper", (EntityPlayerMP) event.getEntityPlayer());
                }
            }
        }*/
        if(event.getHand()==EnumHand.MAIN_HAND && event.getUseBlock()!=Event.Result.DENY ) {
            TileEntity tile = null;
            if(blockState.getBlock() instanceof BlockApricornTree || blockState.getBlock() instanceof BlockBerryTree) {
                if(blockState.getValue(BlockApricornTree.BLOCKPOS) == EnumBlockPos.TOP) {
                    tile = event.getWorld().getTileEntity(event.getPos().down());
                } else {
                    tile = event.getWorld().getTileEntity(event.getPos());
                }
            }
            else if(blockState.getBlock() instanceof BlockFridge){
                if(blockState.getValue(MultiBlock.MULTIPOS)== EnumMultiPos.TOP){
                    tile = event.getWorld().getTileEntity(event.getPos().down());
                }
                else{
                    tile = event.getWorld().getTileEntity(event.getPos());
                }
            }
            else if(blockState.getBlock() instanceof BlockSkinnable){
                tile = event.getWorld().getTileEntity(event.getPos());
            }
            if(tile instanceof ICustomScript) {
                ICustomScript customTile = (ICustomScript) tile;
                if (event.getItemStack().getItem() instanceof ItemScripted && !event.getEntityPlayer().isSneaking()) { // && new PlayerWrapper((EntityPlayerMP)event.getEntityPlayer()).hasPermission("goldglow.scripting")) {
                    ItemScriptedWrapper item = (ItemScriptedWrapper) NpcAPI.Instance().getIItemStack(event.getEntityPlayer().getHeldItemMainhand());
                    customTile.getScriptedTile().setNBT(item.getScriptNBT(new NBTTagCompound()));
                    customTile.getScriptedTile().setEnabled(true);
                    final BlockEvent.InitEvent initEvent = new BlockEvent.InitEvent(customTile.getScriptedTile().getBlock());
                    customTile.getScriptedTile().runScript(EnumScriptType.INIT, initEvent);
                    WrapperNpcAPI.EVENT_BUS.post(initEvent);
                    event.getEntityPlayer().sendMessage(new TextComponentString("Applied Script!"));
                } else {
                    EventHooks.onScriptBlockInteract(customTile.getScriptedTile(), event.getEntityPlayer(), 0, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ());
                }
            }
            else if(blockState.getBlock().getRegistryName().toString().equals("cfm:modern_tv")||blockState.getBlock().getRegistryName().toString().equals("cfm:tv")){
                GGLogger.info("in");
                TileEntityTV tileEntityTV= (TileEntityTV) event.getWorld().getTileEntity(event.getPos());
                try {
                    if (PermissionUtils.checkPermission(((EntityPlayerMP) event.getEntityPlayer()), "group.builder")) {
                        ReflectionHelper.setPrivateBoolean(tileEntityTV, "disabled", false);
                        GGLogger.info("builder");
                    } else {
                        ReflectionHelper.setPrivateBoolean(tileEntityTV, "disabled", true);
                        GGLogger.info("out");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                return;
            }
        }
        else if((GoldenGlow.rightClickBlacklistHandler.blacklistedItems.contains(blockState.getBlock().getRegistryName().toString()) || blockState.getBlock() instanceof BlockContainer) && !(PermissionUtils.checkPermission((EntityPlayerMP) event.getEntityPlayer(), "builder"))) {
            event.setCanceled(true);
        }
        else{
            return;
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
                String s = p.getOwnerPlayer().getCapability(OOPlayerProvider.OO_DATA, null).getEquippedSeals()[p.getPosition().order];
                if(s!=null && s.isEmpty() && SealManager.loadedSeals.containsKey(s))
                    SealManager.loadedSeals.get(s).execute(event.getEntity());
            }
        }
    }

    @SubscribeEvent
    public void onPokemonAggro(AggressionEvent event) {
        if(PlayerData.get(event.player).editingNpc!=null)
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void onMessage(ServerChatEvent event) {
        List<String> matches = new ArrayList<>();
        Matcher m = Pattern.compile("<\\w*:\\d>").matcher(event.getMessage());
        while(m.find()) {
            matches.add(m.group());
        }

        TextComponentString pre = new TextComponentString(event.getComponent().getFormattedText().split(event.getMessage())[0]);
        //pre.appendText(event.getMessage().split(matches.get(0))[0]);
        String[] inbetweens = event.getMessage().split("<\\w*:\\d>");
        int i = 0;
        for(String match : matches) {
            pre.appendText(inbetweens[i]);
            try {
                String split = (match.split("<\\w*:")[1].replace(">", ""));
                int num = Integer.valueOf(split);
                if(num>0 && num<7) {
                    Pokemon p = Pixelmon.storageManager.getParty(event.getPlayer()).get(num - 1);
                    if(p!=null) {
                        TextComponentString share = new TextComponentString("["+ (p.getNickname()!=null ? p.getNickname() : p.getSpecies().name) +"]");
                        share.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new TextComponentString(CustomItem.getPokemonItem(p).getItem().writeToNBT(new NBTTagCompound()).toString()))).setBold(true).setColor(TextFormatting.DARK_AQUA);
                        pre.appendSibling(share);
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            i++;
        }
        if(inbetweens.length>i)
            pre.appendText(inbetweens[i]);
        event.setComponent(pre);
    }

    @SubscribeEvent
    public void onTurnEnd(TurnEndEvent event) {
        if(event.bcb.rules instanceof CustomNPCBattle) {
            CustomNPCBattle rules = (CustomNPCBattle)event.bcb.rules;
            CNPCBattleEvent.TurnEnd npcEvent = new CNPCBattleEvent.TurnEnd(new NPCWrapper(rules.getNpc()), new PlayerWrapper(event.bcb.getPlayers().get(0).player), event.bcb);
            for(ScriptContainer s : ((CustomNPCBattle)event.bcb.rules).getNpc().script.getScripts()) {
                s.run("turnEnd", npcEvent);
            }
        }
    }
}
