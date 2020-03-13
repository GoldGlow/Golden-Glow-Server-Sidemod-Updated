package com.goldenglow.common.util.actions.types;

import com.goldenglow.common.inventory.BagInventories;
import com.goldenglow.common.teams.DepositoryPokemon;
import com.goldenglow.common.util.actions.Action;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.entity.player.EntityPlayerMP;

public class DepositoryPokemonAction implements Action {
    public final String name="DEPOSITORY_POKEMON";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        String[] args=value.split(" ");
        if(EnumSpecies.hasPokemonAnyCase(args[0])){
            PokemonSpec pokemonSpec= PokemonSpec.from(args[0]);
            if(args.length==2){
                int form=Integer.parseInt(args[1]);
                int formIndex=0;
                while(formIndex<form){
                    pokemonSpec.form++;
                    formIndex++;
                }
            }
            Pokemon pokemon= DepositoryPokemon.generateDepositoryPokemon(pokemonSpec);
            Pixelmon.storageManager.getParty(player).add(pokemon);
        }
    }
}
