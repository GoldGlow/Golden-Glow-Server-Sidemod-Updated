package com.goldenglow.mixin;

import com.goldenglow.common.api.ITileEntityTV;
import com.mrcrayfish.furniture.tileentity.TileEntitySyncClient;
import com.mrcrayfish.furniture.tileentity.TileEntityTV;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TileEntityTV.class)
public abstract class RestrictCrayfishTVs extends TileEntity implements ITileEntityTV {
    @Shadow
    private boolean disabled;

    @Override
    public void setDisabled(boolean isDisabled){
        this.disabled=isDisabled;
    }
}
