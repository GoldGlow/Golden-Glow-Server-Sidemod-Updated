package com.goldenglow.common.battles;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.teams.Team;
import com.goldenglow.common.util.Reference;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.IStorageManager;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
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

    public static void createCustomBattle(EntityPlayerMP player, String teamName, int initDialogID, int winDialogID, int loseDialogID, EntityNPCInterface npc) {
        Team npcTeam;
        if(teamName!=null) {
            GoldenGlow.instance.teamManager.printTeams();
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
                EntityPixelmon pixelmon= Pixelmon.storageManager.getParty(player).getAndSendOutFirstAblePokemon(player);
                if(pixelmon!=null)
                {
                    PlayerParticipant playerParticipant = new PlayerParticipant(player, pixelmon);
                    TrainerParticipant trainerParticipant = new TrainerParticipant(trainer, player, 1);

                    Dialog winDialog = DialogController.instance.dialogs.get(winDialogID);
                    Dialog loseDialog = DialogController.instance.dialogs.get(loseDialogID);
                    Dialog initDialog = DialogController.instance.dialogs.get(initDialogID);
                    CustomNPCBattle rules=new CustomNPCBattle(npc, initDialog, winDialog, loseDialog);
                    BattleRegistry.startBattle(new BattleParticipant[]{playerParticipant}, new BattleParticipant[] {trainerParticipant}, rules);
                }else{
                    player.sendMessage(new TextComponentString(Reference.messagePrefix + Reference.red + "You have no pokemon that are able to battle!"));
                }
            }
            catch (Exception e){}
    }

}