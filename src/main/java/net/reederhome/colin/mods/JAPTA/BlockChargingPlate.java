package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class BlockChargingPlate extends BlockBasePressurePlate implements ITileEntityProvider {

	public static final PropertyBool POWERED = PropertyBool.create("powered");

	private boolean wooden;

	public BlockChargingPlate(boolean wooden) {
		super(Material.circuits);
		setCreativeTab(JAPTA.tab);
		setUnlocalizedName("chargingPlate"+(wooden?"Wooden":""));
		setHardness(1);
		if(wooden) {
			setHarvestLevel("axe", 0);
		}
		else {
			setHarvestLevel("pickaxe", 2);
		}
		this.wooden = wooden;
		setDefaultState(getBlockState().getBaseState().withProperty(POWERED, false));
	}

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityChargingPlate();
	}

	@Override
	public void breakBlock(World w, BlockPos pos, IBlockState state) {
		w.removeTileEntity(pos);
	}

	@Override
	protected int computeRedstoneStrength(World worldIn, BlockPos pos) {
		AxisAlignedBB aabb = getSensitiveAABB(pos);
		Class cl = wooden ? Entity.class : EntityPlayer.class;
		List<Entity> l = worldIn.getEntitiesWithinAABB(cl, aabb);
		if(!l.isEmpty()) {
			return 15;
		}
		else {
			return 0;
		}
	}

	@Override
	protected int getRedstoneStrength(IBlockState state) {
		return state.getValue(POWERED) ? 15 : 0;
	}

	@Override
	protected IBlockState setRedstoneStrength(IBlockState state, int strength) {
		return state.withProperty(POWERED, strength > 0);
	}

	@Override
	public BlockState getBlockState() {
		return new BlockState(this, POWERED);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		if(state.getValue(POWERED)) {
			return 1;
		}
		else {
			return 2;
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getBlockState().getBaseState().withProperty(POWERED, meta > 0);
	}
}