package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockMover extends BlockContainer {

	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public BlockMover() {
		super(Material.rock);
		setCreativeTab(JAPTA.tab);
		setHardness(1);
		setDefaultState(getBlockState().getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}
	
	public TileEntity createNewTileEntity(World w, int i) {
		return new TileEntityMover();
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		world.setBlockState(pos, state.withProperty(FACING, BlockPistonBase.getFacingFromEntity(world, pos, placer)));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, BlockPistonBase.getFacing(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}

	@Override
	public BlockState getBlockState() {
		return new BlockState(this, FACING);
	}
}