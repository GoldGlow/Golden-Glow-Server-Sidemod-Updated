package com.goldenglow.common.api;

import com.mrcrayfish.furniture.client.GifDownloadThread.ImageDownloadResult;
import com.mrcrayfish.furniture.tileentity.IValueContainer.Entry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Map;

public interface ITileEntityTV {
    void setDisabled(boolean isDisabled);
}
