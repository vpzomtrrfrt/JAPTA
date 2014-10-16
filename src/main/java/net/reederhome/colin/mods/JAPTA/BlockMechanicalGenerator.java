package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockMechanicalGenerator extends BlockContainer {

	IIcon topIcon;
	
	public BlockMechanicalGenerator() {
		super(Material.rock);
		setCreativeTab(JAPTA.tab);
		setBlockName("mechanicalGenerator");
		setBlockTextureName(JAPTA.modid+":mechanicalGenerator");
	}
	
	public void onNeighborBlockChange(World world, int x, int y, int z, Block b) {
		TileEntityMechanicalGenerator te = (TileEntityMechanicalGenerator) world.getTileEntity(x, y, z);
		if(te.amount+te.inc<te.maxAmount) {
			te.amount+=te.inc;
		}
		else {
			te.amount=te.maxAmount;
		}
		te.transmit();
	}
	
	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityMechanicalGenerator();
	}
	
	public void registerBlockIcons(IIconRegister ir) {
		super.registerBlockIcons(ir);
		topIcon = ir.registerIcon(getTextureName()+"Top");
	}
	
	public IIcon getIcon(int side, int meta) {
		return side<2?topIcon:blockIcon;
	}

}