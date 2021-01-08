package com.goldenglow.common.events;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import net.minecraftforge.fml.common.eventhandler.Event;

public class BossReceivedDamageEvent extends Event {

    public final PixelmonWrapper source;
    public final PixelmonWrapper target;
    public final float damage;
    public final DamageTypeEnum damageType;

    public BossReceivedDamageEvent(PixelmonWrapper source, PixelmonWrapper target, float damage, DamageTypeEnum damageType) {
        this.source = source;
        this.target = target;
        this.damage = damage;
        this.damageType = damageType;
    }

}
