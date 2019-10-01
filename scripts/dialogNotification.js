function dialogClose(event){
	if(event.dialog.getId()==dialogId){
		Packages.com.goldenglow.common.util.scripting.OtherFunctions.showAchievement(event.player, firstLine, secondLine);
	}
}