function achievement(player, firstLine, secondLine){
	Packages.com.goldenglow.common.util.scripting.OtherFunctions.showAchievement(player, firstLine, secondLine);
}

function playSong(player){
	Packages.com.goldenglow.common.util.scripting.MusicSoundFunctions.playSong(player);
}

function setCurrentSong(player, song){
	Packages.com.goldenglow.common.music.SongManager.setCurrentSong(player.getMCEntity(), song);
}

function checkRoute(player, lastPosX, lastPosY, lastPosZ){
	Packages.com.goldenglow.common.util.scripting.WorldFunctions.checkRoute(player, lastPosX, lastPosY, lastPosZ);
}

function updatePos(player) {
    lastPosX = player.getX();
    lastPosY = player.getY();
    lastPosZ = player.getZ();
}

function getCurrentDay(world){
	return Packages.com.goldenglow.common.util.scripting.WorldFunctions.getCurrentDay(world);
}

function newRLDay(world){
	Packages.com.goldenglow.common.util.scripting.WorldFunctions.newRLDay(world);
}

function addKeyItem(player, itemstack){
	Packages.com.goldenglow.common.util.scripting.InventoryFunctions.addKeyItem(player, itemstack);
}

function removeKeyItem(player, displayName){
	Packages.com.goldenglow.common.util.scripting.InventoryFunctions.removeKeyItem(player, displayName);	
}

function unlockTM(player, itemID){
	Packages.com.goldenglow.common.util.scripting.InventoryFunctions.unlockTM(player, itemID);
}

function addKeyItemAndQuestObjective(player, questId, objectiveId, itemstack){
	addKeyItem(player, itemstack);
	var quests=player.getActiveQuests();
	for(var i=0;i<quests.length;i++){
		if(quests[i].getId()==questId){
			quests[i].getObjectives(player)[objectiveId].setProgress(quests[i].getObjectives(player)[objectiveId]++);
		}
	}
}

function setScoreboard(player){
	Packages.com.goldenglow.common.util.scripting.OtherFunctions.setScoreboard(player);
}