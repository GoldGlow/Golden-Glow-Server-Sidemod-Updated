function interact(event){
	if(Packages.com.goldenglow.common.util.scripting.WorldFunctions.isDifferentIRLDay(event.player, event.block)){
		event.player.giveItem("pixelmon:"+stone+"_stone_shard", 0, 1);
		event.player.message("Obtained a "+stone+" stone shard!");
	}
	else{
		event.player.message("You already obtained a "+stone+" stone shard today!");
	}
}