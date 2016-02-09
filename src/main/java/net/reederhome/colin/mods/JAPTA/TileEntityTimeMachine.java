package net.reederhome.colin.mods.JAPTA;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class TileEntityTimeMachine extends TileEntityJPT implements ITickable {

	int use = 200;
	int jump = 9;

	boolean active = false;
	
	public void update() {
		boolean nm = false;
		if(worldObj.isBlockIndirectlyGettingPowered(getPos()) == 0 && amount >= use) {
			worldObj.setWorldTime(worldObj.getWorldTime()+jump);
			amount-=use;
			nm = true;
		}
		active = nm;
	};
	
	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return 10000;
	}

}
