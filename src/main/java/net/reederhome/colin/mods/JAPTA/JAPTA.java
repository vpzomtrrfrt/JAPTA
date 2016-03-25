package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.reederhome.colin.mods.JAPTA.block.*;
import net.reederhome.colin.mods.JAPTA.item.ItemBatteryPotato;
import net.reederhome.colin.mods.JAPTA.item.ItemBlockPowerCabinet;
import net.reederhome.colin.mods.JAPTA.item.ItemRFMeter;
import net.reederhome.colin.mods.JAPTA.tileentity.*;

@Mod(name = "JAPTA", modid = JAPTA.MODID)
public class JAPTA {
    public static final String MODID = "japta";

    public static CreativeTabs tab = new CreativeTabs("japta") {
        @Override
        public Item getTabIconItem() {
            return JAPTA.batteryPotato;
        }
    };

    public static BlockCakeConverter cakeConverter;
    public static BlockFluxHopper fluxHopper;
    public static BlockChargingPlate chargingPlate;
    public static BlockElevatorShaft elevatorShaft;
    public static BlockElevatorTop elevatorTop;
    public static BlockFluxBlaster fluxBlaster;
    public static BlockItemBlaster itemBlaster;
    public static BlockRNGQuarry rngQuarry;
    public static BlockChestCharger chestCharger;
    public static BlockMover mover;
    public static BlockBonemealApplicator bonemealApplicator;
    public static BlockPowerCabinet powerCabinet;
    public static BlockPowerCabinetBase powerCabinetBase;
    public static BlockHeatConverter heatConverter;

    public static ItemRFMeter rfMeter;
    public static ItemBatteryPotato batteryPotato;
    public static ItemRFMeter diagnosticTool;

    private Configuration config;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent ev) {
        config = new Configuration(ev.getSuggestedConfigurationFile());
        config.load();
        config.addCustomCategoryComment("recipes", "Enable/Disable crafting of certain items");

        cakeConverter = new BlockCakeConverter();
        fluxHopper = new BlockFluxHopper();
        chargingPlate = new BlockChargingPlate();
        elevatorShaft = new BlockElevatorShaft();
        elevatorTop = new BlockElevatorTop();
        fluxBlaster = new BlockFluxBlaster();
        itemBlaster = new BlockItemBlaster();
        rngQuarry = new BlockRNGQuarry();
        chestCharger = new BlockChestCharger();
        mover = new BlockMover();
        bonemealApplicator = new BlockBonemealApplicator();
        powerCabinet = new BlockPowerCabinet();
        powerCabinetBase = new BlockPowerCabinetBase();
        heatConverter = new BlockHeatConverter();

        rfMeter = new ItemRFMeter(false);
        batteryPotato = new ItemBatteryPotato();
        diagnosticTool = new ItemRFMeter(true);

        GameRegistry.registerBlock(cakeConverter, "cakeConverter");
        GameRegistry.registerBlock(fluxHopper, "fluxHopper");
        GameRegistry.registerBlock(chargingPlate, "chargingPlate");
        GameRegistry.registerBlock(elevatorShaft, "elevatorShaft");
        GameRegistry.registerBlock(elevatorTop, "elevatorTop");
        GameRegistry.registerBlock(fluxBlaster, "fluxBlaster");
        GameRegistry.registerBlock(itemBlaster, "itemBlaster");
        GameRegistry.registerBlock(rngQuarry, "rngQuarry");
        GameRegistry.registerBlock(chestCharger, "chestCharger");
        GameRegistry.registerBlock(mover, "mover");
        GameRegistry.registerBlock(bonemealApplicator, "bonemealApplicator");
        GameRegistry.registerBlock(powerCabinetBase, "powerCabinetBase");
        GameRegistry.registerBlock(powerCabinet, ItemBlockPowerCabinet.class, "powerCabinet");
        GameRegistry.registerBlock(heatConverter, "heatConverter");

        GameRegistry.registerItem(rfMeter, "rfMeter");
        GameRegistry.registerItem(batteryPotato, "batteryPotato");
        GameRegistry.registerItem(diagnosticTool, "diagnosticTool");

        GameRegistry.registerTileEntity(TileEntityCakeConverter.class, "CakeConverter");
        GameRegistry.registerTileEntity(TileEntityFluxHopper.class, "FluxHopper");
        GameRegistry.registerTileEntity(TileEntityChargingPlate.class, "ChargingPlate");
        GameRegistry.registerTileEntity(TileEntityElevatorTop.class, "ElevatorTop");
        GameRegistry.registerTileEntity(TileEntityFluxBlaster.class, "FluxBlaster");
        GameRegistry.registerTileEntity(TileEntityItemBlaster.class, "ItemBlaster");
        GameRegistry.registerTileEntity(TileEntityRNGQuarry.class, "RNGQuarry");
        GameRegistry.registerTileEntity(TileEntityChestCharger.class, "ChestCharger");
        GameRegistry.registerTileEntity(TileEntityMover.class, "Mover");
        GameRegistry.registerTileEntity(TileEntityBonemealApplicator.class, "BonemealApplicator");
        GameRegistry.registerTileEntity(TileEntityPowerCabinetBase.class, "PowerCabinetBase");
        GameRegistry.registerTileEntity(TileEntityHeatConverter.class, "HeatConverter");

        addRecipe(new ShapedOreRecipe(cakeConverter, "frf", "rgr", "frf", 'f', Items.cake, 'r', "dustRedstone", 'g', "ingotGold"));
        addRecipe(new ShapedOreRecipe(fluxHopper, "i i", "iri", " i ", 'i', "ingotIron", 'r', "dustRedstone"));
        addRecipe(new ShapedOreRecipe(chargingPlate, "   ", "gpg", "oro", 'g', "dustGlowstone", 'p', Blocks.stone_pressure_plate, 'o', Blocks.obsidian, 'r', "dustRedstone"));
        addRecipe(new ShapedOreRecipe(new ItemStack(elevatorShaft, 4), "igi", "igi", "igi", 'i', "ingotIron", 'g', "blockGlass"));
        addRecipe(new ShapedOreRecipe(elevatorTop, "grg", "rer", "rsr", 'r', "dustRedstone", 'e', Items.ender_pearl, 's', elevatorShaft, 'g', "nuggetGold"));
        addRecipe(new ShapedOreRecipe(fluxBlaster, "grg", "rir", "grg", 'g', "nuggetGold", 'r', "dustRedstone", 'i', "ingotIron"));
        addRecipe(new ShapedOreRecipe(itemBlaster, "grg", "rcr", "grg", 'g', "nuggetGold", 'r', "dustRedstone", 'c', "chest"));
        addRecipe(new ShapedOreRecipe(rngQuarry, "s s", "iri", "PgS", 's', "stone", 'i', "ingotIron", 'r', "dustRedstone", 'P', Items.wooden_pickaxe, 'g', "ingotGold", 'S', Items.wooden_shovel));
        addRecipe(new ShapedOreRecipe(chestCharger, "rRr", "gcg", "oRo", 'r', "dustRedstone", 'R', "blockRedstone", 'g', "nuggetGold", 'c', "chest", 'o', Blocks.obsidian));
        addRecipe(new ShapedOreRecipe(new ItemStack(mover, 4), "rgr", "gpg", "rgr", 'r', "dustRedstone", 'g', "nuggetGold", 'p', Blocks.piston));
        addRecipe(new ShapedOreRecipe(bonemealApplicator, "gbg", "brb", "gbg", 'g', "nuggetGold", 'r', "dustRedstone", 'b', new ItemStack(Items.dye, 1, 15)));
        addRecipe(new ShapedOreRecipe(powerCabinet, " i ", "iri", " i ", 'i', "ingotIron", 'r', "blockRedstone"));
        addRecipe(new ShapedOreRecipe(powerCabinetBase, "lll", "gcg", 'l', "dyeBlue", 'g', "ingotGold", 'c', powerCabinet));
        addRecipe(new ShapedOreRecipe(heatConverter, "frf", "rgr", "frf", 'f', Blocks.furnace, 'r', "dustRedstone", 'g', "ingotGold"));

        addRecipe(new ShapedOreRecipe(rfMeter, "n", "d", 'n', "nuggetGold", 'd', "dustRedstone"));
        addRecipe(new ShapelessOreRecipe(new ItemStack(batteryPotato, 1, batteryPotato.getMaxDamage()), "cropPotato", "nuggetGold", "ingotIron", "dustRedstone"));
        addRecipe(new ShapelessOreRecipe(diagnosticTool, rfMeter, "dyeBlue"));

        BlockBlaster.RANGE = config.get("machines.blaster", "range", BlockBlaster.RANGE).getInt();
        TileEntityRNGQuarry.RANGE = config.get("machines.rngQuarry", "range", TileEntityRNGQuarry.RANGE).getInt();
        TileEntityBonemealApplicator.RANGE = config.get("machines.bonemealApplicator", "range", TileEntityBonemealApplicator.RANGE).getInt();
        TileEntityBonemealApplicator.USE = config.get("machines.bonemealApplicator", "cost", TileEntityBonemealApplicator.USE).getInt();
        TileEntityCakeConverter.RANGE = config.get("machines.cakeConverter", "range", TileEntityCakeConverter.RANGE).getInt();
        TileEntityCakeConverter.BITE_VALUE = config.get("machines.cakeConverter", "biteValue", TileEntityCakeConverter.BITE_VALUE).getInt();
        TileEntityElevatorTop.USE_BASE = config.get("machines.elevator", "baseCost", TileEntityElevatorTop.USE_BASE).getInt();
        TileEntityElevatorTop.USE_EXTRA = config.get("machines.elevator", "costPerBlock", TileEntityElevatorTop.USE_EXTRA).getInt();
        TileEntityMover.USE = config.get("machines.mover", "cost", TileEntityMover.USE).getInt();

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void clientInit(FMLInitializationEvent ev) {
        JAPTAClient.registerClientThings();

        if(config.get("misc", "Enable Version Checker", true, "").getBoolean()) {
            new Thread(new UpdateCheckThread(Loader.instance().activeModContainer().getVersion())).start();
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent ev) {
        config.save();
    }

    private boolean notified = false;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onTick(TickEvent ev) {
        if(!notified && UpdateCheckThread.ret != null) {
            if(UpdateCheckThread.ret.equals("update")) {
                EntityPlayer p = Minecraft.getMinecraft().thePlayer;
                if(p != null) {
                    p.addChatComponentMessage(new ChatComponentTranslation("text.japta.newversion"));
                    notified = true;
                }
            }
            else {
                notified = true;
            }
        }
    }

    public void addRecipe(IRecipe recipe) {
        addRecipe(recipe, recipe.getRecipeOutput());
    }

    public void addRecipe(IRecipe recipe, ItemStack stack) {
        addRecipe(recipe, stack.getDisplayName());
    }

    public void addRecipe(IRecipe recipe, String name) {
        addRecipe(recipe, name, true);
    }

    public void addRecipe(IRecipe recipe, String name, boolean defaultEnabled) {
        if(config.get("recipes", name, defaultEnabled, "").getBoolean()) {
            GameRegistry.addRecipe(recipe);
        }
    }

    public static<T extends Comparable<T>> T safeGetValue(IBlockState state, IProperty<T> prop) {
        T tr = null;
        try {
            tr = state.getValue(prop);
        } catch(Exception e) {
            e.printStackTrace();
        }
        if(tr == null) {
            // hopefully this won't happen, but it seems it does
            return prop.getAllowedValues().iterator().next();
        }
        else {
            return tr;
        }
    }
}
