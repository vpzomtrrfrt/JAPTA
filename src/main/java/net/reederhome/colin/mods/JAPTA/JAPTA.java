package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = JAPTA.modid, name = JAPTA.name)
public class JAPTA {

	public static final String modid = "JAPTA";
	public static final String name = "JAPTA";
	
	public static CreativeTabs tab = new CreativeTabs(CreativeTabs.getNextID(), "JAPTA") {

		@Override
		public Item getTabIconItem() {
			return Item.getItemFromBlock(rngQuarry);
		}
		
	};
	
	public static Block rngQuarry = new BlockRNGQuarry();
	public static Block mechanicalGenerator = new BlockMechanicalGenerator();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent ev) {
		GameRegistry.registerBlock(rngQuarry, "rngQuarry");
		GameRegistry.registerBlock(mechanicalGenerator, "mechanicalGenerator");
		GameRegistry.registerTileEntity(TileEntityRNGQuarry.class, "RNGQuarry");
		GameRegistry.registerTileEntity(TileEntityMechanicalGenerator.class, "MechanicalGenerator");
		GameRegistry.addRecipe(new ItemStack(rngQuarry), "srs", "iwi", " g ", 's', Blocks.stone, 'r', Items.redstone, 'i', Items.iron_ingot, 'w', Items.wooden_pickaxe, 'g', Items.gold_ingot);
		
	}
}