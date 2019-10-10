function interact(event){
	if(Packages.com.goldenglow.common.util.scripting.WorldFunctions.hasWaitedForDay(event.player, event.block)){
		event.player.giveItem("pixelmon:"+color+"_apricorn", 0, 1);
		event.player.message("Obtained a "+color+" apricorn!");
		Packages.com.goldenglow.common.util.scripting.DailyFunctions.apricornScript(event.player, event.block);
	}
	else{
		event.player.message("You already obtained a "+color+" apricorn today!");
	}
}