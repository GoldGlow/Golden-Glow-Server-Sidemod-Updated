package com.goldenglow.common.util.scripting;

import com.goldenglow.common.battles.CustomBattleHandler;
import com.goldenglow.common.handlers.TickHandler;
import com.goldenglow.common.music.SongManager;
import com.goldenglow.common.routes.SpawnPokemon;
import com.goldenglow.common.util.PermissionUtils;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.entity.EntityNPCInterface;

public class BattleFunctions {
    public static void createNPCBattle(NPCWrapper firstNPC, String firstTeamName, NPCWrapper secondNPC, String secondTeamName){
        EntityNPCInterface firstNpc=(EntityNPCInterface) firstNPC.getMCEntity();
        EntityNPCInterface secondNpc=(EntityNPCInterface) secondNPC.getMCEntity();
        CustomBattleHandler.createCustomNPCBattle(firstNpc, firstTeamName, secondNpc, secondTeamName);
    }

    public static void createCustomBattle(PlayerWrapper playerWrapper, String teamName, int initDialogID, int winDialogID, int loseDialogID, NPCWrapper npcWrapper) {
        EntityNPCInterface npc=(EntityNPCInterface) npcWrapper.getMCEntity();
        EntityPlayerMP player=(EntityPlayerMP)playerWrapper.getMCEntity();
        if(PermissionUtils.checkPermission(player,"hard")){
            teamName+="-hard";
        }
        CustomBattleHandler.createCustomBattle(player, teamName, initDialogID, winDialogID, loseDialogID, npc);
    }

    public static void registerLOSBattle(NPCWrapper npc, int initDialogID) {
        TickHandler.battleNPCs.put(npc, initDialogID);
    }

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

    public static void battleInitDialog(PlayerWrapper player, NPCWrapper npc, int dialogId){
        SongManager.setCurrentSong((EntityPlayerMP) player.getMCEntity(), SongManager.encounterDefault);
        NoppesUtilServer.openDialog((EntityPlayerMP) player.getMCEntity(), (EntityNPCInterface)npc.getMCEntity(), (Dialog) DialogController.instance.get(dialogId));
    }
}
