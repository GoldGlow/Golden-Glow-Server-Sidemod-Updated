package com.goldenglow.common.util.scripting;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.routes.Route;
import com.goldenglow.common.routes.RouteManager;
import com.goldenglow.common.routes.SpawnPokemon;
import noppes.npcs.api.wrapper.BlockScriptedWrapper;
import noppes.npcs.api.wrapper.PlayerWrapper;

public class DailyFunctions {
    public static void apricornScript(PlayerWrapper playerWrapper, BlockScriptedWrapper block){
        Route apricornRoute= GoldenGlow.routeManager.getRoute(block.getPos().getMCBlockPos());
        BattleFunctions.startWildBattle(playerWrapper, SpawnPokemon.getWeightedPokemonFromList(apricornRoute.apricornPokemon));
    }
}
