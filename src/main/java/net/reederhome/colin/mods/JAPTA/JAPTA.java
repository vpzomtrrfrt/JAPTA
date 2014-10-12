package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.Block;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = JAPTA.modid, name = JAPTA.name)
public class JAPTA {

	public static final String modid = "JAPTA";
	public static final String name = "JAPTA";
	
	public static Block rngQuarry = new BlockRNGQuarry();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent ev) {
		GameRegistry.registerBlock(rngQuarry, "rngQuarry");
		GameRegistry.registerTileEntity(TileEntityRNGQuarry.class, "RNGQuarry");
	}
}