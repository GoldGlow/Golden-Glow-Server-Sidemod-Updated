package com.goldenglow.common.util.scripting;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.battles.bosses.BossManager;
import com.goldenglow.common.battles.npc.CustomBattleHandler;
import com.goldenglow.common.handlers.TickHandler;
import com.goldenglow.common.routes.SpawnPokemon;
import com.pixelmonessentials.common.battles.CustomNPCBattle;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.entity.EntityNPCInterface;

public class BattleFunctions {
    //used to do Blue vs May battle sequence, might be used again later
    public static void createNPCBattle(NPCWrapper firstNPC, String firstTeamName, NPCWrapper secondNPC, String secondTeamName){
        EntityNPCInterface firstNpc=(EntityNPCInterface) firstNPC.getMCEntity();
        EntityNPCInterface secondNpc=(EntityNPCInterface) secondNPC.getMCEntity();
        CustomBattleHandler.createCustomNPCBattle(firstNpc, firstTeamName, secondNpc, secondTeamName);
    }

    //Used for Trainer Battles, doesn't include LoS
    public static void createCustomBattle(PlayerWrapper playerWrapper, NPCWrapper npcWrapper) {
        EntityNPCInterface npc=(EntityNPCInterface) npcWrapper.getMCEntity();
        EntityPlayerMP player=(EntityPlayerMP)playerWrapper.getMCEntity();
        CustomBattleHandler.createCustomBattle(player, npc);
    }

    //Used to start a Boss Battle for a Player
    public static void createBossBattle(PlayerWrapper playerWrapper, String bossName) {
        BossManager.startBossBattle((EntityPlayerMP)playerWrapper.getMCEntity(), bossName);
    }

    // Line of sight code, used for sneaking portions and trainers
    public static void registerLOSBattle(NPCWrapper npc, int initDialogID) {
        TickHandler.battleNPCs.put(npc, initDialogID);
    }

    //code to start wild battles, currently used for apricorns
    public static void startWildBattle(PlayerWrapper player, SpawnPokemon pokemon){
        EntityPlayerMP playerMP=(EntityPlayerMP)player.getMCEntity();
        PokemonSpec pokemonSpec=PokemonSpec.from(pokemon.species);
        pokemonSpec.form=pokemon.form;
        pokemonSpec.level= RandomHelper.getRandomNumberBetween(pokemon.minLvl, pokemon.maxLvl);
        Pokemon wildPokemon=pokemonSpec.create();
        EntityPixelmon pixelmon=new EntityPixelmon(playerMP.world);
        pixelmon.setPokemon(wildPokemon);
        pixelmon.setPosition(playerMP.posX, playerMP.posY, playerMP.posZ);
        pixelmon.setSpawnLocation(pixelmon.getDefaultSpawnLocation());
        BattleRegistry.startBattle(new PlayerParticipant(playerMP, Pixelmon.storageManager.getParty(playerMP).getAndSendOutFirstAblePokemon(playerMP)), new WildPixelmonParticipant(pixelmon));
    }

    public static boolean pokemonKOd(BattleControllerBase bcb){
        if(bcb.rules instanceof CustomNPCBattle){
            if(((CustomNPCBattle) bcb.rules).getRemainingNPCPokemon()!=bcb.participants.get(1).countAblePokemon()){
                ((CustomNPCBattle) bcb.rules).setRemainingNPCPokemon(bcb.participants.get(1).countAblePokemon());
                return true;
            }
        }
        return false;
    }

    public static int getRemainingPokemon(BattleControllerBase bcb){
        return bcb.participants.get(1).countAblePokemon();
    }

}
