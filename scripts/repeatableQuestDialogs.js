function interact(event){
    if(event.player.canQuestBeAccepted(questId)&&!event.player.hasActiveQuest(29)){
        event.player.showDialog(beforeId, event.npc.getName());
    }
    else if(event.player.hasActiveQuest(questId)){
        event.player.showDialog(duringId, event.npc.getName());
    }
    else{
        event.player.showDialog(waitingId, event.npc.getName());
    }
}