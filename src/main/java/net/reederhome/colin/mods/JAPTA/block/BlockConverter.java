package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.reederhome.colin.mods.JAPTA.EnumConverterMode;
import net.reederhome.colin.mods.JAPTA.JAPTA;

public abstract class BlockConverter extends BlockModelContainer {
    public static final PropertyEnum<EnumConverterMode> MODE = PropertyEnum.create("mode", EnumConverterMode.class);

    public BlockConverter() {
        super(Material.rock);
        setUnlocalizedName("converter" + getConverterType());
        setDefaultState(blockState.getBaseState().withProperty(MODE, EnumConverterMode.ABSORB));
        setHardness(2);
        setCreativeTab(JAPTA.tab);
    }

    @Override
    public BlockState createBlockState() {
        return new BlockState(this, MODE);
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
    public boolean onBlockActivated(World w, BlockPos pos, IBlockState state, EntityPlayer p, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!w.isRemote) {
            EnumConverterMode mode = state.getValue(MODE);
            mode = mode.getOpposite();
            w.setBlockState(pos, state.withProperty(MODE, mode));
            p.addChatComponentMessage(new ChatComponentTranslation("text.japta.converter.mode", getModeName(mode), getConverterType()));
        }
        return true;
    }

    public String getModeName(EnumConverterMode mode) {
        return mode.getName();
    }

    public abstract String getConverterType();

}
