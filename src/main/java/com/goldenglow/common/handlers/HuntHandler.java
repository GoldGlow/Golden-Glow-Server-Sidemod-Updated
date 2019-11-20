package com.goldenglow.common.handlers;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.battles.raids.RaidBattleRules;
import com.goldenglow.common.data.IPlayerData;
import com.goldenglow.common.data.OOPlayerData;
import com.goldenglow.common.data.OOPlayerProvider;
import com.goldenglow.common.music.SongManager;
import com.goldenglow.common.util.PermissionUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.BeatWildPixelmonEvent;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.events.DropEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.api.events.spawning.SpawnEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnActionPokemon;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.client.particle.ParticleSystems;
import com.pixelmonmod.pixelmon.comm.packetHandlers.PlayParticleSystem;
import com.pixelmonmod.pixelmon.comm.packetHandlers.itemDrops.ItemDropMode;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DroppedItem;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class HuntHandler {

    @SubscribeEvent
    public static void onPixelmonSpawnEvent(SpawnEvent event) {
        if(event.action instanceof SpawnActionPokemon) {
            SpawnActionPokemon action = (SpawnActionPokemon)event.action;
            action.usingSpec.shiny = false;
        }
    }

    @SubscribeEvent
    public static void battleStartedEvent(BattleStartedEvent event) {
        if(event.participant2[0] instanceof WildPixelmonParticipant && event.participant1[0] instanceof PlayerParticipant) {
            Random r = new Random();
            Pokemon wild = ((WildPixelmonParticipant)event.participant2[0]).allPokemon[0].pokemon;
            EntityPlayerMP player = (EntityPlayerMP)event.participant1[0].getEntity();
            int catchChain = player.getCapability(OOPlayerProvider.OO_DATA, null).getCaptureChain();

            int shinyChance = Math.max(4096-((catchChain/5)*716), 516);
            boolean shiny = r.nextInt(shinyChance) == 0;

            if(PermissionUtils.checkPermission(player, "*"))
                player.sendMessage(new TextComponentString("Chain: "+ catchChain +", Chance: "+shinyChance));

            if(shiny) {
                wild.setShiny(true);
                player.sendMessage(new TextComponentString("What!? It's a Shiny!"));
                SongManager.playSound(player, "neutral", GoldenGlow.songManager.levelUpDefault);
            }

            int koChain = player.getCapability(OOPlayerProvider.OO_DATA, null).getKOChain();
            int abilityChance = Math.max(150 - ((koChain/5) * 20), 50);
            boolean hiddenAbility = r.nextInt(abilityChance) == 0;
            if(hiddenAbility) {
                BaseStats stats = BaseStats.allBaseStats.get(wild.getSpecies());
                if(stats.abilities[2]!=null)
                    wild.setAbility(stats.abilities[2]);
            }
        }
    }

    @SubscribeEvent
    public static void startCaptureEvent(CaptureEvent.StartCapture event) {
    }

    @SubscribeEvent
    public static void successfulCaptureEvent(CaptureEvent.SuccessfulCapture event) {
        OOPlayerData data = (OOPlayerData)event.player.getCapability(OOPlayerProvider.OO_DATA, null);
        if(data.getChainSpecies() != event.getPokemon().getSpecies()) {
            data.setChainSpecies(event.getPokemon().getSpecies());
            data.setCaptureChain(1);
        }
        else {
            int chain = data.increaseCaptureChain(1);
            if(chain % 5 == 0)
                event.player.sendStatusMessage(new TextComponentString(chain+" Catch Combo!"), true);
        }
    }

    @SubscribeEvent
    public static void failedCaptureEvent(CaptureEvent.FailedCapture event) {
    }

    @SubscribeEvent
    public static void beatWildPixelmonEvent(BeatWildPixelmonEvent event) {
        OOPlayerData data = (OOPlayerData)event.player.getCapability(OOPlayerProvider.OO_DATA, null);
        if(data.getLastKOPokemon() != event.wpp.allPokemon[0].getSpecies()) {
            data.setLastKOPokemon(event.wpp.allPokemon[0].getSpecies());
            data.setKOChain(1);
        }
        else {
            int chain = data.increaseKOChain(1);
            if(chain % 5 == 0)
                event.player.sendStatusMessage(new TextComponentString(chain+" KO Combo!"), true);
        }
    }

    @SubscribeEvent
    public static void wildDropEvent(DropEvent event) {
        OOPlayerData data = (OOPlayerData)event.player.getCapability(OOPlayerProvider.OO_DATA, null);
        int chainTotal = data.getKOChain() + data.getCaptureChain();
        if(data.getKOChain()>0) {
            if (event.dropMode == ItemDropMode.NormalPokemon) {
                ImmutableList<DroppedItem> drops = event.getDrops();
                for(int i = 0; i < drops.size(); i++) {
                    event.removeDrop(drops.get(i));
                    ItemStack newItem = drops.get(i).itemStack;
                    newItem.setCount(newItem.getCount() * Ints.constrainToRange((chainTotal/10)+1, 1, 4));
                    event.addDrop(newItem);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBattleEnd(BattleEndEvent event) {
        if(!(event.bc.rules instanceof RaidBattleRules) && event.cause == EnumBattleEndCause.FLEE) {
            OOPlayerData data = (OOPlayerData) event.getPlayers().get(0).getCapability(OOPlayerProvider.OO_DATA, null);
            if (event.bc.participants.get(1).getEntity() instanceof EntityPixelmon) {
                EntityPixelmon e = (EntityPixelmon)event.bc.participants.get(1).getEntity();
                Pixelmon.network.sendToDimension(new PlayParticleSystem(ParticleSystems.BLOOM, e.posX, e.posY, e.posZ, e.dimension, e.getPixelmonScale(), e.getPokemonData().isShiny(), new double[0]), e.dimension);
                e.setDead();
            }
        }
    }
}
