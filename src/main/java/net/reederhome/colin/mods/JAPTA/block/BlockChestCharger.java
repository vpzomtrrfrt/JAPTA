package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.tileentity.TileEntityChestCharger;

public class BlockChestCharger extends BlockContainer {
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityChestCharger();
    }

    public BlockChestCharger() {
        super(Material.rock);
        setHardness(2);
        setCreativeTab(JAPTA.tab);
        setUnlocalizedName("chestCharger");
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState p_getRenderType_1_) {
        return EnumBlockRenderType.MODEL;
    }
}
