package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockChestCharger extends BlockContainer {

	public BlockChestCharger() {
		super(Material.rock);
		setCreativeTab(JAPTA.tab);
		setUnlocalizedName("chestCharger");
		setHardness(5);
	}
	
	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityChestCharger();
	}

	
}