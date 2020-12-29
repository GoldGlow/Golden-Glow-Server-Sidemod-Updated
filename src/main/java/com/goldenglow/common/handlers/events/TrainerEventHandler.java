package com.goldenglow.common.handlers.events;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.battles.npc.CustomBattleHandler;
import com.goldenglow.common.music.SongManager;
import com.pixelmonessentials.common.util.NpcScriptDataManipulator;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noppes.npcs.api.event.DialogEvent;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.entity.EntityNPCInterface;

public class TrainerEventHandler {
    public TrainerEventHandler(){
    }

    @SubscribeEvent
    public void onDialogOpen(DialogEvent.OpenEvent event){
        ScriptObjectMirror object = NpcScriptDataManipulator.getJavascriptObject((NPCWrapper)event.npc, "ooTrainerData");
        if (object != null) {
            if (event.dialog.getId() == (Integer)object.get("initDialog")) {
                if(object.get("initTheme")!=null){
                    SongManager.setCurrentSong(event.player.getMCEntity(), (String) object.get("initTheme"));
                }
                else{
                    SongManager.setCurrentSong(event.player.getMCEntity(), GoldenGlow.songManager.encounterDefault);
                }
            } else if (event.dialog.getId() == (Integer)object.get("winDialog")) {
                if(object.get("winTheme")!=null){
                    SongManager.setCurrentSong(event.player.getMCEntity(), (String) object.get("winTheme"));
                }
                else{
                    SongManager.setCurrentSong(event.player.getMCEntity(), GoldenGlow.songManager.victoryDefault);
                }
            }
        }
    }

    @SubscribeEvent
    public void onDialogClose(DialogEvent.CloseEvent event) {
        ScriptObjectMirror object = NpcScriptDataManipulator.getJavascriptObject((NPCWrapper)event.npc, "ooTrainerData");
        if (object != null) {
            if (event.dialog.getId() == (Integer)object.get("initDialog")) {
                CustomBattleHandler.createCustomBattle(event.player.getMCEntity(), (EntityNPCInterface)event.npc.getMCEntity());
            } else if (event.dialog.getId() == (Integer)object.get("lossDialog")) {
                event.player.removeDialog((Integer)object.get("initDialog"));
            } else if (event.dialog.getId() == (Integer)object.get("winDialog")) {
                SongManager.setRouteSong(event.player.getMCEntity());
            }
        }

    }
}
