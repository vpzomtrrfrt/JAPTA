package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockChargingPlate extends BlockPressurePlate implements ITileEntityProvider {

	public BlockChargingPlate(boolean wooden) {
		super(JAPTA.modid+":chargingPlate"+(wooden?"Wooden":""), Material.circuits, wooden?Sensitivity.everything:Sensitivity.players);
		setCreativeTab(JAPTA.tab);
		setBlockName("chargingPlate"+(wooden?"Wooden":""));
		setHardness(1);
		if(wooden) {
			setHarvestLevel("axe", 0);
		}
		else {
			setHarvestLevel("pickaxe", 2);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityChargingPlate();
	}
	
	public void breakBlock(World w, int x, int y, int z, Block b, int i) {
		w.removeTileEntity(x, y, z);
	}
	
}