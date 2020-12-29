package com.goldenglow.common.util.actions.types.other;

import com.goldenglow.common.teams.DepositoryPokemon;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.entity.player.EntityPlayerMP;

public class DepositoryPokemonAction extends ActionBase {
    public DepositoryPokemonAction(){
        super("DEPOSITORY_POKEMON");
    }

    @Override
    public void doAction(EntityPlayerMP player, ActionData data){
        if(data instanceof ActionStringData){
            String[] args=((ActionStringData) data).getValue().split(" ");
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
}
