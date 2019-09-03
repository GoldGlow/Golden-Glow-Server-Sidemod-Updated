package com.goldenglow.common.seals;

import net.minecraft.util.EnumParticleTypes;

public class ParticleFrame {

    public ParticleFrame(EnumParticleTypes type, double posX, double posY, double posZ, double speedX, double speedY, double speedZ) {
        this.type=type;
        this.posX = posX; this.posY = posY; this.posZ = posZ;
        this.speedX = speedX; this.speedY = speedY; this.speedZ = speedZ;
    }

    EnumParticleTypes type;
    double posX,posY,posZ;
    double speedX,speedY,speedZ;

}
