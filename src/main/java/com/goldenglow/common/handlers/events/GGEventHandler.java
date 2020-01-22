package com.goldenglow.common.handlers.events;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.battles.npc.CustomNPCBattle;
import com.goldenglow.common.battles.npc.DoubleNPCBattle;
import com.goldenglow.common.data.player.IPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.events.CNPCBattleEvent;
import com.goldenglow.common.events.OOPokedexEvent;
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
import com.goldenglow.common.tiles.TileEntityCustomFridge;
import com.goldenglow.common.util.*;
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
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
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
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import noppes.npcs.CustomItems;
import noppes.npcs.EventHooks;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.event.BlockEvent;
import noppes.npcs.api.wrapper.ItemScriptedWrapper;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.WrapperNpcAPI;
import noppes.npcs.blocks.BlockScripted;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerScriptData;
import noppes.npcs.items.ItemNpcScripter;
import noppes.npcs.items.ItemNpcWand;
import noppes.npcs.items.ItemScripted;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
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
            BlockPos blockPos = event.getPos();
            if(blockState.getBlock() instanceof BlockApricornTree || blockState.getBlock() instanceof BlockBerryTree) {
                if(blockState.getValue(BlockApricornTree.BLOCKPOS) == EnumBlockPos.TOP) {
                    blockPos = event.getPos().down();
                    tile = event.getWorld().getTileEntity(blockPos);
                } else {
                    tile = event.getWorld().getTileEntity(event.getPos());
                }
            }
            else if(blockState.getBlock() instanceof BlockFridge){
                if(blockState.getValue(MultiBlock.MULTIPOS)== EnumMultiPos.TOP){
                    blockPos = event.getPos().down();
                    tile = event.getWorld().getTileEntity(blockPos);
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
                if (event.getItemStack().getItem() instanceof ItemNpcScripter && !event.getEntityPlayer().isSneaking()) {
                    GGLogger.info("Scriptable - "+blockPos.toString());
                    event.getEntityPlayer().closeScreen();
                    SPacketBlockChange packet = new SPacketBlockChange(event.getWorld(), blockPos);
                    packet.blockState = CustomItems.scripted.getDefaultState();
                    ((EntityPlayerMP)event.getEntityPlayer()).connection.sendPacket(packet);
                    NoppesUtilServer.sendOpenGui(event.getEntityPlayer(), EnumGuiType.ScriptBlock, null, blockPos.getX(), blockPos.getY(), blockPos.getZ());
                }
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

    //ToDo: Issues with message formatting duplicating message contents when special characters used. Either RegEx pattern issue or just needs a rewrite.
    @SubscribeEvent
    public void onMessage(ServerChatEvent event) {
        /*
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

         */
    }
}
