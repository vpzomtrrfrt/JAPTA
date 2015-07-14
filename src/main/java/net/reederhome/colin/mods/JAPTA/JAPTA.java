package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
	public static Block chargingPlate = new BlockChargingPlate(false);
	public static Block chargingPlateWooden = new BlockChargingPlate(true);
	public static Block elevatorShaft = new BlockElevatorShaft();
	public static Block elevatorTop = new BlockElevatorTop();
	public static Block timeMachine = new BlockTimeMachine();
	public static Block chestCharger = new BlockChestCharger();
	public static Block cakeConverter = new BlockCakeConverter();
	public static Block fluxBlaster = new BlockFluxBlaster();
	public static Block mover = new BlockMover();
	public static Block bonemealApplicator = new BlockBonemealApplicator();
	
	public static Item batteryPotato = new ItemBatteryPotato();
	public static Item rfMeter = new ItemRFMeter();
	
	public Configuration config;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent ev) {
		config = new Configuration(ev.getSuggestedConfigurationFile());
		config.load();
		config.addCustomCategoryComment("recipes", "Enable/Disable crafting of certain items");
		
		GameRegistry.registerBlock(rngQuarry, "rngQuarry");
		GameRegistry.registerBlock(mechanicalGenerator, "mechanicalGenerator");
		GameRegistry.registerBlock(lifeConverter, "lifeConverter");
		GameRegistry.registerBlock(energyTeleporter, "energyTeleporter");
		GameRegistry.registerBlock(chargingPlate, "chargingPlate");
		GameRegistry.registerBlock(chargingPlateWooden, "chargingPlateWooden");
		GameRegistry.registerBlock(elevatorShaft, "elevatorShaft");
		GameRegistry.registerBlock(elevatorTop, "elevatorTop");
		GameRegistry.registerBlock(timeMachine, "timeMachine");
		GameRegistry.registerBlock(chestCharger, "chestCharger");
		GameRegistry.registerBlock(cakeConverter, "cakeConverter");
		GameRegistry.registerBlock(fluxBlaster, "fluxBlaster");
		GameRegistry.registerBlock(mover, "mover");
		GameRegistry.registerBlock(bonemealApplicator, "bonemealApplicator");
		
		GameRegistry.registerItem(batteryPotato, "batteryPotato");
		GameRegistry.registerItem(rfMeter, "rfMeter");
		
		GameRegistry.registerTileEntity(TileEntityRNGQuarry.class, "RNGQuarry");
		GameRegistry.registerTileEntity(TileEntityMechanicalGenerator.class, "MechanicalGenerator");
		GameRegistry.registerTileEntity(TileEntityLifeConverter.class, "LifeConverter");
		GameRegistry.registerTileEntity(TileEntityEnergyTeleporter.class, "EnergyTeleporter");
		GameRegistry.registerTileEntity(TileEntityChargingPlate.class, "ChargingPlate");
		GameRegistry.registerTileEntity(TileEntityElevatorTop.class, "ElevatorTop");
		GameRegistry.registerTileEntity(TileEntityTimeMachine.class, "TimeMachine");
		GameRegistry.registerTileEntity(TileEntityChestCharger.class, "ChestCharger");
		GameRegistry.registerTileEntity(TileEntityCakeConverter.class, "CakeConverter");
		GameRegistry.registerTileEntity(TileEntityFluxBlaster.class, "FluxBlaster");
		GameRegistry.registerTileEntity(TileEntityMover.class, "Mover");
		GameRegistry.registerTileEntity(TileEntityBonemealApplicator.class, "BonemealApplicator");
		
		addRecipe(new ShapedOreRecipe(rngQuarry, "s s", "iri", "wgw", 's', "stone", 'r', "dustRedstone", 'i', "ingotIron", 'w', Items.wooden_pickaxe, 'g', "ingotGold"));
		addRecipe(new ShapedOreRecipe(mechanicalGenerator, "rrr", "sgs", "sgs", 'r', "dustRedstone", 's', "stone", 'g', "nuggetGold"));
		addRecipe(new ShapedOreRecipe(lifeConverter, "frf", "rgr", "frf", 'f', Items.rotten_flesh, 'r', "dustRedstone", 'g', "ingotGold"));
		addRecipe(new ShapedOreRecipe(energyTeleporter, "prp", "rer", "prp", 'p', Items.ender_pearl, 'r', "dustRedstone", 'e', Items.ender_eye));
		addRecipe(new ShapedOreRecipe(chargingPlate, "   ", "gpg", "oro", 'g', "dustGlowstone", 'p', Blocks.stone_pressure_plate, 'o', Blocks.obsidian, 'r', "blockRedstone"));
		addRecipe(new ShapedOreRecipe(chargingPlateWooden, "   ", "gpg", "oro", 'g', "dustGlowstone", 'p', Blocks.wooden_pressure_plate, 'o', Blocks.obsidian, 'r', "blockRedstone"));
		addRecipe(new ShapedOreRecipe(new ItemStack(elevatorShaft, 4), "igi", "igi", "igi", 'i', "ingotIron", 'g', "blockGlass"));
		addRecipe(new ShapedOreRecipe(elevatorTop, "grg", "rer", "rsr", 'r', "dustRedstone", 'e', Items.ender_pearl, 's', elevatorShaft, 'g', "nuggetGold"));
		addRecipe(new ShapedOreRecipe(timeMachine, "oro", "rcr", "oro", 'o', Blocks.obsidian, 'r', "blockRedstone", 'c', Items.clock));
		addRecipe(new ShapedOreRecipe(chestCharger, "rRr", "gcg", "oRo", 'r', "dustRedstone", 'R', "blockRedstone", 'g', "nuggetGold", 'c', Blocks.chest, 'o', Blocks.obsidian));
		addRecipe(new ShapedOreRecipe(cakeConverter, "frf", "rgr", "frf", 'f', Blocks.cake, 'r', "dustRedstone", 'g', "ingotGold"));
		addRecipe(new ShapedOreRecipe(fluxBlaster, "dbd", "bgb", "dbd", 'd', "dustRedstone", 'b', "blockRedstone", 'g', "ingotGold"));
		addRecipe(new ShapedOreRecipe(bonemealApplicator, "gbg", "brb", "gbg", 'g', "nuggetGold", 'b', new ItemStack(Items.dye, 1, 15), 'r', "blockRedstone"));
		addRecipe(new ShapedOreRecipe(new ItemStack(mover, 4), "rgr", "gpg", "rgr", 'r', "dustRedstone", 'g', "nuggetGold", 'p', Blocks.piston));
		
		addRecipe(new ShapelessOreRecipe(new ItemStack(batteryPotato, 1, ItemBatteryPotato.maxAmount), "cropPotato", "nuggetGold", "ingotIron", "dustRedstone"));
		addRecipe(new ShapedOreRecipe(rfMeter, "n", "d", "d", 'n', "nuggetGold", 'd', "blockRedstone"));
		
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
		
		if(config.getBoolean("Enable Version Checker", "misc", true, "")) {
			new Thread(new UpdateCheckThread(Loader.instance().activeModContainer().getVersion())).start();
		}
		
		config.save();
	}
	
	public void addRecipe(String name, IRecipe r, boolean def) {
		if(config.getBoolean(name, "recipes", def, "")) {
			GameRegistry.addRecipe(r);
		}
	}
	
	public void addRecipe(String name, IRecipe r) {
		addRecipe(name, r, true);
	}
	
	public void addRecipe(IRecipe r) {
		addRecipe(r.getRecipeOutput().getDisplayName(), r);
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
	
	boolean notified = false;
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTick(TickEvent ev) {
		if(!notified && UpdateCheckThread.ret != null) {
			if(UpdateCheckThread.ret.equals("update")) {
				EntityPlayer p = Minecraft.getMinecraft().thePlayer;
				if(p!=null) {
					p.addChatMessage(new ChatComponentTranslation("text.japta.newversion"));
					notified = true;
				}
			}
			else {
				notified = true;
			}
			//System.out.println(UpdateCheckThread.ret);
		}
	}
}