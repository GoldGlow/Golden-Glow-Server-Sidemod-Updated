package com.goldenglow.mixin;

import com.goldenglow.common.api.IDrop;
import com.pixelmonmod.pixelmon.entities.npcs.registry.PokemonDropInformation;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PokemonDropInformation.class)
public class BetterPokemonDropInfo implements IDrop {
    @Shadow
    ItemStack mainDrop;
    @Shadow
    ItemStack rareDrop;
    @Shadow
    ItemStack optDrop1;
    @Shadow
    ItemStack optDrop2;
    int mainDropMin;
    @Shadow
    int mainDropMax;
    @Shadow
    int rareDropMin;
    @Shadow
    int rareDropMax;
    @Shadow
    int optDrop1Min;
    @Shadow
    int optDrop1Max;
    @Shadow
    int optDrop2Min;
    @Shadow
    int optDrop2Max;

    @Override
    public ItemStack getMainDrop() {
        return this.mainDrop;
    }
    @Override
    public ItemStack getRareDrop() {
        return this.rareDrop;
    }
    @Override
    public ItemStack getOptDrop1() {
        return this.optDrop1;
    }
    @Override
    public ItemStack getOptDrop2() {
        return this.optDrop2;
    }

    @Override
    public int getMainDropMin() {
        return this.mainDropMin;
    }

    @Override
    public int getMainDropMax() {
        return this.mainDropMax;
    }

    @Override
    public int getRareDropMin() {
        return this.rareDropMin;
    }

    @Override
    public int getRareDropMax() {
        return this.rareDropMax;
    }

    @Override
    public int getOptDrop1Min() {
        return this.optDrop1Min;
    }

    @Override
    public int getOptDrop1Max() {
        return this.optDrop1Max;
    }

    @Override
    public int getOptDrop2Min() {
        return this.optDrop2Min;
    }

    @Override
    public int getOptDrop2Max() {
        return this.optDrop2Max;
    }
}

