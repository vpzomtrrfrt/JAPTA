package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockTimeMachine extends BlockContainer {

	IIcon activeIcon;
	public BlockTimeMachine() {
		super(Material.circuits);
		setBlockName("timeMachine");
		setBlockTextureName(JAPTA.modid+":timeMachine");
		setCreativeTab(JAPTA.tab);
		setHardness(5);
	}
	
	public void registerBlockIcons(IIconRegister ir) {
		super.registerBlockIcons(ir);
		activeIcon = ir.registerIcon(textureName+"Active");
	}
	
	public IIcon getIcon(int side, int meta) {
		if(meta==0) {
			return blockIcon;
		}
		else {
			return activeIcon;
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityTimeMachine();
	}

}