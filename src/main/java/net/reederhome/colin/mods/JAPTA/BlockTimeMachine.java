package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTimeMachine extends BlockContainer {

	public static final PropertyBool ACTIVE = PropertyBool.create("active");

	public BlockTimeMachine() {
		super(Material.circuits);
		setUnlocalizedName("timeMachine");
		setCreativeTab(JAPTA.tab);
		setHardness(5);
	}
	
	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityTimeMachine();
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		boolean tr = false;
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityTimeMachine) {
			tr = ((TileEntityTimeMachine)te).active;
		}
		return state.withProperty(ACTIVE, tr);
	}
}