package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = JAPTA.modid, name = JAPTA.name)
public class JAPTA {

	public static final String modid = "JAPTA";
	public static final String name = "JAPTA";
	
	public static CreativeTabs tab = new CreativeTabs(CreativeTabs.getNextID(), "JAPTA") {

		@Override
		public Item getTabIconItem() {
			return batteryPotato;
		}
		
	};
	
	public static Block rngQuarry = new BlockRNGQuarry();
	public static Block mechanicalGenerator = new BlockMechanicalGenerator();
	public static Block lifeConverter = new BlockLifeConverter();
	public static Block energyTeleporter = new BlockEnergyTeleporter();
	public static Block chargingPlate = new BlockChargingPlate();
	
	public static Item batteryPotato = new ItemBatteryPotato();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent ev) {
		GameRegistry.registerBlock(rngQuarry, "rngQuarry");
		GameRegistry.registerBlock(mechanicalGenerator, "mechanicalGenerator");
		GameRegistry.registerBlock(lifeConverter, "lifeConverter");
		GameRegistry.registerBlock(energyTeleporter, ItemBlockOne.class, "energyTeleporter");
		GameRegistry.registerBlock(chargingPlate, "chargingPlate");
		
		GameRegistry.registerItem(batteryPotato, "batteryPotato");
		
		GameRegistry.registerTileEntity(TileEntityRNGQuarry.class, "RNGQuarry");
		GameRegistry.registerTileEntity(TileEntityMechanicalGenerator.class, "MechanicalGenerator");
		GameRegistry.registerTileEntity(TileEntityLifeConverter.class, "LifeConverter");
		GameRegistry.registerTileEntity(TileEntityEnergyTeleporter.class, "EnergyTeleporter");
		GameRegistry.registerTileEntity(TileEntityChargingPlate.class, "ChargingPlate");
		
		GameRegistry.addRecipe(new ItemStack(rngQuarry), "s s", "iri", "wgw", 's', Blocks.stone, 'r', Items.redstone, 'i', Items.iron_ingot, 'w', Items.wooden_pickaxe, 'g', Items.gold_ingot);
		GameRegistry.addRecipe(new ItemStack(mechanicalGenerator), "rrr", "sgs", "sgs", 'r', Items.redstone, 's', Blocks.stone, 'g', Items.gold_nugget);
		GameRegistry.addRecipe(new ItemStack(lifeConverter), "frf", "rgr", "frf", 'f', Items.rotten_flesh, 'r', Items.redstone, 'g', Items.gold_ingot);
		GameRegistry.addRecipe(new ItemStack(energyTeleporter), "prp", "rer", "prp", 'p', Items.ender_pearl, 'r', Items.redstone, 'e', Items.ender_eye);
		GameRegistry.addRecipe(new ItemStack(chargingPlate), "   ", "gpg", "oro", 'g', Items.glowstone_dust, 'p', Blocks.stone_pressure_plate, 'o', Blocks.obsidian, 'r', Blocks.redstone_block);
		
		GameRegistry.addShapelessRecipe(new ItemStack(batteryPotato, 1, ItemBatteryPotato.maxAmount), Items.potato, Items.gold_nugget, Items.iron_ingot, Items.redstone);
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent ev) {
		int x = (int)Math.floor(ev.entity.posX);
		int y = (int)Math.floor(ev.entity.posY-1);
		int z = (int)Math.floor(ev.entity.posZ);
		TileEntity te = ev.entity.worldObj.getTileEntity(x,y,z);
		if(te instanceof TileEntityLifeConverter&&ev.entity.worldObj.getBlockMetadata(x, y, z)==0) {
			if(((TileEntityLifeConverter) te).amount+ev.ammount*TileEntityLifeConverter.inc<=TileEntityLifeConverter.maxAmount) {
				((TileEntityLifeConverter) te).amount+=ev.ammount*TileEntityLifeConverter.inc;
			}
			else {
				((TileEntityLifeConverter) te).amount=TileEntityLifeConverter.maxAmount;
			}
			((TileEntityLifeConverter) te).transmit();
		}
	}
}