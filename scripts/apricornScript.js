function interact(event){
	if(Packages.com.goldenglow.common.util.NPCFunctions.hasWaitedForDay(event.player, event.block)){
		event.player.giveItem("pixelmon:"+color+"_apricorn", 0, 1);
		event.player.message("Obtained a "+color+" apricorn!");
	}
	else{
		event.player.message("You already obtained a "+color+" apricorn today!");
	}
}