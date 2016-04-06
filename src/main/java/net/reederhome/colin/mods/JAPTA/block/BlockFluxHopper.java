package net.reederhome.colin.mods.JAPTA.block;

import com.google.common.base.Predicate;
import net.minecraft.block.BlockContainer;
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
import net.reederhome.colin.mods.JAPTA.tileentity.TileEntityFluxHopper;

public class BlockFluxHopper extends BlockModelContainer {

    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class, new Predicate<EnumFacing>() {
        @Override
        public boolean apply(EnumFacing input) {
            return input != EnumFacing.UP;
        }
    });

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityFluxHopper();
    }

    public BlockFluxHopper() {
        super(Material.iron);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN));
        setUnlocalizedName("fluxHopper");
        setHardness(3);
        setCreativeTab(JAPTA.tab);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        EnumFacing dir = facing.getOpposite();
        if (dir == EnumFacing.UP) {
            dir = EnumFacing.DOWN;
        }
        return getDefaultState().withProperty(FACING, dir);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
}
