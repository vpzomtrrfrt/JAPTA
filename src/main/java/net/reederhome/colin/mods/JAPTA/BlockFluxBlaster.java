package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockFluxBlaster extends BlockContainer {

	public BlockFluxBlaster() {
		super(Material.rock);
		setCreativeTab(JAPTA.tab);
		setBlockName("fluxBlaster");
		setBlockTextureName(JAPTA.modid+":fluxBlaster_front");
		setHardness(1);
	}

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityFluxBlaster();
	}
	
	public IIcon getIcon(int side, int meta) {
		//return side==meta?this.blockIcon:(side==Facing.oppositeSide[meta]?Blocks.dispenser.getIcon(0, 2):sideIcon);
		return side==meta?this.blockIcon:Blocks.dispenser.getIcon(side, meta);
	}
	
	public void onBlockPlacedBy(World world, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
		world.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, Facing.oppositeSide[BlockPistonBase.determineOrientation(world, p_149689_2_, p_149689_3_, p_149689_4_, p_149689_5_)], 3);
	}
	
}