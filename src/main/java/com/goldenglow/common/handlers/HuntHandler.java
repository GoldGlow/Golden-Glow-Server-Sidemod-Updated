package com.goldenglow.common.handlers;

import com.goldenglow.common.data.OOPlayerData;
import com.goldenglow.common.data.OOPlayerProvider;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.BeatWildPixelmonEvent;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.events.spawning.SpawnEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnActionPokemon;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
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
            Pokemon wild = ((WildPixelmonParticipant)event.participant2[0]).allPokemon[0].pokemon;
            EntityPlayerMP player = (EntityPlayerMP)event.participant1[0].getEntity();
            int chain = player.getCapability(OOPlayerProvider.OO_DATA, null).getCaptureChain();
            int i = Math.max(4096-((chain/10)*750), 346);
            boolean shiny = new Random().nextInt(i) == 0;
            player.sendMessage(new TextComponentString("Chain: "+ chain +", Chance: "+i));
            if(true) {
                wild.setShiny(true);
                player.sendMessage(new TextComponentString("What!? It's a Shiny!"));
            }
            //Calculate changes based on chain combo
            //Shiny rate. IVs(?). Moveset(?).
        }
    }

    @SubscribeEvent
    public static void startCaptureEvent(CaptureEvent.StartCapture event) {
    }

    @SubscribeEvent
    public static void successfulCaptureEvent(CaptureEvent.SuccessfulCapture event) {
        OOPlayerData data = (OOPlayerData)event.player.getCapability(OOPlayerProvider.OO_DATA, null);
        if(data.getLastCaughtPokemon() != event.getPokemon().getSpecies()) {
            data.setLastCaughtPokemon(event.getPokemon().getSpecies());
            data.setCaptureChain(1);
        }
        else {
            int chain = data.increaseCaptureChain(1);
            if(chain > 1)
                event.player.sendStatusMessage(new TextComponentString(chain+" Chain Combo!"), true);
        }
    }

    @SubscribeEvent
    public static void failedCaptureEvent(CaptureEvent.FailedCapture event) {
        BattleControllerBase battle = BattleRegistry.getBattle(event.player);
        if(battle != null) {
        }
    }

    @SubscribeEvent
    public static void beatWildPixelmonEvent(BeatWildPixelmonEvent event) {
        //Change combo based on fainting Wild pixelmon
    }

}
