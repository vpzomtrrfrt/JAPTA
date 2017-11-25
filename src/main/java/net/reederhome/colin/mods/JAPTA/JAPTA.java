package net.reederhome.colin.mods.JAPTA;

import mcjty.lib.tools.ItemStackTools;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
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
import net.minecraftforge.registries.IForgeRegistry;
import net.reederhome.colin.mods.JAPTA.block.*;
import net.reederhome.colin.mods.JAPTA.item.*;
import net.reederhome.colin.mods.JAPTA.tileentity.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod(name = "JAPTA", modid = JAPTA.MODID, dependencies = "before:guideapi")
public class JAPTA {
    public static final String MODID = "japta";

    public static CreativeTabs tab = new CreativeTabs("japta") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(JAPTA.batteryPotato);
        }
    };

    static final ResourceLocation CRAFTING_CATEGORY = new ResourceLocation(MODID, "crafting");

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
    public static SimpleTEBlock fisher;
    public static SimpleTEBlock sheepAdapter;
    public static BlockVoidStack voidStack;

    public static ItemRFMeter rfMeter;
    public static ItemBatteryPotato batteryPotato;
    public static ItemRFMeter diagnosticTool;
    public static Item coilReception;
    public static Item coilTransmission;
    public static ItemPoweredMultiTool poweredMultiTool;
    public static ItemBlockPowerCabinet itemPowerCabinet;
    public static ItemBlockPowerCabinet itemPowerCabinet2;
    public static ItemCapacitor capacitor;
    public static ItemLevitator levitator;

    @CapabilityInject(ITeslaHolder.class)
    public static Capability<ITeslaHolder> CAPABILITY_TESLA_HOLDER;
    @CapabilityInject(ITeslaProducer.class)
    public static Capability<ITeslaProducer> CAPABILITY_TESLA_PRODUCER;
    @CapabilityInject(ITeslaConsumer.class)
    public static Capability<ITeslaConsumer> CAPABILITY_TESLA_CONSUMER;
    @CapabilityInject(IEnergyStorage.class)
    public static Capability<IEnergyStorage> CAPABILITY_FORGE_ENERGY_STORAGE;

    private Configuration config;
    private static Map<Item, IRecipe> recipeMap = new HashMap<Item, IRecipe>();

    protected static ItemStack bookStack;

    @Mod.Instance
    private static JAPTA instance;

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
            int result = ItemStackTools.getStackSize(resultSlot) + ItemStackTools.getStackSize(itemstack);
            return result <= te.getInventoryStackLimit() && result <= resultSlot.getMaxStackSize();
        }
    }

    @SubscribeEvent
    public void onRegisterBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(cakeConverter,
                fluxHopper,
                chargingPlate,
                elevatorShaft,
                elevatorTop,
                fluxBlaster,
                itemBlaster,
                rngQuarry,
                chestCharger,
                mover,
                bonemealApplicator,
                powerCabinet,
                powerCabinet2,
                powerCabinetBase,
                heatConverter,
                furnaceBooster,
                machineBase,
                fluidHopper,
                fluidBlaster,
                fluxInhaler,
                fluidInhaler,
                itemInhaler,
                itemSplitter,
                eater,
                dungeonMaker,
                fisher,
                sheepAdapter,
                voidStack);
    }

    private void registerBlockItems(IForgeRegistry<Item> registry, Block... blocks) {
        for (Block block : blocks) {
            registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        }
    }

    @SubscribeEvent
    public void onRegisterItems(RegistryEvent.Register<Item> event) {
        registerBlockItems(event.getRegistry(),
                cakeConverter,
                fluxHopper,
                chargingPlate,
                elevatorShaft,
                elevatorTop,
                fluxBlaster,
                itemBlaster,
                rngQuarry,
                chestCharger,
                mover,
                bonemealApplicator,
                powerCabinetBase,
                heatConverter,
                furnaceBooster,
                machineBase,
                fluidHopper,
                fluidBlaster,
                fluxInhaler,
                fluidInhaler,
                itemInhaler,
                itemSplitter,
                eater,
                dungeonMaker,
                fisher,
                sheepAdapter,
                voidStack);

        event.getRegistry().register(rfMeter);
        event.getRegistry().register(batteryPotato);
        event.getRegistry().register(diagnosticTool);
        event.getRegistry().register(coilReception);
        event.getRegistry().register(coilTransmission);
        event.getRegistry().register(poweredMultiTool);
        event.getRegistry().register(itemPowerCabinet);
        event.getRegistry().register(itemPowerCabinet2);
        event.getRegistry().register(capacitor);
        event.getRegistry().register(levitator);
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
        fisher = new SimpleTEBlock(Material.ROCK, TileEntityFisher.class, "fisher");
        sheepAdapter = new SimpleTEBlock(Material.ROCK, TileEntitySheepAdapter.class, "sheepAdapter");
        voidStack = new BlockVoidStack();

        rfMeter = new ItemRFMeter(false);
        batteryPotato = new ItemBatteryPotato();
        diagnosticTool = new ItemRFMeter(true);
        coilReception = new Item().setUnlocalizedName("coilReception").setRegistryName("coilReception").setCreativeTab(tab);
        coilTransmission = new Item().setUnlocalizedName("coilTransmission").setRegistryName("coilTransmission").setCreativeTab(tab);
        poweredMultiTool = new ItemPoweredMultiTool();
        itemPowerCabinet = ((ItemBlockPowerCabinet) new ItemBlockPowerCabinet(powerCabinet).setRegistryName("powerCabinet"));
        itemPowerCabinet2 = ((ItemBlockPowerCabinet) new ItemBlockPowerCabinet(powerCabinet2).setRegistryName("powerCabinet2"));
        capacitor = new ItemCapacitor();
        levitator = new ItemLevitator();

        registerTileEntity(TileEntityCakeConverter.class, "CakeConverter");
        registerTileEntity(TileEntityFluxHopper.class, "FluxHopper");
        registerTileEntity(TileEntityChargingPlate.class, "ChargingPlate");
        registerTileEntity(TileEntityElevatorTop.class, "ElevatorTop");
        registerTileEntity(TileEntityFluxBlaster.class, "FluxBlaster");
        registerTileEntity(TileEntityItemBlaster.class, "ItemBlaster");
        registerTileEntity(TileEntityRNGQuarry.class, "RNGQuarry");
        registerTileEntity(TileEntityChestCharger.class, "ChestCharger");
        registerTileEntity(TileEntityMover.class, "Mover");
        registerTileEntity(TileEntityBonemealApplicator.class, "BonemealApplicator");
        registerTileEntity(TileEntityPowerCabinetBase.class, "PowerCabinetBase");
        registerTileEntity(TileEntityHeatConverter.class, "HeatConverter");
        registerTileEntity(TileEntityFurnaceBooster.class, "FurnaceBooster");
        registerTileEntity(TileEntityFluidHopper.class, "FluidHopper");
        registerTileEntity(TileEntityFluidBlaster.class, "FluidBlaster");
        registerTileEntity(TileEntityEater.class, "Eater");
        registerTileEntity(TileEntityDungeonMaker.class, "DungeonMaker");
        registerTileEntity(TileEntityFisher.class, "Fisher");
        registerTileEntity(TileEntitySheepAdapter.class, "SheepAdapter");
        registerTileEntity(TileEntityVoidStack.class, "VoidStack");

        /*RecipeSorter.register("poweredMultiTool", RecipePoweredMultiTool.class, RecipeSorter.Category.SHAPELESS, "");
        RecipeSorter.register("capacitor", RecipeCapacitorUpgrade.class, RecipeSorter.Category.SHAPELESS, "");*/

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
        TileEntitySheepAdapter.RANGE = config.get("machines.sheepAdapter", "range", TileEntitySheepAdapter.RANGE).getInt();
        TileEntitySheepAdapter.MAX_SHEEP_AMOUNT = config.get("machines.sheepAdapter", "held", TileEntitySheepAdapter.MAX_SHEEP_AMOUNT).getInt();
        ItemLevitator.USE = config.get("machines.levitator", "cost", ItemLevitator.USE).getInt();

        MinecraftForge.EVENT_BUS.register(this);

        if (ev.getSide() == Side.CLIENT) {
            JAPTAClient.preInit();
        }
    }

    @SubscribeEvent
    public void onRegisterRecipes(RegistryEvent.Register<IRecipe> event) {
        addRecipe(event, new ShapelessOreRecipe(CRAFTING_CATEGORY, rfMeter, "nuggetGold", "dustRedstone"));
        addRecipe(event, new ShapelessOreRecipe(CRAFTING_CATEGORY, new ItemStack(batteryPotato, 1, batteryPotato.getMaxDamage()), "cropPotato", "nuggetGold", coilReception));
        addRecipe(event, new ShapelessOreRecipe(CRAFTING_CATEGORY, diagnosticTool, rfMeter, "dyeBlue"));
        addRecipe(event, new ShapelessOreRecipe(CRAFTING_CATEGORY, fluxInhaler, fluxBlaster, Blocks.REDSTONE_TORCH));
        addRecipe(event, new ShapelessOreRecipe(CRAFTING_CATEGORY, fluidInhaler, fluidBlaster, Blocks.REDSTONE_TORCH));
        addRecipe(event, new ShapelessOreRecipe(CRAFTING_CATEGORY, itemInhaler, itemBlaster, Blocks.REDSTONE_TORCH));
        addRecipe(event, new ShapelessOreRecipe(CRAFTING_CATEGORY, itemSplitter, itemBlaster, "plankWood"));
        addRecipe(event, new ShapelessOreRecipe(CRAFTING_CATEGORY, new ItemStack(capacitor, 1, capacitor.getMaxDamage(null)), powerCabinetBase, Items.BOW));
        addRecipe(event, new ShapedOreRecipe(CRAFTING_CATEGORY, new ItemStack(levitator, 1, levitator.getMaxDamage(null)), " r ", " m ", "g g", 'r', coilReception, 'm', machineBase, 'g', "dustGlowstone"));
        addRecipe(event, new ShapedOreRecipe(CRAFTING_CATEGORY, cakeConverter, "frf", "gmg", "ftf", 'f', Items.CAKE, 'r', coilReception, 'g', "nuggetGold", 'm', machineBase, 't', coilTransmission));
        addRecipe(event, new ShapedOreRecipe(CRAFTING_CATEGORY, fluxHopper, "i i", "iri", " i ", 'i', "ingotIron", 'r', "dustRedstone"));
        addRecipe(event, new ShapedOreRecipe(CRAFTING_CATEGORY, chargingPlate, "   ", "gpg", "oto", 'g', "dustGlowstone", 'p', Blocks.STONE_PRESSURE_PLATE, 'o', Blocks.OBSIDIAN, 't', coilTransmission));
        addRecipe(event, new ShapedOreRecipe(CRAFTING_CATEGORY, new ItemStack(elevatorShaft, 4), "igi", "igi", "igi", 'i', "ingotIron", 'g', "blockGlass"));
        addRecipe(event, new ShapedOreRecipe(CRAFTING_CATEGORY, elevatorTop, "gcg", "geg", "rsr", 'g', "nuggetGold", 'c', coilReception, 'e', Items.ENDER_PEARL, 's', elevatorShaft, 'r', "dustRedstone"));
        addRecipe(event, new ShapedOreRecipe(CRAFTING_CATEGORY, fluxBlaster, "gdg", "rmt", "gug", 'g', "nuggetGold", 'd', Blocks.DISPENSER, 'r', coilReception, 'm', machineBase, 't', coilTransmission, 'u', "dustRedstone"));
        addRecipe(event, new ShapedOreRecipe(CRAFTING_CATEGORY, itemBlaster, "gdg", "cmc", "gug", 'g', "nuggetGold", 'd', Blocks.DISPENSER, 'c', "chest", 'm', machineBase, 'u', "dustRedstone"));
        addRecipe(event, new ShapedOreRecipe(CRAFTING_CATEGORY, fluidBlaster, "gdg", "bmb", "gug", 'g', "nuggetGold", 'd', Blocks.DISPENSER, 'b', Items.BUCKET, 'm', machineBase, 'u', "dustRedstone"));
        addRecipe(event, new ShapedOreRecipe(CRAFTING_CATEGORY, rngQuarry, "srs", "imi", "PgS", 's', "stone", 'i', "ingotIron", 'm', machineBase, 'g', "nuggetGold", 'P', Items.WOODEN_PICKAXE, 'S', Items.WOODEN_SHOVEL, 'r', coilReception));
        addRecipe(event, new ShapedOreRecipe(CRAFTING_CATEGORY, chestCharger, "rhr", "cmt", "rhr", 'r', "dustRedstone", 'h', "chest", 'c', coilReception, 'm', machineBase, 't', coilTransmission));
        addRecipe(event, new ShapedOreRecipe(CRAFTING_CATEGORY, new ItemStack(mover, 4), "rpr", "gmg", "rcr", 'r', "dustRedstone", 'p', Blocks.PISTON, 'm', machineBase, 'g', "nuggetGold", 'c', coilReception));
        addRecipe(event, new ShapedOreRecipe(CRAFTING_CATEGORY, bonemealApplicator, "gbg", "bmb", "gcg", 'g', "nuggetGold", 'b', new ItemStack(Items.DYE, 1, 15), 'm', machineBase, 'c', coilReception));
        addRecipe(event, new ShapedOreRecipe(CRAFTING_CATEGORY, powerCabinet, " i ", "iri", " i ", 'i', "ingotIron", 'r', "blockRedstone"));
        addRecipe(event, new ShapedOreRecipe(CRAFTING_CATEGORY, powerCabinetBase, "lrl", "gcg", 'l', "dyeBlue", 'r', coilReception, 'g', "nuggetGold", 'c', powerCabinet));
        addRecipe(event, new ShapedOreRecipe(CRAFTING_CATEGORY, heatConverter, "frf", "gmg", "ftf", 'f', Blocks.FURNACE, 'r', coilReception, 'g', "nuggetGold", 't', coilTransmission, 'm', machineBase));
        addRecipe(event, new ShapedOreRecipe(CRAFTING_CATEGORY, furnaceBooster, " r ", "rfr", " r ", 'r', "dustRedstone", 'f', Blocks.FURNACE));
        addRecipe(event, new ShapedOreRecipe(CRAFTING_CATEGORY, new ItemStack(machineBase, 2), "gig", "iri", "gig", 'g', "nuggetGold", 'i', "ingotIron", 'r', "dustRedstone"));
        addRecipe(event, new ShapedOreRecipe(CRAFTING_CATEGORY, coilReception, "rg ", " i ", " gr", 'r', "dustRedstone", 'i', "ingotIron", 'g', "nuggetGold"));
        addRecipe(event, new ShapedOreRecipe(CRAFTING_CATEGORY, coilTransmission, "r  ", "gig", "  r", 'r', "dustRedstone", 'i', "ingotIron", 'g', "nuggetGold"));
        addRecipe(event, new ShapedOreRecipe(CRAFTING_CATEGORY, fluidHopper, "i i", "i i", " b ", 'i', "ingotIron", 'b', Items.BUCKET));
        addRecipe(event, new ShapedOreRecipe(CRAFTING_CATEGORY, eater, "iii", "imi", "rcr", 'i', "ingotIron", 'm', machineBase, 'r', "dustRedstone", 'c', coilTransmission));
        addRecipe(event, new ShapedOreRecipe(CRAFTING_CATEGORY, dungeonMaker, "imi", "mbm", "ici", 'i', "ingotIron", 'b', machineBase, 'm', Blocks.MOSSY_COBBLESTONE, 'c', coilReception));
        addRecipe(event, new ShapedOreRecipe(CRAFTING_CATEGORY, fisher, "ifi", "rmr", "ici", 'i', "ingotIron", 'f', Items.FISHING_ROD, 'r', "dustRedstone", 'c', coilReception, 'm', machineBase));
        addRecipe(event, new ShapedOreRecipe(CRAFTING_CATEGORY, sheepAdapter, "iti", "wmw", "iri", 'i', "ingotIron", 't', coilTransmission, 'w', Blocks.WOOL, 'm', machineBase, 'r', coilReception));
        addRecipe(event, new ShapedOreRecipe(CRAFTING_CATEGORY, voidStack, "coc", "oio", "coc", 'c', "chest", 'o', Blocks.OBSIDIAN, 'i', "ingotIron"));
        addRecipe(event, new RecipePoweredMultiTool());

        if(bookStack != null) {
            addRecipe(event, new ShapelessOreRecipe(JAPTA.CRAFTING_CATEGORY, bookStack, "paper", "ingotIron", "dustRedstone", "dustRedstone"));
        }

        event.getRegistry().register(new RecipeCapacitorUpgrade());

        GameRegistry.addSmelting(powerCabinet, new ItemStack(powerCabinet2), 0);

        saveConfig();
    }

    private static void registerTileEntity(Class<? extends TileEntity> clazz, String name) {
        GameRegistry.registerTileEntity(clazz, name);
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void clientInit(FMLInitializationEvent ev) {
        JAPTAClient.init();

        if (config.get("misc", "Enable Version Checker", true, "").getBoolean()) {
            new Thread(new UpdateCheckThread(Loader.instance().activeModContainer().getVersion())).start();
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent ev) {
        saveConfig();
    }

    public static void saveConfig() {
        instance.config.save();
    }

    private boolean notified = false;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onTick(TickEvent ev) {
        if (!notified && UpdateCheckThread.ret != null) {
            if (UpdateCheckThread.ret.equals("update")) {
                EntityPlayer p = Minecraft.getMinecraft().player;
                if (p != null) {
                    p.sendMessage(new TextComponentTranslation("text.japta.newversion", false));
                    notified = true;
                }
            } else {
                notified = true;
            }
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if(event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) event.getEntity());
            List<ItemStack> capacitors = new ArrayList<ItemStack>();
            for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if(stack != null && stack.getItem() == capacitor) {
                    capacitors.add(stack);
                }
                else if(stack != null && stack.getItem() instanceof ITickableItem) {
                    ((ITickableItem) stack.getItem()).update(stack, player);
                }
            }
            if(capacitors.size() > 0) {
                for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
                    ItemStack stack = player.inventory.getStackInSlot(i);
                    if(stack != null && stack.getItem() != capacitor) {
                        for(ItemStack curCap : capacitors) {
                            int startValue = capacitor.getEnergyStored(curCap);
                            if(startValue > 0) {
                                capacitor.setEnergyStored(curCap, startValue-TileEntityJPT.chargeItem(stack, startValue));
                            }
                        }
                    }
                }
            }
        }
        else if(event.getEntity() instanceof EntitySheep) {
            TileEntitySheepAdapter.equalize(((EntitySheep) event.getEntity()));
        }
    }

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        ItemStack stack = event.getItemStack();
        if(stack.getItem() instanceof ItemRFMeter) {
            ((ItemRFMeter) stack.getItem()).onEntityInteract(event.getTarget(), event.getEntityPlayer());
        }
    }

    public static void addRecipe(RegistryEvent.Register<IRecipe> event, IRecipe recipe) {
        addRecipe(event, recipe, recipe.getRecipeOutput());
    }

    public static void addRecipe(RegistryEvent.Register<IRecipe> event, IRecipe recipe, ItemStack stack) {
        addRecipe(event, recipe, stack.getItem(), stack.getDisplayName());
    }

    public static void addRecipe(RegistryEvent.Register<IRecipe> event, IRecipe recipe, Item item, String name) {
        addRecipe(event, recipe, item, name, true);
    }

    public static void addRecipe(RegistryEvent.Register<IRecipe> event, IRecipe recipe, Item item, String name, boolean defaultEnabled) {
        recipeMap.put(item, recipe);
        if (instance.config.get("recipes", name, defaultEnabled, "").getBoolean()) {
            recipe.setRegistryName(new ResourceLocation(MODID, "crafting_"+name));
            event.getRegistry().register(recipe);
        }
    }

    /**
     * Get the recipe for a JAPTA item
     *
     * @param item the item
     * @return the recipe for that item
     */
    public static IRecipe getRecipe(Item item) {
        return recipeMap.get(item);
    }

    public static IRecipe getRecipe(Block block) {
        return getRecipe(Item.getItemFromBlock(block));
    }

    public static <T extends Comparable<T>> T safeGetValue(IBlockState state, IProperty<T> prop) {
        T tr = null;
        try {
            tr = state.getValue(prop);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tr == null) {
            // hopefully this won't happen, but it seems it does
            return prop.getAllowedValues().iterator().next();
        } else {
            return tr;
        }
    }

    public static <T extends Comparable<T>> T safeGetValue(TileEntity te, IProperty<T> prop) {
        return safeGetValue(te.getWorld().getBlockState(te.getPos()), prop);
    }
}
