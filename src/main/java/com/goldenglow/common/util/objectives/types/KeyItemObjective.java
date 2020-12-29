package com.goldenglow.common.util.objectives.types;

import com.goldenglow.common.data.player.IPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.util.objectives.dataTypes.KeyItemData;
import com.pixelmonessentials.common.api.quests.ObjectiveBase;
import com.pixelmonessentials.common.api.quests.ObjectiveData;
import com.pixelmonessentials.common.api.quests.ObjectiveDataBase;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.handler.data.IQuestObjective;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.quests.QuestManual;

import java.util.ArrayList;
import java.util.List;

public class KeyItemObjective extends ObjectiveBase {
    public KeyItemObjective(){
        super("KEY_ITEM");
    }

    @Override
    public void calculateProgress(ArrayList<ObjectiveData> data, EntityPlayerMP playerMP){
        if(data.size()>0){
            QuestManual quest=(QuestManual) QuestController.instance.quests.get(data.get(0).getQuestId()).questInterface;
            for(ObjectiveData objectiveData:data){
                IQuestObjective objective=quest.getObjectives(playerMP)[objectiveData.getObjectiveId()];
                IPlayerData playerData = playerMP.getCapability(OOPlayerProvider.OO_DATA, null);
                List<String> keyItems=playerData.getKeyItems();
                if(objectiveData instanceof KeyItemData){
                    if(keyItems.contains(((KeyItemData) objectiveData).getKeyItem())){
                        objective.setProgress(objective.getMaxProgress());
                    }
                }
            }
        }
    }
}
