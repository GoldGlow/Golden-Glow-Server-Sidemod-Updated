function setModel(blockId){
	function init(event){
		event.block.setModel(blockId);
	}
}

function dialogAchievement(dialogId, firstLine, secondLine){
	function dialogClose(event){
		if(event.dialog.getId()==dialogId){
			event.player.sendNotification(firstLine, secondLine, event.player.getMCEntity().getEntityData().getInt("RouteNotification"));
		}
	}
}

function lightBlock(){
	function init(event){
		event.block.setLight(15);
	}
}

function playSong(player){
	Packages.com.goldenglow.common.util.NPCFunctions.playSong(player);
}

function playSound(player, source, sound){
	Packages.com.goldenglow.common.util.NPCFunctions.playSound(player, sound, song);
}

function createNPCBattle(team, initDialog, winDialog, loseDialog){
	function dialogClose(event){
		if(event.dialog.getId()==initDialog)
			Packages.com.goldenglow.common.util.NPCFunctions.createCustomBattle(event.player, team, initDialog, winDialog, loseDialog, event.npc);
		else if(event.dialog.getId()==winDialog){
			stopSong(event.player.getMCEntity());
			playRouteSong(event.player.getMCEntity());
		}
	}
}

function createLOSbattle(initDialog){
	function init(event){
		Packages.com.goldenglow.common.util.NPCFunctions.registerLOSBattle(event.npc, initDialog);
	}
}

function createInstancedInv(player, items, containerName, questId){
	Packages.com.goldenglow.common.util.NPCFunctions.createInstancedInv(player, items, containerName, questId);
}

function checkRoute(player){
	Packages.com.goldenglow.common.util.NPCFunctions.checkRoute(player);
}

function removeRouteLogout(){
	Packages.com.goldenglow.common.util.NPCFunctions.removeRouteLogout(player);
}

function createDoubleNPCBattle(ScriptId, firstX, firstY, firstZ, team1, secondX, secondY, secondZ, team2){
	function dialogClose(event){
		if(event.dialog.getId()==ScriptId){
			var world=event.player.getWorld();
			var pos=event.player.getPos();
			Packages.com.goldenglow.common.util.NPCFunctions.createNPCBattle(world.getClosestEntity(pos.add(firstX-pos.getX(), firstY-player.getPos().getY(), firstZ-pos.getZ()), 1, 2), team1, world.getClosestEntity(pos.add(secondX-pos.getX(), secondY-pos.getY(), secondZ-pos.getZ()), 1, 2), team2);
		}
	}
}

function openBlockDialog(dialogId){
	function interact(event){
		event.block.executeCommand("noppes dialog show "+event.player.getName()+" "+dialogId+" block");
	}
}

function setAWModel(block, file){
	Packages.com.goldenglow.common.util.NPCFunctions.setAWModel(block, file);
}