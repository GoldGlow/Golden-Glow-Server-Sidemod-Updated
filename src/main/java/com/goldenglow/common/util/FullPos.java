package com.goldenglow.common.util;

import com.flowpowered.math.vector.Vector3d;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.World;

public class FullPos {
    World world;
    BlockPos pos;

    public FullPos(World world, BlockPos pos){
        this.world=world;
        this.pos=pos;
    }

    public FullPos(EntityPlayerMP player){
        this.world=Sponge.getServer().getPlayer(player.getUniqueID()).get().getWorld();
        this.pos=player.getPosition();
    }

    public void warpToWorldPos(EntityPlayerMP player){
        Sponge.getServer().getPlayer(player.getUniqueID()).get().setLocation(new Vector3d(this.pos.getX(), this.pos.getY(), this.pos.getZ()), this.world.getUniqueId());
    }

    public World getWorld(){
        return this.world;
    }

    public BlockPos getPos(){
        return this.pos;
    }
}
