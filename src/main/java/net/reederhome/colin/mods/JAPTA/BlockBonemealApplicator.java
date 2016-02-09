package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockBonemealApplicator extends BlockContainer {

	public BlockBonemealApplicator() {
		super(Material.rock);
		setCreativeTab(JAPTA.tab);
		setUnlocalizedName("bonemealApplicator");
		setHardness(1);
	}

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityBonemealApplicator();
	}
}