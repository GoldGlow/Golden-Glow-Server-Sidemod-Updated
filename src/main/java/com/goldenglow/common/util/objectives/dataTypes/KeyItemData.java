package com.goldenglow.common.util.objectives.dataTypes;

import com.goldenglow.GoldenGlow;
import com.google.gson.JsonObject;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import com.pixelmonessentials.common.api.quests.Objective;
import com.pixelmonessentials.common.api.quests.ObjectiveData;
import com.pixelmonessentials.common.api.quests.ObjectiveDataBase;
import com.pixelmonessentials.common.guis.objectives.CustomObjectiveGui;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.gui.CustomGuiTextFieldWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.controllers.CustomGuiController;

public class KeyItemData extends ObjectiveDataBase {
    private String keyItem;

    public KeyItemData( int questId, int objectiveId, String keyItem){
        super("KEY_ITEM", questId, objectiveId);
        this.keyItem=keyItem;
    }

    public KeyItemData(){
        super("KEY_ITEM");
        this.addObjective(PixelmonEssentials.questsManager.getObjectiveFromName("KEY_ITEM"));
    }

    public CustomGuiWrapper addComponents(CustomGuiWrapper gui) {
        gui.addLabel(202, "Name:", 60, 170, 128, 20);
        gui.addTextField(400, 110, 170, 100, 20);
        return gui;
    }

    public String getKeyItem(){
        return this.keyItem;
    }

    @Override
    public String getCriteria(){
        return "Key Item";
    }

    @Override
    public String checkForErrors(EntityPlayerMP playerMP){
        EssentialsGuis gui = PixelmonEssentials.essentialsGuisHandler.getGui(playerMP);
        if (gui instanceof CustomObjectiveGui) {
            CustomGuiWrapper guiWrapper = CustomGuiController.getOpenGui(playerMP);
            CustomGuiTextFieldWrapper textfield = (CustomGuiTextFieldWrapper)guiWrapper.getComponent(400);
            if (textfield != null) {
                if(GoldenGlow.customItemManager.getKeyItem(textfield.getText())==null){
                    return "There's no Key Item with that name!";
                }
                else {
                    KeyItemData data = new KeyItemData(((CustomObjectiveGui)gui).getQuest().id, ((CustomObjectiveGui)gui).getObjective(), textfield.getText());
                    PixelmonEssentials.questsManager.removeObjective(((CustomObjectiveGui)gui).getQuest().id, ((CustomObjectiveGui)gui).getObjective());
                    ((Objective)PixelmonEssentials.questsManager.getObjectives().get(((CustomObjectiveGui)gui).getObjectiveIndex())).addQuest(data);
                    return "";
                }
            }
            return "There was no Textfield!";
        } else {
            return "Not the right GUI.";
        }
    }

    @Override
    public ObjectiveData loadFromJson(JsonObject object){
        int questId=-1;
        int objectiveId=-1;
        String keyItem="";
        if (object.has("questId")) {
            questId = object.get("questId").getAsInt();
        }

        if (object.has("objectiveId")) {
            objectiveId = object.get("objectiveId").getAsInt();
        }

        if (object.has("keyItem")) {
            keyItem = object.get("keyItem").getAsString();
        }

        return questId != -1 && objectiveId != -1 && !keyItem.equals("") ? new KeyItemData(questId, objectiveId, keyItem) : null;
    }

    @Override
    public JsonObject saveToJson(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("objectiveType", this.getObjectiveType());
        jsonObject.addProperty("questId", this.questId);
        jsonObject.addProperty("objectiveId", this.objectiveId);
        jsonObject.addProperty("keyItem", this.keyItem);
        return jsonObject;
    }
}
