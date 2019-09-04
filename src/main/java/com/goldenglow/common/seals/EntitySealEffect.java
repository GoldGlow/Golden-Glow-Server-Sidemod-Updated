package com.goldenglow.common.seals;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EntitySealEffect extends Entity {

    public Map<Integer, ArrayList<ParticleFrame>> frames = new HashMap<>();

    public EntitySealEffect(World worldIn) {
        super(worldIn);
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        ArrayList<ParticleFrame> frame = frames.get(this.ticksExisted);
        if(frame!=null && !frame.isEmpty()) {
            System.out.println("Found frame");
            for (ParticleFrame particle : frame) {
                this.world.spawnParticle(particle.type, particle.posX, particle.posY, particle.posZ, particle.speedX, particle.speedY, particle.speedZ);
                System.out.println("Spawned particle");
            }
        }
    }

    protected void entityInit() {
    }

    protected void readEntityFromNBT(NBTTagCompound compound) {
    }

    protected void writeEntityToNBT(NBTTagCompound compound) {
    }
}
