package com.goldenglow.common.util;

import com.goldenglow.common.routes.SpawnPokemon;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.requirement.RequirementData;

/**
 * Created by JeanMarc on 7/10/2019.
 */
public class ParseJson {

    public static RequirementData parseRequirement(JsonObject o){
        RequirementData r = new RequirementData();
        r.name = o.getAsJsonObject().get("type").getAsString().toUpperCase();
        r.value = o.getAsJsonObject().get("value").getAsString();
        return r;
    }

    public static ActionData parseAction(JsonObject o){
        ActionData a = new ActionData();
        a.name = o.getAsJsonObject().get("actionType").getAsString();
        a.value = o.get("value").getAsString();
        if(o.has("requirements")){
            JsonArray requirementArray=o.getAsJsonArray("requirements");
            RequirementData[] requirements=new RequirementData[requirementArray.size()];
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

    public static SpawnPokemon parseSpawnPokemon(JsonObject o){
        SpawnPokemon pokemon=new SpawnPokemon();
        pokemon.species=o.getAsJsonObject().get("species").getAsString();
        if(o.has("form")){
            pokemon.form=o.getAsJsonObject().get("form").getAsByte();
        }
        pokemon.minLvl=o.getAsJsonObject().get("minLvl").getAsInt();
        pokemon.maxLvl=o.getAsJsonObject().get("maxLvl").getAsInt();
        pokemon.weight=o.getAsJsonObject().get("weight").getAsInt();
        return pokemon;
    }
}
