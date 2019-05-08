package com.goldenglow.common.battles;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.teams.Team;
import com.goldenglow.common.util.Reference;
import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.comm.SetTrainerData;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.ArrayList;
import java.util.List;

public class CustomBattleHandler
{
    static GoldenGlow mod;
    public static CustomBattleHandler instance;

    public static List<CustomNPCBattle> battles = new ArrayList<CustomNPCBattle>();

    public CustomBattleHandler()
    {
        this.mod = GoldenGlow.instance;
        this.instance = this;
    }

    public static void createCustomBattle(EntityPlayerMP player, String teamName, int winDialogID, int loseDialogID, EntityNPCInterface npc) {
        Team npcTeam;
        if(teamName!=null) {
            npcTeam = GoldenGlow.instance.teamManager.getTeam(teamName);
        }else{
            npcTeam = new Team("");
        }

        if (BattleRegistry.getBattle(player) != null){
            player.sendMessage(new TextComponentString(Reference.messagePrefix +Reference.red + "Cannot Battle!"));
            return;
        }else
            try {
                NPCTrainer trainer = (NPCTrainer) PixelmonEntityList.createEntityByName(npc.display.getName(), player.getEntityWorld());
                SetTrainerData data=new SetTrainerData(npc.display.getName(), " ", " ", " ", 0, null);
                trainer.update(data);
                trainer.loadPokemon(npcTeam.getMembers());
                trainer.setPosition(player.posX,player.posY,player.posZ);
                ArrayList<Pokemon> playerParty = new ArrayList<Pokemon>();
                ArrayList<EntityPixelmon> pixelmon=PixelmonMethods.getAllActivePokemon(player);
                for(EntityPixelmon pokemon : pixelmon){
                    playerParty.add(pokemon.getStoragePokemonData());
                }
                if(playerParty.size()>0)
                {
                    PlayerParticipant playerParticipant = new PlayerParticipant(player, playerParty, 6);
                    TrainerParticipant trainerParticipant = new TrainerParticipant(trainer, player, 1);

                    Dialog winDialog = DialogController.instance.dialogs.get(winDialogID);
                    Dialog loseDialog = DialogController.instance.dialogs.get(loseDialogID);

                    CustomNPCBattle customNPCBattle = new CustomNPCBattle(playerParticipant, trainerParticipant, npc, winDialog, loseDialog);
                    battles.add(customNPCBattle);
                }else{
                    player.sendMessage(new TextComponentString(Reference.messagePrefix + Reference.red + "You have no pokemon that are able to battle!"));
                }
            }
            catch (Exception e){}
    }

}