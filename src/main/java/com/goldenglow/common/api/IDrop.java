package com.goldenglow.common.api;

import net.minecraft.item.ItemStack;

public interface IDrop {
    ItemStack getMainDrop();
    ItemStack getRareDrop();
    ItemStack getOptDrop1();
    ItemStack getOptDrop2();
    int getMainDropMin();
    int getMainDropMax();
    int getRareDropMin();
    int getRareDropMax();
    int getOptDrop1Min();
    int getOptDrop1Max();
    int getOptDrop2Min();
    int getOptDrop2Max();
}
