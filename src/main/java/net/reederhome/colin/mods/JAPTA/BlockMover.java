package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockMover extends BlockContainer {

	IIcon iconFront;
	
	public BlockMover() {
		super(Material.rock);
		setCreativeTab(JAPTA.tab);
		setBlockName("mover");
		setBlockTextureName(JAPTA.modid+":moverSide");
	}
	
	public TileEntity createNewTileEntity(World w, int i) {
		return new TileEntityMover();
	}
	
	public int getRenderType() {
		return 16;
	}
	
	public IIcon getIcon(int side, int meta) {
		return (side==meta||side==Facing.oppositeSide[meta])?iconFront:blockIcon;
	}
	
	public void onBlockPlacedBy(World world, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
		world.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, BlockPistonBase.determineOrientation(world, p_149689_2_, p_149689_3_, p_149689_4_, p_149689_5_), 3);
	}
	
	public void registerBlockIcons(IIconRegister ir) {
		super.registerBlockIcons(ir);
		iconFront = ir.registerIcon(JAPTA.modid+":moverFront");
	}
}