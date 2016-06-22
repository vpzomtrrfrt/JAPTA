package net.reederhome.colin.mods.JAPTA;

import amerifrance.guideapi.api.GuideAPI;
import amerifrance.guideapi.api.impl.Book;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.text.TextComponentTranslation;
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
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.reederhome.colin.mods.JAPTA.block.*;
import net.reederhome.colin.mods.JAPTA.item.ItemBatteryPotato;
import net.reederhome.colin.mods.JAPTA.item.ItemBlockPowerCabinet;
import net.reederhome.colin.mods.JAPTA.item.ItemPoweredMultiTool;
import net.reederhome.colin.mods.JAPTA.item.ItemRFMeter;
import net.reederhome.colin.mods.JAPTA.tileentity.*;

import java.util.HashMap;
import java.util.Map;

@Mod(name = "JAPTA", modid = JAPTA.MODID, dependencies = "after:guideapi")
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
    public static SimpleTEBlock chestCharger;
    public static BlockMover mover;
    public static SimpleTEBlock bonemealApplicator;
    public static BlockPowerCabinet powerCabinet;
    public static BlockPowerCabinet powerCabinet2;
    public static SimpleTEBlock powerCabinetBase;
    public static BlockHeatConverter heatConverter;
    public static SimpleTEBlock furnaceBooster;
    public static Block machineBase;
    public static BlockFluidHopper fluidHopper;
    public static BlockFluidBlaster fluidBlaster;
    public static BlockFluxBlaster fluxInhaler;
    public static BlockFluidBlaster fluidInhaler;
    public static BlockItemBlaster itemInhaler;
    public static BlockItemBlaster itemSplitter;
    public static BlockEater eater;
    public static SimpleTEBlock dungeonMaker;

    public static ItemRFMeter rfMeter;
    public static ItemBatteryPotato batteryPotato;
    public static ItemRFMeter diagnosticTool;
    public static Item coilReception;
    public static Item coilTransmission;
    public static ItemPoweredMultiTool poweredMultiTool;
    public static ItemBlockPowerCabinet itemPowerCabinet;
    public static ItemBlockPowerCabinet itemPowerCabinet2;

    private Configuration config;
    private static Map<Item,IRecipe> recipeMap = new HashMap<Item, IRecipe>();

    public static boolean canSmelt(TileEntityFurnace te) {
        // took this from decompiled forge
        if (te.getStackInSlot(0) == null) {
            return false;
        } else {
            ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(te.getStackInSlot(0));
            if (itemstack == null) return false;
            ItemStack resultSlot = te.getStackInSlot(2);
            if (resultSlot == null) return true;
            if (!resultSlot.isItemEqual(itemstack)) return false;
            int result = resultSlot.stackSize + itemstack.stackSize;
            return result <= te.getInventoryStackLimit() && result <= resultSlot.getMaxStackSize();
        }
    }

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
        fluxBlaster = new BlockFluxBlaster(false);
        itemBlaster = new BlockItemBlaster(false, false);
        rngQuarry = new BlockRNGQuarry();
        chestCharger = new SimpleTEBlock(Material.ROCK, TileEntityChestCharger.class, "chestCharger");
        mover = new BlockMover();
        bonemealApplicator = new SimpleTEBlock(Material.ROCK, TileEntityBonemealApplicator.class, "bonemealApplicator");
        powerCabinet = (BlockPowerCabinet) new BlockPowerCabinet(config.get("machines.powerCabinet", "basicMetaValue", 1000, "RF per line on texture (1/15 of block) for power cabinet").getInt()).setUnlocalizedName("powerCabinet").setRegistryName("powerCabinet");
        powerCabinet2 = (BlockPowerCabinet) new BlockPowerCabinet(config.get("machines.powerCabinet", "firedMetaValue", 2000, "RF per line on texture (1/15 of block) for scorched power cabinet").getInt()).setUnlocalizedName("powerCabinet2").setRegistryName("powerCabinet2");
        powerCabinetBase = new SimpleTEBlock(Material.ROCK, TileEntityPowerCabinetBase.class, "powerCabinetBase");
        heatConverter = new BlockHeatConverter();
        furnaceBooster = new SimpleTEBlock(Material.ROCK, TileEntityFurnaceBooster.class, "furnaceBooster");
        machineBase = new Block(Material.ROCK).setHardness(1).setUnlocalizedName("machineBase").setRegistryName("machineBase").setCreativeTab(tab);
        fluidHopper = new BlockFluidHopper();
        fluidBlaster = new BlockFluidBlaster(false);
        fluxInhaler = new BlockFluxBlaster(true);
        fluidInhaler = new BlockFluidBlaster(true);
        itemInhaler = new BlockItemBlaster(true, false);
        itemSplitter = new BlockItemBlaster(false, true);
        eater = new BlockEater();
        dungeonMaker = new SimpleTEBlock(Material.ROCK, TileEntityDungeonMaker.class, "dungeonMaker");

        rfMeter = new ItemRFMeter(false);
        batteryPotato = new ItemBatteryPotato();
        diagnosticTool = new ItemRFMeter(true);
        coilReception = new Item().setUnlocalizedName("coilReception").setRegistryName("coilReception").setCreativeTab(tab);
        coilTransmission = new Item().setUnlocalizedName("coilTransmission").setRegistryName("coilTransmission").setCreativeTab(tab);
        poweredMultiTool = new ItemPoweredMultiTool();
        itemPowerCabinet = ((ItemBlockPowerCabinet) new ItemBlockPowerCabinet(powerCabinet).setRegistryName("powerCabinet"));
        itemPowerCabinet2 = ((ItemBlockPowerCabinet) new ItemBlockPowerCabinet(powerCabinet2).setRegistryName("powerCabinet2"));

        registerBlock(cakeConverter);
        registerBlock(fluxHopper);
        registerBlock(chargingPlate);
        registerBlock(elevatorShaft);
        registerBlock(elevatorTop);
        registerBlock(fluxBlaster);
        registerBlock(itemBlaster);
        registerBlock(rngQuarry);
        registerBlock(chestCharger);
        registerBlock(mover);
        registerBlock(bonemealApplicator);
        GameRegistry.register(powerCabinet);
        GameRegistry.register(powerCabinet2);
        registerBlock(powerCabinetBase);
        registerBlock(heatConverter);
        registerBlock(furnaceBooster);
        registerBlock(machineBase);
        registerBlock(fluidHopper);
        registerBlock(fluidBlaster);
        registerBlock(fluxInhaler);
        registerBlock(fluidInhaler);
        registerBlock(itemInhaler);
        registerBlock(itemSplitter);
        registerBlock(eater);
        registerBlock(dungeonMaker);

        GameRegistry.register(rfMeter);
        GameRegistry.register(batteryPotato);
        GameRegistry.register(diagnosticTool);
        GameRegistry.register(coilReception);
        GameRegistry.register(coilTransmission);
        GameRegistry.register(poweredMultiTool);
        GameRegistry.register(itemPowerCabinet);
        GameRegistry.register(itemPowerCabinet2);

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
        GameRegistry.registerTileEntity(TileEntityFurnaceBooster.class, "FurnaceBooster");
        GameRegistry.registerTileEntity(TileEntityFluidHopper.class, "FluidHopper");
        GameRegistry.registerTileEntity(TileEntityFluidBlaster.class, "FluidBlaster");
        GameRegistry.registerTileEntity(TileEntityEater.class, "Eater");
        GameRegistry.registerTileEntity(TileEntityDungeonMaker.class, "DungeonMaker");

        RecipeSorter.register("poweredMultiTool", RecipePoweredMultiTool.class, RecipeSorter.Category.SHAPELESS, "");

        addRecipe(new ShapelessOreRecipe(rfMeter, "nuggetGold", "dustRedstone"));
        addRecipe(new ShapelessOreRecipe(new ItemStack(batteryPotato, 1, batteryPotato.getMaxDamage()), "cropPotato", "nuggetGold", coilReception));
        addRecipe(new ShapelessOreRecipe(diagnosticTool, rfMeter, "dyeBlue"));
        addRecipe(new ShapelessOreRecipe(fluxInhaler, fluxBlaster, Blocks.REDSTONE_TORCH));
        addRecipe(new ShapelessOreRecipe(fluidInhaler, fluidBlaster, Blocks.REDSTONE_TORCH));
        addRecipe(new ShapelessOreRecipe(itemInhaler, itemBlaster, Blocks.REDSTONE_TORCH));
        addRecipe(new ShapelessOreRecipe(itemSplitter, itemBlaster, "plankWood"));

        addRecipe(new ShapedOreRecipe(cakeConverter, "frf", "gmg", "ftf", 'f', Items.CAKE, 'r', coilReception, 'g', "nuggetGold", 'm', machineBase, 't', coilTransmission));
        addRecipe(new ShapedOreRecipe(fluxHopper, "i i", "iri", " i ", 'i', "ingotIron", 'r', "dustRedstone"));
        addRecipe(new ShapedOreRecipe(chargingPlate, "   ", "gpg", "oto", 'g', "dustGlowstone", 'p', Blocks.STONE_PRESSURE_PLATE, 'o', Blocks.OBSIDIAN, 't', coilTransmission));
        addRecipe(new ShapedOreRecipe(new ItemStack(elevatorShaft, 4), "igi", "igi", "igi", 'i', "ingotIron", 'g', "blockGlass"));
        addRecipe(new ShapedOreRecipe(elevatorTop, "gcg", "geg", "rsr", 'g', "nuggetGold", 'c', coilReception, 'e', Items.ENDER_PEARL, 's', elevatorShaft, 'r', "dustRedstone"));
        addRecipe(new ShapedOreRecipe(fluxBlaster, "gdg", "rmt", "gdg", 'g', "nuggetGold", 'd', Blocks.DISPENSER, 'r', coilReception, 'm', machineBase, 't', coilTransmission));
        addRecipe(new ShapedOreRecipe(itemBlaster, "gdg", "cmc", "gdg", 'g', "nuggetGold", 'd', Blocks.DISPENSER, 'c', "chest", 'm', machineBase));
        addRecipe(new ShapedOreRecipe(fluidBlaster, "gdg", "bmb", "gdg", 'g', "nuggetGold", 'd', Blocks.DISPENSER, 'b', Items.BUCKET, 'm', machineBase));
        addRecipe(new ShapedOreRecipe(rngQuarry, "srs", "imi", "PgS", 's', "stone", 'i', "ingotIron", 'm', machineBase, 'g', "nuggetGold", 'P', Items.WOODEN_PICKAXE, 'S', Items.WOODEN_SHOVEL, 'r', coilReception));
        addRecipe(new ShapedOreRecipe(chestCharger, "rhr", "cmt", "rhr", 'r', "dustRedstone", 'h', "chest", 'c', coilReception, 'm', machineBase, 't', coilTransmission));
        addRecipe(new ShapedOreRecipe(new ItemStack(mover, 4), "rpr", "gmg", "rcr", 'r', "dustRedstone", 'p', Blocks.PISTON, 'm', machineBase, 'g', "nuggetGold", 'c', coilReception));
        addRecipe(new ShapedOreRecipe(bonemealApplicator, "gbg", "bmb", "gcg", 'g', "nuggetGold", 'b', new ItemStack(Items.DYE, 1, 15), 'm', machineBase, 'c', coilReception));
        addRecipe(new ShapedOreRecipe(powerCabinet, " i ", "iri", " i ", 'i', "ingotIron", 'r', "blockRedstone"));
        addRecipe(new ShapedOreRecipe(powerCabinetBase, "lrl", "gcg", 'l', "dyeBlue", 'r', coilReception, 'g', "nuggetGold", 'c', powerCabinet));
        addRecipe(new ShapedOreRecipe(heatConverter, "frf", "gmg", "ftf", 'f', Blocks.FURNACE, 'r', coilReception, 'g', "nuggetGold", 't', coilTransmission, 'm', machineBase));
        addRecipe(new ShapedOreRecipe(furnaceBooster, " r ", "rfr", " r ", 'r', "dustRedstone", 'f', Blocks.FURNACE));
        addRecipe(new ShapedOreRecipe(machineBase, "gig", "iri", "gig", 'g', "nuggetGold", 'i', "ingotIron", 'r', "dustRedstone"));
        addRecipe(new ShapedOreRecipe(coilReception, "rg ", " i ", " gr", 'r', "dustRedstone", 'i', "ingotIron", 'g', "nuggetGold"));
        addRecipe(new ShapedOreRecipe(coilTransmission, "r  ", "gig", "  r", 'r', "dustRedstone", 'i', "ingotIron", 'g', "nuggetGold"));
        addRecipe(new ShapedOreRecipe(fluidHopper, "i i", "i i", " b ", 'i', "ingotIron", 'b', Items.BUCKET));
        addRecipe(new ShapedOreRecipe(eater, "iii", "imi", "rcr", 'i', "ingotIron", 'm', machineBase, 'r', "dustRedstone", 'c', coilTransmission));
        addRecipe(new ShapedOreRecipe(dungeonMaker, "imi", "mbm", "imi", 'i', "ingotIron", 'b', machineBase, 'm', Blocks.MOSSY_COBBLESTONE));
        addRecipe(new RecipePoweredMultiTool());

        GameRegistry.addSmelting(powerCabinet, new ItemStack(powerCabinet2), 0);

        if(Loader.isModLoaded("guideapi")) {
            System.out.println("Guide-API detected, adding book");
            Book book = GuideJAPTA.get().getBook();
            GameRegistry.register(book);
            addRecipe(new ShapelessOreRecipe(GuideAPI.getStackFromBook(book), "paper", "ingotIron", "dustRedstone", "dustRedstone"));
        }

        BlockBlaster.RANGE = config.get("machines.blaster", "range", BlockBlaster.RANGE).getInt();
        TileEntityRNGQuarry.RANGE = config.get("machines.rngQuarry", "range", TileEntityRNGQuarry.RANGE).getInt();
        TileEntityBonemealApplicator.RANGE = config.get("machines.bonemealApplicator", "range", TileEntityBonemealApplicator.RANGE).getInt();
        TileEntityBonemealApplicator.USE = config.get("machines.bonemealApplicator", "cost", TileEntityBonemealApplicator.USE).getInt();
        TileEntityCakeConverter.RANGE = config.get("machines.cakeConverter", "range", TileEntityCakeConverter.RANGE).getInt();
        TileEntityCakeConverter.BITE_VALUE = config.get("machines.cakeConverter", "biteValue", TileEntityCakeConverter.BITE_VALUE).getInt();
        TileEntityElevatorTop.USE_BASE = config.get("machines.elevator", "baseCost", TileEntityElevatorTop.USE_BASE).getInt();
        TileEntityElevatorTop.USE_EXTRA = config.get("machines.elevator", "costPerBlock", TileEntityElevatorTop.USE_EXTRA).getInt();
        TileEntityMover.USE = config.get("machines.mover", "cost", TileEntityMover.USE).getInt();
        TileEntityHeatConverter.USE = config.get("machines.heatConverter", "cost", TileEntityHeatConverter.USE, "RF/t generated and used by Heat Converters").getInt();
        TileEntityEater.MULTIPLIER = config.get("machines.eater", "multiplier", TileEntityEater.MULTIPLIER).getInt();
        TileEntityEater.TIME = config.get("machines.eater", "time", TileEntityEater.TIME, "Ticks taken to eat food").getInt();
        TileEntityDungeonMaker.USE = config.get("machines.dungeonifier", "cost", TileEntityDungeonMaker.USE).getInt();

        MinecraftForge.EVENT_BUS.register(this);

        if(ev.getSide() == Side.CLIENT) {
            JAPTAClient.preInit();
        }
    }

    private void registerBlock(Block block) {
        GameRegistry.register(block);
        GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void clientInit(FMLInitializationEvent ev) {
        JAPTAClient.init();

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
                    p.addChatComponentMessage(new TextComponentTranslation("text.japta.newversion"));
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
        addRecipe(recipe, stack.getItem(), stack.getDisplayName());
    }

    public void addRecipe(IRecipe recipe, Item item, String name) {
        addRecipe(recipe, item, name, true);
    }

    public void addRecipe(IRecipe recipe, Item item, String name, boolean defaultEnabled) {
        recipeMap.put(item, recipe);
        if(config.get("recipes", name, defaultEnabled, "").getBoolean()) {
            GameRegistry.addRecipe(recipe);
        }
    }

    /**
     * Get the recipe for a JAPTA item
     * @param item the item
     * @return the recipe for that item
     */
    public static IRecipe getRecipe(Item item) {
        return recipeMap.get(item);
    }

    public static IRecipe getRecipe(Block block) {
        return getRecipe(Item.getItemFromBlock(block));
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
