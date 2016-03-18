package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.reederhome.colin.mods.JAPTA.JAPTA;

import java.util.List;

public class BlockPowerCabinet extends Block {
    public static final PropertyInteger VALUE = PropertyInteger.create("value", 0, 15);

    public BlockPowerCabinet() {
        super(Material.rock);
        setDefaultState(blockState.getBaseState().withProperty(VALUE, 0));
        setCreativeTab(JAPTA.tab);
        setUnlocalizedName("powerCabinet");
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VALUE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VALUE, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VALUE);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 15));
    }
}
