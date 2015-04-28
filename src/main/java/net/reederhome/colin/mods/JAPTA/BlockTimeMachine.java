package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTimeMachine extends BlockContainer {

	public BlockTimeMachine() {
		super(Material.circuits);
		setBlockName("timeMachine");
		setBlockTextureName(JAPTA.modid+":timeMachine");
		setCreativeTab(JAPTA.tab);
	}
	
	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityTimeMachine();
	}

}