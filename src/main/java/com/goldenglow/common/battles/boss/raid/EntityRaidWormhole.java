package com.goldenglow.common.battles.boss.raid;

import com.goldenglow.common.battles.BattleManager;
import com.pixelmonmod.pixelmon.entities.EntityWormhole;
import net.minecraft.world.World;

public class EntityRaidWormhole extends EntityWormhole {

    int maxAge;

    public EntityRaidWormhole(World world) {
        super(world);
    }

    public EntityRaidWormhole(World worldIn, double x, double y, double z, int maxAge) {
        super(worldIn, x, y, z, maxAge);
        this.maxAge = maxAge;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.maxAge != -1 && this.ticksExisted > this.maxAge) {
            BattleManager.currentRaid.startBattles();
            this.setDead();
        }
    }
}
