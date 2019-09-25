function achievement(player, firstLine, secondLine){
	Packages.com.goldenglow.common.util.NPCFunctions.showAchievement(player, firstLine, secondLine);
}

function playSong(player){
	Packages.com.goldenglow.common.util.NPCFunctions.playSong(player);
}

function setCurrentSong(player, song){
	Packages.com.goldenglow.common.music.SongManager.setCurrentSong(player.getMCEntity(), song);
}

function checkRoute(player, lastPosX, lastPosY, lastPosZ){
	Packages.com.goldenglow.common.util.NPCFunctions.checkRoute(player, lastPosX, lastPosY, lastPosZ);
}

function updatePos(player) {
    lastPosX = player.getX();
    lastPosY = player.getY();
    lastPosZ = player.getZ();
}

function getCurrentDay(world){
	return Packages.com.goldenglow.common.util.NPCFunctions.getCurrentDay(world);
}

function newRLDay(world){
	Packages.com.goldenglow.common.util.NPCFunctions.newRLDay(world);
}

function addKeyItem(player, itemstack){
	Packages.com.goldenglow.common.util.NPCFunctions.addKeyItem(player, itemstack);
}

function removeKeyItem(player, displayName){
	
}Packages.com.goldenglow.common.util.NPCFunctions.removeKeyItem(player, displayName);