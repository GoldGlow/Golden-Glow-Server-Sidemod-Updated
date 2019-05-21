function playSong(player, song){
	Packages.com.goldenglow.common.util.NPCFunctions.playSong(player, song);
}

function playRouteSong(player){
	Packages.com.goldenglow.common.music.SongManager.playRouteSong(player);
}

function playSound(player, source, sound){
	Packages.com.goldenglow.common.util.NPCFunctions.playSound(player, sound, song);
}

function stopSong(player){
	Packages.com.goldenglow.common.util.NPCFunctions.stopSong(player);
}

function createNPCBattle(event, team, initDialog, winDialog, loseDialog){
	if(event.dialog.getId()==initDialog)
		Packages.com.goldenglow.common.util.NPCFunctions.createCustomBattle(event.player, team, initDialog, winDialog, loseDialog, event.npc);
	else if(event.dialog.getId()==winDialog){
		stopSong(event.player.getMCEntity());
		playRouteSong(event.player.getMCEntity());
	}
}

function createLOSbattle(npc, initDialog){
	Packages.com.goldenglow.common.util.NPCFunctions.registerLOSBattle(npc, initDialog);
}

function createInstancedInv(player, items, containerName, questId){
	Packages.com.goldenglow.common.util.NPCFunctions.createInstancedInv(player, items, containerName, questId);
}

function setPixelmonModel(npc, name){
	Packages.com.goldenglow.common.util.NPCFunctions.setPixelmon(npc, name);
}

function checkRoute(player){
	Packages.com.goldenglow.common.util.NPCFunctions.checkRoute(player);
}

function removeRouteLogout(){
	Packages.com.goldenglow.common.util.NPCFunctions.removeRouteLogout(player);
}

function createDoubleNPCBattle(player, firstX, firstY, firstZ, team1, secondX, secondY, secondZ, team2){
	var world=player.getWorld();
	var pos=player.getPos();
	Packages.com.goldenglow.common.util.NPCFunctions.createNPCBattle(world.getClosestEntity(pos.add(firstX-pos.getX(), firstY-player.getPos().getY(), firstZ-pos.getZ()), 1, 2), team1, world.getClosestEntity(pos.add(secondX-pos.getX(), secondY-pos.getY(), secondZ-pos.getZ()), 1, 2), team2);
}

function flashingRedstone(time){
	if(time.toLowerCase()==="night"){
		if(event.block.getWorld().getTime()<13000||event.block.getWorld().getTime()>23000)
		{
			event.block.setRedstonePower(0);
		}
		else{
			if(!lit){
				event.block.setRedstonePower(15);
			}
			else{
				event.block.setRedstonePower(0);
			}
			lit=!lit
		}
	}
	else if(time.toLowerCase()==="day"){
		if(event.block.getWorld().getTime()<13000||event.block.getWorld().getTime()>23000)
		{
			if(!lit) {
				event.block.setRedstonePower(0);
			} else {
			event.block.setRedstonePower(15);
			}
			lit=!lit;
		}
		else{
			event.block.setRedstonePower(0);
		}
	}
}

function set

function setAWModel(block, file){
	Packages.com.goldenglow.common.util.NPCFunctions.setAWModel(block, file);
}