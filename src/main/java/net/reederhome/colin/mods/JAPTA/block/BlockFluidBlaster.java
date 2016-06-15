package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.reederhome.colin.mods.JAPTA.tileentity.TileEntityFluidBlaster;

public class BlockFluidBlaster extends BlockBlaster {

    public BlockFluidBlaster(boolean inhaler) {
        super(inhaler);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityFluidBlaster();
    }

    @Override
    protected String getBlasterType() {
        return "fluid";
    }
}
