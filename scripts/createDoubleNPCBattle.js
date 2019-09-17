function dialogClose(event){
	if(event.dialog.getId()==scriptId){
		var world=event.player.getWorld();
		var pos=event.player.getPos();
		Packages.com.goldenglow.common.util.NPCFunctions.createNPCBattle(world.getClosestEntity(pos.add(firstX-pos.getX(), firstY-pos.getY(), firstZ-pos.getZ()), 1, 2), team1, world.getClosestEntity(pos.add(secondX-pos.getX(), secondY-pos.getY(), secondZ-pos.getZ()), 1, 2), team2);
	}
}