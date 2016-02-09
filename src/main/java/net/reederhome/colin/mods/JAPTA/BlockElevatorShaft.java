package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockElevatorShaft extends Block {
	public BlockElevatorShaft() {
		super(Material.glass);
		setCreativeTab(JAPTA.tab);
		setUnlocalizedName("elevatorShaft");
		setHardness(1);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		return AxisAlignedBB.fromBounds(pos.getX(), pos.getY()-0.3, pos.getZ(), pos.getX()+1, pos.getY()+1, pos.getZ()+1);
	}

	@Override
	public void onEntityCollidedWithBlock(World w, BlockPos pos, Entity ent) {
		if(ent instanceof EntityLivingBase) {
			EntityLivingBase e = (EntityLivingBase) ent;
			if(e.posX>pos.getX()&&e.posZ>pos.getZ()&&e.posX<pos.getX()+1&&e.posZ<pos.getZ()+1) {
				BlockPos cp = pos;
				boolean found = false;
				while(cp.getY() < w.getHeight() && !found) {
					if(w.getBlockState(cp).getBlock().equals(this)) {
						cp = cp.up();
					}
					else if(w.getBlockState(cp).getBlock().equals(JAPTA.elevatorTop)) {
						found = true;
					}
					else {
						break;
					}
				}
				if(found && !w.isRemote) {
					TileEntityElevatorTop te = (TileEntityElevatorTop) w.getTileEntity(cp);
					int use = te.getEnergyCost(cp.getY()-pos.getY());
					if(te.amount>=use) {
						e.setPositionAndUpdate(e.posX, cp.getY()+1, e.posZ);
						w.playSoundAtEntity(e, "mob.chicken.plop", 1, 1);
						te.amount-=use;
					}
				}
			}
		}
	}
}