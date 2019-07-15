package com.goldenglow.common.tiles;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityApricornTree;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import noppes.npcs.blocks.tiles.TileScripted;

public class TileEntityCustomApricornTree extends TileEntityApricornTree implements ICustomScript, ITickable {

    private TileScripted tile;

    public TileEntityCustomApricornTree() {
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
