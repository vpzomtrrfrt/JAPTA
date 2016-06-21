package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.reederhome.colin.mods.JAPTA.EnumConverterMode;
import net.reederhome.colin.mods.JAPTA.JAPTA;

public abstract class BlockConverter extends BlockModelContainer {
    public static final PropertyEnum<EnumConverterMode> MODE = PropertyEnum.create("mode", EnumConverterMode.class);

    public BlockConverter() {
        super(Material.ROCK);
        String type = getConverterType();
        setUnlocalizedName("converter" + type);
        setRegistryName(type.substring(0, 1).toLowerCase()+type.substring(1)+"Converter");
        setDefaultState(blockState.getBaseState().withProperty(MODE, EnumConverterMode.ABSORB));
        setHardness(2);
        setCreativeTab(JAPTA.tab);
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, MODE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getBlockState().getBaseState().withProperty(MODE, (meta == 0) ? EnumConverterMode.ABSORB : EnumConverterMode.DEPLOY);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (state.getValue(MODE) == EnumConverterMode.ABSORB) {
            return 0;
        } else {
            return 5;
        }
    }

    @Override
    public boolean onBlockActivated(World w, BlockPos pos, IBlockState state, EntityPlayer p, EnumHand hand, ItemStack stack, EnumFacing p_onBlockActivated_7_, float p_onBlockActivated_8_, float p_onBlockActivated_9_, float p_onBlockActivated_10_) {
        if (!w.isRemote) {
            EnumConverterMode mode = state.getValue(MODE);
            mode = mode.getOpposite();
            w.setBlockState(pos, state.withProperty(MODE, mode));
            p.addChatComponentMessage(new TextComponentTranslation("text.japta.converter.mode", getModeName(mode), getConverterType()));
        }
        return true;
    }

    public String getModeName(EnumConverterMode mode) {
        return mode.getName();
    }

    public abstract String getConverterType();

}
