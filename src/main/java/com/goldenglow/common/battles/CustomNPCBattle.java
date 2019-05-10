package com.goldenglow.common.battles;

import com.goldenglow.GoldenGlow;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.entity.EntityNPCInterface;

public class CustomNPCBattle extends BattleRules
{
    private Dialog winDiag;
    private Dialog loseDiag;
    private Dialog initDiag;
    public static EntityPlayer player;
    private static EntityNPCInterface npc;

    public CustomNPCBattle(EntityNPCInterface npc, Dialog initDialog, Dialog winDialog, Dialog loseDialog) throws Exception
    {
        super();
        this.npc=npc;
        this.initDiag=initDialog;
        this.winDiag=winDialog;
        this.loseDiag=loseDialog;
    }

    public void addWinDialog(Dialog winDiag)
    {
        this.winDiag=winDiag;
    }

    public void addLoseDialog(Dialog loseDiag)
    {
        this.loseDiag=loseDiag;
    }

    public Dialog getWinDialog()
    {
        return this.winDiag;
    }

    public Dialog getLoseDialog()
    {
        return this.loseDiag;
    }

    public Dialog getInitDiag() {return this.initDiag;}

    public EntityNPCInterface getNpc()
    {
        return this.npc;
    }

    public boolean hasPlayer(EntityPlayer participant)
    {
        if(participant==player)
            return true;
        else
            return false;
    }
}