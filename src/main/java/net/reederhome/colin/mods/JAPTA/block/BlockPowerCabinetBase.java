package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.tileentity.TileEntityPowerCabinetBase;

public class BlockPowerCabinetBase extends BlockContainer {
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityPowerCabinetBase();
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    public BlockPowerCabinetBase() {
        super(Material.iron);
        setCreativeTab(JAPTA.tab);
        setUnlocalizedName("powerCabinetBase");
    }
}
