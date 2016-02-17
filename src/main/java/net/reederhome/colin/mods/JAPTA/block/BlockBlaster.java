package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.reederhome.colin.mods.JAPTA.JAPTA;

public abstract class BlockBlaster extends BlockContainer {

    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);
    public static final int RANGE = 8;

    @Override
    public abstract TileEntity createNewTileEntity(World worldIn, int meta);

    public BlockBlaster() {
        super(Material.rock);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        setUnlocalizedName(getBlasterType()+"Blaster");
        setHardness(1);
        setCreativeTab(JAPTA.tab);
    }

    protected abstract String getBlasterType();

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        EnumFacing ts = BlockPistonBase.getFacingFromEntity(worldIn, pos, placer);
        if(!placer.isSneaking()) {
            ts = ts.getOpposite();
        }
        return getDefaultState().withProperty(FACING, ts);
    }

}
