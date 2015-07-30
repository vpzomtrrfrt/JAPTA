package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockElevatorTop extends BlockContainer {

	public BlockElevatorTop() {
		super(Material.glass);
		setCreativeTab(JAPTA.tab);
		setBlockTextureName(JAPTA.modid+":elevatorTop");
		setBlockName("elevatorTop");
		setHardness(1);
	}
	
	public IIcon getIcon(int s, int m) {
		if(s==1) {
			return super.getIcon(s, m);
		}
		else {
			return JAPTA.elevatorShaft.getIcon(s, m);
		}
	}
	
	public boolean renderAsNormalBlock() {
		return false;
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
	
	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityElevatorTop();
	}

}