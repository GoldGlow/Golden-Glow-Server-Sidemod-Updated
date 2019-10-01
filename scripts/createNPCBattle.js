function dialogClose(event){
	if(event.dialog.getId()==initDialog)
		Packages.com.goldenglow.common.util.scripting.BattleFunctions.createCustomBattle(event.player, team, initDialog, winDialog, loseDialog, event.npc);
	else if(event.dialog.getId()==winDialog){
		Packages.com.goldenglow.common.music.SongManager.setToRouteSong(event.player.getMCEntity());
	}
	else if(event.dialog.getId()==loseDialog){
		event.player.removeDialog(initDialog);
		Packages.com.goldenglow.common.util.scripting.WorldFunctions.warpToSafeZone(event.player);
	}
}