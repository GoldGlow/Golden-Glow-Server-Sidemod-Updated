package com.goldenglow.common.tiles;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityBerryTree;
import net.minecraft.world.World;
import noppes.npcs.blocks.tiles.TileScripted;

public class TileEntityCustomBerryTree extends TileEntityBerryTree implements ICustomScript {

    private TileScripted tile;

    public TileEntityCustomBerryTree() {
        this.tile = new TileScripted();
    }

    @Override
    protected void setWorldCreate(World worldIn) {
        super.setWorldCreate(worldIn);
        this.tile.setWorld(worldIn);
    }

    @Override
    public void setWorld(World worldIn) {
        super.setWorld(worldIn);
        this.tile.setWorld(worldIn);
    }

    @Override
    public TileScripted getScriptedTile() {
        return this.tile;
    }

    @Override
    public void update() {
        super.update();
        tile.update();
    }
}
