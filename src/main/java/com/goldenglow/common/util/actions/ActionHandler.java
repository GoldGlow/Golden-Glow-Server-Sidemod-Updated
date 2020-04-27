package com.goldenglow.common.util.actions;

import com.goldenglow.common.util.actions.types.*;
import com.pixelmonessentials.PixelmonEssentials;

import java.util.ArrayList;

public class ActionHandler {

    public static void init(){
        PixelmonEssentials.actionHandler.addAction(new AcceptFriendAction());
        PixelmonEssentials.actionHandler.addAction(new ChangeSkinAction());
        PixelmonEssentials.actionHandler.addAction(new ChangeTutorialPageAction());
        PixelmonEssentials.actionHandler.addAction(new CloseGymAction());
        PixelmonEssentials.actionHandler.addAction(new DepositoryPokemonAction());
        PixelmonEssentials.actionHandler.addAction(new EquipArmorAction());
        PixelmonEssentials.actionHandler.addAction(new NextChallengerAction());
        PixelmonEssentials.actionHandler.addAction(new OpenGymAction());
        PixelmonEssentials.actionHandler.addAction(new OpenInvAction());
        PixelmonEssentials.actionHandler.addAction(new OpenTutorialAction());
        PixelmonEssentials.actionHandler.addAction(new RejectFriendAction());
        PixelmonEssentials.actionHandler.addAction(new SaveSongAction());
        PixelmonEssentials.actionHandler.addAction(new ScoreboardAction());
        PixelmonEssentials.actionHandler.addAction(new ScrollAction());
        PixelmonEssentials.actionHandler.addAction(new SealSetAction());
        PixelmonEssentials.actionHandler.addAction(new SetFriendViewAction());
        PixelmonEssentials.actionHandler.addAction(new SetSongAction());
        PixelmonEssentials.actionHandler.addAction(new StartBattleAction());
        PixelmonEssentials.actionHandler.addAction(new StopChallengersAction());
        PixelmonEssentials.actionHandler.addAction(new TakeChallengersAction());
        PixelmonEssentials.actionHandler.addAction(new TeachMoveAction());
        PixelmonEssentials.actionHandler.addAction(new TMPartyAction());
        PixelmonEssentials.actionHandler.addAction(new UpdateBagAction());
        PixelmonEssentials.actionHandler.addAction(new TMGuiAction());
        PixelmonEssentials.actionHandler.addAction(new SetLocationAction());
    }
}
