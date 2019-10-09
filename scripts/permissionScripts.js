function setPrefix(player, prefix){
	Packages.com.goldenglow.common.util.PermissionUtils.setPrefix(player.getMCEntity(), prefix);
}

function addPermission(player, permission){
	Packages.com.goldenglow.common.util.PermissionUtils.addPermissionNode(player.getMCEntity(), permission);
}

function unsetPermissionWithStart(player, start){
	Packages.com.goldenglow.common.util.PermissionUtils.unsetPermissionWithStart(player.getMCEntity(), start);
}

function checkPermission(player, permission){
	Packages.com.goldenglow.common.util.PermissionUtils.checkPermission(player.getMCEntity(), permission);
}