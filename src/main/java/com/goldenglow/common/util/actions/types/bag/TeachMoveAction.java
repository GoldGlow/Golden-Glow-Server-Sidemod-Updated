package com.goldenglow.common.util.actions.types.bag;

import com.goldenglow.common.util.Reference;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenReplaceMoveScreen;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

public class TeachMoveAction extends ActionBase {
    public TeachMoveAction(){
        super("TEACH_MOVE");
    }

    @Override
    public void doAction(EntityPlayerMP player, ActionData data){
        if(data instanceof ActionStringData){
            String value=((ActionStringData) data).getValue();
            int slot=Integer.parseInt(value.split(":")[0]);
            Attack attack=new Attack(value.split(":")[1]);
            PlayerPartyStorage partyStorage = Pixelmon.storageManager.getParty(player);
            Pokemon pokemon=partyStorage.get(slot);
            if (!pokemon.getMoveset().hasAttack(attack)) {
                if (pokemon.getMoveset().size() >= 4) {
                    player.closeScreen();
                    Pixelmon.network.sendTo(new OpenReplaceMoveScreen(pokemon.getUUID(), attack.getActualMove().getAttackId()), player);
                } else {
                    player.closeScreen();
                    pokemon.getMoveset().add(attack);
                    player.sendMessage(new TextComponentString(pokemon.getDisplayName()+" successfully learned the move "+attack.getActualMove().getAttackName()));
                }
            } else {
                player.closeScreen();
                player.sendMessage(new TextComponentString(Reference.red+"Already knows the move!"));
            }
        }
    }
}
