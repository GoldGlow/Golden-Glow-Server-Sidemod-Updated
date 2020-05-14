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
	return Packages.com.goldenglow.common.util.scripting.InventoryFunctions.unlockTM(player, itemID);
}

function addKeyItemAndQuestObjective(player, questId, objectiveId, itemstack){
	addKeyItem(player, itemstack);
	var quests=player.getActiveQuests();
	for(var i=0;i<quests.length;i++){
		if(quests[i].getId()==questId){
			quests[i].getObjectives(player)[objectiveId].setProgress(quests[i].getObjectives(player)[objectiveId].getProgress()+1);
		}
	}
}

function addAwFromName(player, name){
	Packages.com.goldenglow.common.util.scripting.InventoryFunctions.addAwFromName(player, name);
}

function clearAwItems(player){
	Packages.com.goldenglow.common.util.scripting.InventoryFunctions.clearAwItems(player);
}

function setScoreboard(player){
	Packages.com.goldenglow.common.util.scripting.OtherFunctions.setScoreboard(player);
}

function checkEquippedHead(player, displayName){
	var value=Packages.com.goldenglow.common.util.scripting.OtherFunctions.checkEquippedHead(player, displayName);
	return value;
}

function formChange(player, species, form){
	Packages.com.goldenglow.common.inventory.ChangeFormInventory.openInventory(player.getMCEntity(), species, form);
}

function battleDialog(player, npc, lines, time){
	com.goldenglow.common.util.scripting.BattleFunctions.battleDialog(player, npc, lines, time);
}

function pokemonKOd(bcb){
	return com.goldenglow.common.util.scripting.BattleFunctions.pokemonKOd(bcb);
}

function getRemainingPokemon(bcb){
	return com.goldenglow.common.util.scripting.BattleFunctions.getRemainingPokemon(bcb);
}

function isNpcVisible(player, npc){
	return com.goldenglow.common.util.scripting.VisibilityFunctions.isNpcVisible(player, npc);
}