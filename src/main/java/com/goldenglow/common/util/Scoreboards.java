package com.goldenglow.common.util;

import com.goldenglow.common.data.IPlayerData;
import com.goldenglow.common.data.OOPlayerData;
import com.goldenglow.common.data.OOPlayerProvider;
import com.goldenglow.common.util.scripting.WorldFunctions;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.custom.overlays.ScoreboardLocation;
import com.pixelmonmod.pixelmon.comm.packetHandlers.customOverlays.CustomScoreboardDisplayPacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.customOverlays.CustomScoreboardUpdatePacket;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.api.handler.data.IQuestCategory;
import noppes.npcs.api.handler.data.IQuestObjective;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.WorldWrapper;

import java.util.ArrayList;

public class Scoreboards {
    public enum EnumScoreboardType{
        NONE,
        DEBUG,
        QUEST_LOG
    }

    public static void buildScoreboard(EntityPlayerMP player){
        EnumScoreboardType type=player.getCapability(OOPlayerProvider.OO_DATA, null).getScoreboardType();
        if(type==EnumScoreboardType.NONE){
            Pixelmon.network.sendTo(new CustomScoreboardDisplayPacket(ScoreboardLocation.RIGHT_MIDDLE, false), player);
            return;
        }
        else if(type==EnumScoreboardType.DEBUG){
            Scoreboards.buildDebugScoreboard(player);
            return;
        }
        else if(type==EnumScoreboardType.QUEST_LOG){
            Scoreboards.buildQuestLogScoreboard(player);
        }
    }

    public static void buildDebugScoreboard(EntityPlayerMP player){
        IPlayerData playerData=player.getCapability(OOPlayerProvider.OO_DATA, null);
        ArrayList<String> lines = new ArrayList<>();
        lines.add("Route");
        lines.add("Song");
        lines.add("Time");
        lines.add("Day");
        ArrayList<String> scores = new ArrayList<>();
        scores.add(playerData.getRoute().displayName);
        scores.add(playerData.getCurrentSong());
        Long time=player.getServerWorld().getWorldTime()%24000L;
        String extra="";
        if((time%1000)*60/1000<10){
            extra="0";
        }
        scores.add(((time/1000)+6)%24+":"+extra+(time%1000)*60/1000);
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        scores.add(WorldFunctions.getCurrentDay((WorldWrapper) playerWrapper.getWorld())+"");
        Pixelmon.network.sendTo(new CustomScoreboardUpdatePacket("Debug", lines, scores), player);
        Pixelmon.network.sendTo(new CustomScoreboardDisplayPacket(ScoreboardLocation.RIGHT_MIDDLE, true), player);
    }

    public static void buildQuestLogScoreboard(EntityPlayerMP player){
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        IQuest[] activeQuests=playerWrapper.getActiveQuests();
        ArrayList<String> lines = new ArrayList<>();
        ArrayList<String> scores = new ArrayList<>();
        for(IQuest quest:activeQuests){
            String questTitle="";
            IQuestCategory category=quest.getCategory();
            if(category.getName().toLowerCase().contains("main")){
                questTitle=Reference.gold;
            }
            else {
                questTitle=Reference.aqua;
            }
            questTitle+=quest.getName();
            lines.add(questTitle);
            IQuestObjective[] objectives=quest.getObjectives(playerWrapper);
            boolean isComplete=true;
            for(IQuestObjective objective: objectives){
                if(!objective.isCompleted()){
                    isComplete=false;
                    break;
                }
            }
            if(isComplete){
                scores.add(Reference.darkGreen+"COMPLETE");
            }
            else{
                scores.add("");
                for(IQuestObjective objective: objectives){
                    if(objective.isCompleted()){
                        lines.add(" -"+Reference.strike+objective.getText().replace("(read)", ""));
                        scores.add("");
                    }
                    else{
                        lines.add(" -"+objective.getText().replace("(unread)", ""));
                        scores.add(objective.getProgress()+"/"+objective.getMaxProgress());
                    }
                }
            }
        }
        Pixelmon.network.sendTo(new CustomScoreboardUpdatePacket("Quest Log", lines, scores), player);
        Pixelmon.network.sendTo(new CustomScoreboardDisplayPacket(ScoreboardLocation.RIGHT_MIDDLE, true), player);
    }
}
