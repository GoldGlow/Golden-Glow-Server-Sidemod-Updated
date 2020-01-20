package com.goldenglow.common.handlers.events;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.data.player.IPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.events.OOPokedexEvent;
import com.goldenglow.common.inventory.BetterTrading.TradeManager;
import com.goldenglow.common.music.SongManager;
import com.goldenglow.common.tiles.ICustomScript;
import com.goldenglow.common.tiles.TileEntityCustomApricornTree;
import com.goldenglow.common.tiles.TileEntityCustomBerryTree;
import com.goldenglow.common.util.TitleMethods;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.*;
import com.pixelmonmod.pixelmon.api.events.spawning.PixelmonSpawnerEvent;
import com.pixelmonmod.pixelmon.pokedex.EnumPokedexRegisterStatus;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerScriptData;
import org.spongepowered.api.Sponge;

import java.util.ArrayList;
import java.util.Map;

public class PixelmonEventHandler {

    @SubscribeEvent
    public static void onPixelmonSpawner(PixelmonSpawnerEvent event){
        if(event.spawner.getWorld().isDaytime()){
            for(String pokemon: GoldenGlow.pixelmonSpawnerHandler.nightPokemon){
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
    public static void onEvolutionStart(EvolveEvent.PreEvolve event){
        IPlayerData playerData= event.player.getCapability(OOPlayerProvider.OO_DATA, null);
        playerData.setEvolvingPokemon(true);
        SongManager.setCurrentSong(event.player, GoldenGlow.songManager.evolutionDefault);
    }

    @SubscribeEvent
    public static void onEvolutionEnd(EvolveEvent.PostEvolve event){
        IPlayerData playerData= event.player.getCapability(OOPlayerProvider.OO_DATA, null);
        playerData.setEvolvingPokemon(false);
        SongManager.setRouteSong(event.player);
        if(playerData.getWaitToEvolve().size()>0){
            playerData.removePokemonWaiting(0);
            TradeManager.evolutionTest(event.player);
        }
    }

    @SubscribeEvent
    public static void onLevelUp(LevelUpEvent event){
        SongManager.playSound(event.player, "neutral", GoldenGlow.songManager.levelUpDefault);
    }

    @SubscribeEvent
    public static void onPickApricorn(ApricornEvent.PickApricorn event) {
        if(event.tree instanceof TileEntityCustomApricornTree) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPickBerry(BerryEvent.PickBerry event) {
        if(event.tree instanceof TileEntityCustomBerryTree) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPokemonAggro(AggressionEvent event) {
        if(PlayerData.get(event.player).editingNpc!=null)
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void pokedexRegisteredEvent(PokedexEvent event){
        Map<Integer, EnumPokedexRegisterStatus> seen= Pixelmon.storageManager.getParty(event.uuid).pokedex.getSeenMap();
        ArrayList<Integer> caught=new ArrayList<Integer>();
        int bugTypes=0;
        for(Map.Entry<Integer, EnumPokedexRegisterStatus> entry:seen.entrySet()){
            if(entry.getValue()==EnumPokedexRegisterStatus.caught){
                caught.add(entry.getKey());
            }
        }
        PlayerScriptData scriptData=PlayerData.get((EntityPlayerMP) Sponge.getServer().getPlayer(event.uuid).get().getBaseVehicle()).scriptData;
        OOPokedexEvent dexEvent=new OOPokedexEvent(new PlayerWrapper((EntityPlayerMP) Sponge.getServer().getPlayer(event.uuid).get().getBaseVehicle()), caught);
        TitleMethods.unlockBugCatcher(dexEvent);
        for(ScriptContainer s : scriptData.getScripts()){
            s.run("pokedexEvent", dexEvent);
        }
    }

    static void runOnPickEvent(ICustomScript tile, Event event) {
        for (final ScriptContainer scriptContainer4 : tile.getScriptedTile().scripts) {
            scriptContainer4.run("onPick", event);
        }
    }
}
