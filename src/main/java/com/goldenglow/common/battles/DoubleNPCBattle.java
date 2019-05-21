package com.goldenglow.common.battles;

import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.entity.EntityNPCInterface;

/**
 * Created by JeanMarc on 5/20/2019.
 */
public class DoubleNPCBattle extends BattleRules
{
    private static EntityNPCInterface firstNpc;
    private static EntityPixelmon firstPokemon;
    private static EntityNPCInterface secondNpc;
    private static EntityPixelmon secondPokemon;

    public DoubleNPCBattle(EntityNPCInterface firstNpc, EntityPixelmon firstPokemon, EntityNPCInterface secondNpc, EntityPixelmon secondPokemon) throws Exception
    {
        super();
        this.firstNpc=firstNpc;
        this.firstPokemon=firstPokemon;
        this.secondNpc=secondNpc;
        this.secondPokemon=secondPokemon;
    }


    public EntityNPCInterface getSecondNpc() {return this.secondNpc;}

    public EntityNPCInterface getFirstNpc()
    {
        return this.firstNpc;
    }

    public EntityPixelmon getFirstPokemon(){
        return this.firstPokemon;
    }

    public EntityPixelmon getSecondPokemon(){
        return this.secondPokemon;
    }
}
