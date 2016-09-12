package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.reederhome.colin.mods.JAPTA.EnumConverterMode;
import net.reederhome.colin.mods.JAPTA.tileentity.TileEntityHeatConverter;

public class BlockHeatConverter extends BlockConverter {
    @Override
    public String getConverterType() {
        return "Heat";
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityHeatConverter();
    }

    @Override
    public String getModeName(EnumConverterMode mode) {
        if (mode == EnumConverterMode.DEPLOY) {
            return "Heat";
        } else {
            return super.getModeName(mode);
        }
    }
}
