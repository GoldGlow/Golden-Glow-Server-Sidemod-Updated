package com.goldenglow.common.tiles;

import moe.plushie.armourers_workshop.common.tileentities.TileEntitySkinnable;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import noppes.npcs.blocks.tiles.TileScripted;

public class TileEntityCustomAW extends TileEntitySkinnable implements ICustomScript, ITickable {

    private TileScripted tile;

    public TileEntityCustomAW() {
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
        tile.update();
    }
}