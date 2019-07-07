function dialogClose(event){
	if(event.dialog.getId()==dialogId){
		Packages.com.goldenglow.common.util.NPCFunctions.showAchievement(event.player, firstLine, secondLine);
	}
}