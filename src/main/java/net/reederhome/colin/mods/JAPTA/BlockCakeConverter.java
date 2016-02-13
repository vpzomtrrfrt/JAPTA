package net.reederhome.colin.mods.JAPTA;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCakeConverter extends BlockConverter {

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityCakeConverter();
    }

    @Override
    public String getConverterType() {
        return "Cake";
    }
}
