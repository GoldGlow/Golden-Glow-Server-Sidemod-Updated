package com.goldenglow.common.util.actions;

import com.goldenglow.common.util.ReflectionHelper;
import com.goldenglow.common.util.actions.types.bag.*;
import com.goldenglow.common.util.actions.types.friendList.AcceptFriendAction;
import com.goldenglow.common.util.actions.types.friendList.RejectFriendAction;
import com.goldenglow.common.util.actions.types.gui.OpenSpawnsAction;
import com.goldenglow.common.util.actions.types.gui.ScrollAction;
import com.goldenglow.common.util.actions.types.gui.SetLocationAction;
import com.goldenglow.common.util.actions.types.gyms.*;
import com.goldenglow.common.util.actions.types.keyItems.*;
import com.goldenglow.common.util.actions.types.oldActions.OpenInvAction;
import com.goldenglow.common.util.actions.types.oldActions.ScoreboardAction;
import com.goldenglow.common.util.actions.types.oldActions.SealSetAction;
import com.goldenglow.common.util.actions.types.options.*;
import com.goldenglow.common.util.actions.types.other.DepositoryPokemonAction;
import com.goldenglow.common.util.actions.types.other.SetSongAction;
import com.goldenglow.common.util.actions.types.trainer.OOEditTrainerAction;
import com.goldenglow.common.util.actions.types.trainer.OOReturnToTrainerAction;
import com.goldenglow.common.util.actions.types.trainer.OOSaveTeamAction;
import com.goldenglow.common.util.actions.types.trainer.OOSelectTeamAction;
import com.goldenglow.common.util.actions.types.tutorials.ChangeTutorialPageAction;
import com.goldenglow.common.util.actions.types.tutorials.OpenTutorialAction;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.Action;

import java.util.ArrayList;

public class ActionHandler {

    public static void init(){
        PixelmonEssentials.actionHandler.addAction(new AcceptFriendAction());
        PixelmonEssentials.actionHandler.addAction(new ChangeClothesAction());
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
        PixelmonEssentials.actionHandler.addAction(new SetSongAction());
        PixelmonEssentials.actionHandler.addAction(new SelectAmountAction());
        PixelmonEssentials.actionHandler.addAction(new StartBattleAction());
        PixelmonEssentials.actionHandler.addAction(new StopChallengersAction());
        PixelmonEssentials.actionHandler.addAction(new TakeChallengersAction());
        PixelmonEssentials.actionHandler.addAction(new TeachMoveAction());
        PixelmonEssentials.actionHandler.addAction(new TMPartyAction());
        PixelmonEssentials.actionHandler.addAction(new UpdateBagAction());
        PixelmonEssentials.actionHandler.addAction(new TMGuiAction());
        PixelmonEssentials.actionHandler.addAction(new SetLocationAction());
        PixelmonEssentials.actionHandler.addAction(new SaveOptionAction());
        PixelmonEssentials.actionHandler.addAction(new OpenOptionAction());
        PixelmonEssentials.actionHandler.addAction(new ChatVisibilityAction());
        PixelmonEssentials.actionHandler.addAction(new SetDmAction());
        PixelmonEssentials.actionHandler.addAction(new SetViewAction());
        PixelmonEssentials.actionHandler.addAction(new OpenSpawnsAction());
        PixelmonEssentials.actionHandler.addAction(new DeleteKeyItemAction());
        PixelmonEssentials.actionHandler.addAction(new NewKeyItemAction());
        PixelmonEssentials.actionHandler.addAction(new CancelKeyItemAction());
        PixelmonEssentials.actionHandler.addAction(new EditKeyItemAction());
        PixelmonEssentials.actionHandler.addAction(new PreviewKeyItemAction());
        PixelmonEssentials.actionHandler.addAction(new CategoryUseAction());

        ArrayList<Action> actions= ReflectionHelper.getPrivateValue(PixelmonEssentials.actionHandler, "actionTypes");
        actions.remove(PixelmonEssentials.actionHandler.getType("EDIT_TRAINER"));
        actions.remove(PixelmonEssentials.actionHandler.getType("SAVE_TEAM"));
        actions.remove(PixelmonEssentials.actionHandler.getType("RETURN_TRAINER"));
        actions.remove(PixelmonEssentials.actionHandler.getType("SELECT_TEAM"));
        ReflectionHelper.setPrivateValue(PixelmonEssentials.actionHandler, "actionTypes", actions);

        PixelmonEssentials.actionHandler.addAction(new OOEditTrainerAction());
        PixelmonEssentials.actionHandler.addAction(new OOSaveTeamAction());
        PixelmonEssentials.actionHandler.addAction(new OOReturnToTrainerAction());
        PixelmonEssentials.actionHandler.addAction(new OOSelectTeamAction());
    }
}
