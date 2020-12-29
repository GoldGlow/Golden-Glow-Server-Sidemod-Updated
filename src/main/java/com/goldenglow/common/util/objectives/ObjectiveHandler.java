package com.goldenglow.common.util.objectives;

import com.goldenglow.common.util.objectives.dataTypes.KeyItemData;
import com.goldenglow.common.util.objectives.types.KeyItemObjective;
import com.pixelmonessentials.PixelmonEssentials;

public class ObjectiveHandler {
    public static void init(){
        PixelmonEssentials.questsManager.objectives.add(new KeyItemObjective());

        PixelmonEssentials.questsManager.dataTypes.add(new KeyItemData());
    }
}
