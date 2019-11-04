function dialogClose(event){
	if(event.dialog.getId()==initDialog)
		Packages.com.goldenglow.common.util.scripting.BattleFunctions.createCustomBattle(event.player, team, initDialog, winDialog, loseDialog, event.npc);
	else if(event.dialog.getId()==winDialog){
		Packages.com.goldenglow.common.music.SongManager.setRouteSong(event.player.getMCEntity());
		Packages.com.goldenglow.common.util.scripting.WorldFunctions.setDayChallengedNPC(event.player, event.npc);
	}
	else if(event.dialog.getId()==loseDialog){
		event.player.removeDialog(initDialog);
		Packages.com.goldenglow.common.util.scripting.WorldFunctions.warpToSafeZone(event.player);
	}
}