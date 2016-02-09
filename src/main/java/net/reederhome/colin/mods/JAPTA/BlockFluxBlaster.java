package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockFluxBlaster extends BlockContainer {

	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public BlockFluxBlaster() {
		super(Material.rock);
		setCreativeTab(JAPTA.tab);
		setUnlocalizedName("fluxBlaster");
		setHardness(1);
	}

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityFluxBlaster();
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		world.setBlockState(pos, state.withProperty(FACING, BlockPistonBase.getFacingFromEntity(world, pos, placer)));
	}
	
}