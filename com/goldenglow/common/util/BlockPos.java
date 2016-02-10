package com.goldenglow.common.util;

public class BlockPos
{
    public int x;
    public int y;
    public int z;

    public BlockPos(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String toString()
    {
        return this.x + " " + this.y + " " + this.z;
    }
}
