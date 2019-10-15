function backupTeam(player, folder){
	Packages.com.goldenglow.common.teams.PlayerParty.backupTeam(player.getMCEntity(), folder);
}

function emptyParty(player){
	Packages.com.goldenglow.common.teams.PlayerParty.emptyParty(player.getMCEntity());
}

function loadSavedTeam(player, folder){
	Packages.com.goldenglow.common.teams.PlayerParty.loadSavedTeam(player.getMCEntity(), folder);
}