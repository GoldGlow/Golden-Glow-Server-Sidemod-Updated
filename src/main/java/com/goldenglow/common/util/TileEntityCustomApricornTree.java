package com.goldenglow.common.util;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityApricornTree;
import net.minecraft.world.World;
import noppes.npcs.blocks.tiles.TileScripted;

public class TileEntityCustomApricornTree extends TileEntityApricornTree {

    public TileScripted tile;

    public TileEntityCustomApricornTree(World world) {
        this.world = world;
        this.tile = new TileScripted();
        this.tile.setWorld(this.world);
    }
}
