package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockChargingPlate extends BlockPressurePlate implements ITileEntityProvider {

	public BlockChargingPlate() {
		super(JAPTA.modid+":chargingPlate", Material.circuits, Sensitivity.players);
		setCreativeTab(JAPTA.tab);
		setBlockName("chargingPlate");
		setHardness(1);
		setHarvestLevel("pickaxe", 2);
	}

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityChargingPlate();
	}
	
	public void breakBlock(World w, int x, int y, int z, Block b, int i) {
		w.removeTileEntity(x, y, z);
	}
	
}