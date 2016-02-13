package net.reederhome.colin.mods.JAPTA;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.util.EnumFacing;

public class TileEntityCakeConverter extends TileEntityJPT implements IEnergyProvider, IEnergyReceiver {
    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return 100000;
    }


}
