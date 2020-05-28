package com.goldenglow.common.handlers.events;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.data.player.IPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.events.OOPokedexEvent;
import com.goldenglow.common.guis.pokehelper.bag.BagMenu;
import com.goldenglow.common.guis.pokehelper.config.OptionListMenu;
import com.goldenglow.common.guis.pokehelper.social.PlayerProfileMenu;
import com.goldenglow.common.guis.pokehelper.info.tutorials.TutorialsMenu;
import com.goldenglow.common.music.SongManager;
import com.goldenglow.common.seals.SealManager;
import com.goldenglow.common.util.TitleMethods;
import com.goldenglow.common.util.scripting.VisibilityFunctions;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.AggressionEvent;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
import com.pixelmonmod.pixelmon.api.events.spawning.PixelmonSpawnerEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.pokedex.EnumPokedexRegisterStatus;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noppes.npcs.api.event.CustomGuiEvent;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerScriptData;

import java.util.ArrayList;
import java.util.Map;

public class OtherEventHandler {
    @SubscribeEvent
    public void pokedexRegisteredEvent(PokedexEvent event){
        Map<Integer, EnumPokedexRegisterStatus> seen= Pixelmon.storageManager.getParty(event.uuid).pokedex.getSeenMap();
        ArrayList<Integer> caught=new ArrayList<Integer>();
        int bugTypes=0;
        for(Map.Entry<Integer, EnumPokedexRegisterStatus> entry:seen.entrySet()){
            if(entry.getValue()==EnumPokedexRegisterStatus.caught){
                caught.add(entry.getKey());
            }
        }
        PlayerScriptData scriptData= PlayerData.get(FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(event.uuid)).scriptData;
        OOPokedexEvent dexEvent=new OOPokedexEvent(new PlayerWrapper(FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(event.uuid)), caught);
        TitleMethods.unlockBugCatcher(dexEvent);
        for(ScriptContainer s : scriptData.getScripts()){
            s.run("pokedexEvent", dexEvent);
        }
    }

    @SubscribeEvent
    public void onGuiClose(CustomGuiEvent.CloseEvent event){
        if(!GoldenGlow.routeManager.getRoute(event.player.getMCEntity()).equals(null)) {
            if (!GoldenGlow.routeManager.getRoute(event.player.getMCEntity()).song.equals(null)) {
                SongManager.setCurrentSong(event.player.getMCEntity(), GoldenGlow.routeManager.getRoute(event.player.getMCEntity()).song);
            }
        }
    }

    @SubscribeEvent
    public void onSlotChange(CustomGuiEvent.ScrollEvent event){
        EssentialsGuis gui= PixelmonEssentials.essentialsGuisHandler.getGui(event.player.getMCEntity());
        if(gui instanceof BagMenu){
            ((BagMenu) gui).setIndex(event.selection[0]);
            ((BagMenu) gui).updateScroll(event.player.getMCEntity());
        }
        else if(gui instanceof TutorialsMenu){
            ((TutorialsMenu) gui).setIndex(event.selection[0]);
            ((TutorialsMenu) gui).updateScroll(event.player.getMCEntity());
        }
        else if(gui instanceof OptionListMenu){
            ((OptionListMenu) gui).setIndex(event.selection[0]);
            ((OptionListMenu) gui).updateScroll(event.player.getMCEntity());
        }
    }

    @SubscribeEvent
    public void onVanish(PlayerEvent.StartTracking event){
        if(event.getTarget() instanceof EntityPlayerMP){
            if(!VisibilityFunctions.canPlayerSeeOtherPlayer((EntityPlayerMP) event.getEntityPlayer(), (EntityPlayerMP) event.getTarget())){
                ((EntityPlayerMP)event.getEntityPlayer()).removeEntity(event.getTarget());
            }
        }
    }

    @SubscribeEvent
    public void onMessage(ServerChatEvent event) {
    }

    @SubscribeEvent
    public void onPixelmonSpawner(PixelmonSpawnerEvent event){
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
    public void onPlayerInteract(PlayerInteractEvent.EntityInteract event){
        if(event.getTarget() instanceof EntityPlayerMP){
            PlayerProfileMenu profile=new PlayerProfileMenu((EntityPlayerMP) event.getTarget());
            profile.init((EntityPlayerMP) event.getEntityPlayer(), null);
        }
    }
}
