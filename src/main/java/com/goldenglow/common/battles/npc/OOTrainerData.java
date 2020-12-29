package com.goldenglow.common.battles.npc;

import com.pixelmonessentials.common.api.data.TrainerNPCData;
import com.pixelmonessentials.common.util.NpcScriptDataManipulator;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.entity.EntityNPCInterface;

public class OOTrainerData extends TrainerNPCData {
    @Override
    public void setNpcTrainerData(EntityNPCInterface npc){
        NPCWrapper npcWrapper = new NPCWrapper(npc);
        ScriptObjectMirror object = NpcScriptDataManipulator.getJavascriptObject(npcWrapper, "ooTrainerData");
        OOTrainerData data=new OOTrainerData();
        if(object!=null){
            data.setInitDialogId((Integer)object.get("initDialog"));
            data.setWinDialogId((Integer)object.get("winDialog"));
            data.setLossDialogId((Integer)object.get("lossDialog"));
            data.setCategoryName((String)object.get("category"));
            data.setTeam((String)object.get("team"));
        }
        data.setCategoryName(this.getCategoryName());
        data.setTeam(this.getTeam());
        data.setInitDialogId(this.getInitDialogId());
        data.setWinDialogId(this.getWinDialogId());
        data.setLossDialogId(this.getLossDialogId());

        if (!this.getRules().equals("")) {
            data.setRules(this.getRules());
        }
        if(!this.getEncounterTheme().equals("")){
            data.setEncounterTheme(this.getEncounterTheme());
        }
        if(!this.getBattleTheme().equals("")){
            data.setBattleTheme(this.getBattleTheme());
        }
        if(!this.getVictoryTheme().equals("")){
            data.setVictoryTheme(this.getVictoryTheme());
        }

        String convertedData = this.dataToObjectString();
        NpcScriptDataManipulator.setJavascriptObject(npcWrapper, "ooTrainerData", convertedData);
    }

    @Override
    public String dataToObjectString(){
        String data="{\n";
        if(!this.getEncounterTheme().equals("")){
            data = data +"    \"initTheme\":\""+this.getEncounterTheme()+"\",\n";
        }
        if(!this.getBattleTheme().equals("")){
            data = data +"    \"battleTheme\":\""+this.getBattleTheme()+"\",\n";
        }
        if(!this.getVictoryTheme().equals("")){
            data = data +"    \"winTheme\":\""+this.getVictoryTheme()+"\",\n";
        }
        if (!this.getRules().equals("")) {
            data = data + "    \"rules\":\"" + this.getRules() + "\",\n";
        }

        data = data + "    \"initDialog\":" + this.getInitDialogId() + ",\n";
        data = data + "    \"winDialog\":" + this.getWinDialogId() + ",\n";
        data = data + "    \"lossDialog\":" + this.getLossDialogId() + ",\n";
        data = data + "    \"category\":\"" + this.getCategoryName() + "\",\n";
        data = data + "    \"team\": \"" + this.getTeam() + "\"\n";
        data = data + "};";
        return data;
    }
}
