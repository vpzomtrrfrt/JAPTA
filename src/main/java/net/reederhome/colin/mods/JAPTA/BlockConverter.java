package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class BlockConverter extends BlockContainer {
    public static final PropertyEnum<EnumConverterType> MODE = PropertyEnum.create("mode", EnumConverterType.class);

    public BlockConverter() {
        super(Material.rock);
        setUnlocalizedName("converter"+getConverterType());
        setDefaultState(blockState.getBaseState().withProperty(MODE, EnumConverterType.ABSORB));
        setHardness(2);
        setCreativeTab(JAPTA.tab);
    }

    @Override
    public BlockState createBlockState() {
        return new BlockState(this, MODE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getBlockState().getBaseState().withProperty(MODE, (meta==0)?EnumConverterType.ABSORB:EnumConverterType.DEPLOY);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if(state.getValue(MODE) == EnumConverterType.ABSORB) {
            return 0;
        }
        else {
            return 5;
        }
    }

    @Override
    public boolean onBlockActivated(World w, BlockPos pos, IBlockState state, EntityPlayer p, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!w.isRemote) {
            EnumConverterType mode = state.getValue(MODE);
            mode = mode.getOpposite();
            w.setBlockState(pos, state.withProperty(MODE, mode));
            p.addChatComponentMessage(new ChatComponentTranslation("text.japta.converter.mode", getModeName(mode), getConverterType()));
        }
        return true;
    }

    public String getModeName(EnumConverterType mode) {
        return mode.getName();
    }

    public abstract String getConverterType();

    @Override
    public int getRenderType() {
        return 3;
    }
}
