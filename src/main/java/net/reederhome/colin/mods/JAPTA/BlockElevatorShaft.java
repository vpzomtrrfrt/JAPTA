package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockElevatorShaft extends Block {

	IIcon bottom;
	public BlockElevatorShaft() {
		super(Material.glass);
		setBlockTextureName(JAPTA.modid+":elevatorShaft");
		setCreativeTab(JAPTA.tab);
		setBlockName("elevatorShaft");
		setHardness(1);
	}
	
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	public void registerBlockIcons(IIconRegister ir) {
		super.registerBlockIcons(ir);
		bottom = ir.registerIcon(JAPTA.modid+":elevatorShaftBottom");
	}
	
	public IIcon getIcon(int s, int m) {
		if(s<2) {
			return bottom;
		}
		return super.getIcon(s, m);
	}
	
	public boolean isOpaqueCube() {
		return false;
	}
	
	public int getRenderBlockPass() {
		return 1;
	}
	
	public boolean shouldSideBeRendered(IBlockAccess b, int x, int y, int z, int s) {
		return true;
	}
	
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return AxisAlignedBB.getBoundingBox(i, j-0.3, k, (i + 1), (j + 1), (k + 1));
	}
	
	public void onEntityCollidedWithBlock(World w, int x, int y, int z, Entity ent) {
		if(ent instanceof EntityLivingBase) {
			EntityLivingBase e = (EntityLivingBase) ent;
			if(e.posX>x&&e.posZ>z&&e.posX<x+1&&e.posZ<z+1) {
				int ty = y;
				boolean found = false;
				while(ty<w.getHeight() && !found) {
					if(w.getBlock(x, ty, z).equals(this)) {
						ty++;
					}
					else if(w.getBlock(x, ty, z).equals(JAPTA.elevatorTop)) {
						found = true;
					}
					else {
						break;
					}
				}
				if(found && !w.isRemote) {
					TileEntityElevatorTop te = (TileEntityElevatorTop) w.getTileEntity(x, ty, z);
					int use = te.getEnergyCost(ty-y);
					if(te.amount>=use) {
						e.setPositionAndUpdate(e.posX, ty+1, e.posZ);
						w.playSoundAtEntity(e, "mob.chicken.plop", 1, 1);
						te.amount-=use;
					}
				}
			}
		}
	}
}