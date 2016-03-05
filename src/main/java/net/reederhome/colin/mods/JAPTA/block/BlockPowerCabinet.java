package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;

public class BlockPowerCabinet extends Block {
    public static final PropertyInteger VALUE = PropertyInteger.create("value", 0, 15);

    public BlockPowerCabinet() {
        super(Material.rock);
        setDefaultState(blockState.getBaseState().withProperty(VALUE, 0));
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, VALUE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VALUE, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VALUE);
    }
}
