function interact(event){
	if(Packages.com.goldenglow.common.util.scripting.WorldFunctions.isDifferentIRLDay(event.player, event.npc))){
		Packages.com.goldenglow.common.util.scripting.BattleFunctions.battleInitDialog(event.player, event.npc, rematchDialog);
	}
}

function dialogClose(event){
	if(event.dialog.getId()==rematchDialog){
		Packages.com.goldenglow.common.util.scripting.BattleFunctions.createCustomBattle(event.player, team, initDialog, winDialog, loseDialog, event.npc);
	}
	else if(event.dialog.getId()==winDialog){
		Packages.com.goldenglow.common.music.SongManager.setToRouteSong(event.player.getMCEntity());
		event.npc.getEntityNBT().getMCNBT().setLong(event.player.getUUID(), Packages.com.goldenglow.common.util.scripting.WorldFunctions.getLastDailyRefresh());
	}
	else if(event.dialog.getId()==loseDialog){
		Packages.com.goldenglow.common.util.scripting.WorldFunctions.warpToSafeZone(event.player);
	}
}