package com.goldenglow.common.util;

import com.goldenglow.common.inventory.Action;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Created by JeanMarc on 7/10/2019.
 */
public class ParseJson {

    public static Requirement parseRequirement(JsonObject o){
        Requirement r = new Requirement();
        r.type = Requirement.RequirementType.valueOf(o.getAsJsonObject().get("type").getAsString());
        if (r.type == Requirement.RequirementType.TIME || r.type == Requirement.RequirementType.PERMISSION) {
            r.value = o.getAsJsonObject().get("value").getAsString();
        } else {
            r.id = o.getAsJsonObject().get("id").getAsInt();
        }
        r.override = o.getAsJsonObject().get("override").getAsString();
        return r;
    }

    public static Action parseAction(JsonObject o){
        Action a = new Action();
        a.actionType = Action.ActionType.valueOf(o.getAsJsonObject().get("actionType").getAsString());
        a.value = o.get("value").getAsString();
        if(o.has("requirements")){
            JsonArray requirementArray=o.getAsJsonArray("requirements");
            Requirement[] requirements=new Requirement[requirementArray.size()];
            for(int k=0;k<requirementArray.size();k++){
                requirements[k]=ParseJson.parseRequirement((JsonObject) requirementArray.get(k));
            }
            a.requirements=requirements;
        }
        if(o.has("closeInv")){
            a.closeInv=o.get("closeInv").getAsBoolean();
        }
        return a;
    }
}
