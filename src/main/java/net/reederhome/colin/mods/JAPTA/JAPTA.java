package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
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
	public static Block elevatorShaft = new BlockElevatorShaft();
	public static Block elevatorTop = new BlockElevatorTop();
	public static Block timeMachine = new BlockTimeMachine();
	public static Block chestCharger = new BlockChestCharger();
	
	public static Item batteryPotato = new ItemBatteryPotato();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent ev) {
		GameRegistry.registerBlock(rngQuarry, "rngQuarry");
		GameRegistry.registerBlock(mechanicalGenerator, "mechanicalGenerator");
		GameRegistry.registerBlock(lifeConverter, "lifeConverter");
		GameRegistry.registerBlock(energyTeleporter, "energyTeleporter");
		GameRegistry.registerBlock(chargingPlate, "chargingPlate");
		GameRegistry.registerBlock(elevatorShaft, "elevatorShaft");
		GameRegistry.registerBlock(elevatorTop, "elevatorTop");
		GameRegistry.registerBlock(timeMachine, "timeMachine");
		GameRegistry.registerBlock(chestCharger, "chestCharger");
		
		GameRegistry.registerItem(batteryPotato, "batteryPotato");
		
		GameRegistry.registerTileEntity(TileEntityRNGQuarry.class, "RNGQuarry");
		GameRegistry.registerTileEntity(TileEntityMechanicalGenerator.class, "MechanicalGenerator");
		GameRegistry.registerTileEntity(TileEntityLifeConverter.class, "LifeConverter");
		GameRegistry.registerTileEntity(TileEntityEnergyTeleporter.class, "EnergyTeleporter");
		GameRegistry.registerTileEntity(TileEntityChargingPlate.class, "ChargingPlate");
		GameRegistry.registerTileEntity(TileEntityElevatorTop.class, "ElevatorTop");
		GameRegistry.registerTileEntity(TileEntityTimeMachine.class, "TimeMachine");
		GameRegistry.registerTileEntity(TileEntityChestCharger.class, "ChestCharger");
		
		GameRegistry.addRecipe(new ShapedOreRecipe(rngQuarry, "s s", "iri", "wgw", 's', "stone", 'r', "dustRedstone", 'i', "ingotIron", 'w', Items.wooden_pickaxe, 'g', "ingotGold"));
		GameRegistry.addRecipe(new ShapedOreRecipe(mechanicalGenerator, "rrr", "sgs", "sgs", 'r', "dustRedstone", 's', "stone", 'g', "nuggetGold"));
		GameRegistry.addRecipe(new ShapedOreRecipe(lifeConverter, "frf", "rgr", "frf", 'f', Items.rotten_flesh, 'r', "dustRedstone", 'g', "ingotGold"));
		GameRegistry.addRecipe(new ShapedOreRecipe(energyTeleporter, "prp", "rer", "prp", 'p', Items.ender_pearl, 'r', "dustRedstone", 'e', Items.ender_eye));
		GameRegistry.addRecipe(new ShapedOreRecipe(chargingPlate, "   ", "gpg", "oro", 'g', "dustGlowstone", 'p', Blocks.stone_pressure_plate, 'o', Blocks.obsidian, 'r', "blockRedstone"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(elevatorShaft, 4), "igi", "igi", "igi", 'i', "ingotIron", 'g', "blockGlass"));
		GameRegistry.addRecipe(new ShapedOreRecipe(elevatorTop, "grg", "rer", "rsr", 'r', "dustRedstone", 'e', Items.ender_pearl, 's', elevatorShaft, 'g', "nuggetGold"));
		GameRegistry.addRecipe(new ShapedOreRecipe(timeMachine, "oro", "rcr", "oro", 'o', Blocks.obsidian, 'r', "blockRedstone", 'c', Items.clock));
		GameRegistry.addRecipe(new ShapedOreRecipe(chestCharger, "rRr", "gcg", "rRr", 'r', "dustRedstone", 'R', "blockRedstone", 'g', "nuggetGold", 'c', Blocks.chest));
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(batteryPotato, 1, ItemBatteryPotato.maxAmount), "cropPotato", "nuggetGold", "ingotIron", "dustRedstone"));
		
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