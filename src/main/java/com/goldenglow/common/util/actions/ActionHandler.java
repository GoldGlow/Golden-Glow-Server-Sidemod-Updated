package com.goldenglow.common.util.actions;

import com.goldenglow.common.util.actions.types.*;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;

public class ActionHandler {
    public ArrayList<Action> actionTypes=new ArrayList<Action>();

    public void init(){
        actionTypes.add(new ChangeSkinAction());
        actionTypes.add(new CloseGymAction());
        actionTypes.add(new CommandAction());
        actionTypes.add(new DepositoryPokemonAction());
        actionTypes.add(new EquipArmorAction());
        actionTypes.add(new GiveItemAction());
        actionTypes.add(new NextChallengerAction());
        actionTypes.add(new OpenGymAction());
        actionTypes.add(new OpenInvAction());
        actionTypes.add(new ScoreboardAction());
        actionTypes.add(new SealSetAction());
        actionTypes.add(new SetFriendViewAction());
        actionTypes.add(new StartBattleAction());
        actionTypes.add(new StopChallengersAction());
        actionTypes.add(new TakeChallengersAction());
        actionTypes.add(new TeachMoveAction());
        actionTypes.add(new TMPartyAction());
    }

    public Action getType(String type){
        for(Action actionType: actionTypes){
            if(actionType.getName().equals(type)){
                return actionType;
            }
        }
        return null;
    }

    public Action getType(ActionData type){
        for(Action actionType: actionTypes){
            if(actionType.getName().equals(type.name)){
                return actionType;
            }
        }
        return null;
    }

    public void doAction(ActionData data, EntityPlayerMP playerMP){
        Action action=getType(data);
        if(data.closeInv)
            playerMP.closeScreen();
        action.doAction(data.value, playerMP);
    }
}
