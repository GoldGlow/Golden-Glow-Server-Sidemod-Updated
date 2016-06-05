package com.goldenglow.common.routes;

import net.minecraft.util.BlockPos;

public class Area {
    public BlockPos pos1;
    public BlockPos pos2;

    public Area() {
    }

    public Area(BlockPos pos) {
        this.pos1 = pos;
    }

    public Area(BlockPos pos1, BlockPos pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }
}
