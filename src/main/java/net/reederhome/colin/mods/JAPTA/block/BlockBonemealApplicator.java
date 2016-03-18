package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.tileentity.TileEntityBonemealApplicator;

public class BlockBonemealApplicator extends BlockContainer {
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBonemealApplicator();
    }

    public BlockBonemealApplicator() {
        super(Material.rock);
        setUnlocalizedName("bonemealApplicator");
        setHardness(1);
        setCreativeTab(JAPTA.tab);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState p_getRenderType_1_) {
        return EnumBlockRenderType.MODEL;
    }
}
