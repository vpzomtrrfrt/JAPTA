package net.reederhome.colin.mods.JAPTA;

import amerifrance.guideapi.api.GuideAPI;
import amerifrance.guideapi.api.GuideBook;
import amerifrance.guideapi.api.IGuideBook;
import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.entry.EntryItemStack;
import amerifrance.guideapi.page.PageFurnaceRecipe;
import amerifrance.guideapi.page.PageIRecipe;
import amerifrance.guideapi.page.PageItemStack;
import amerifrance.guideapi.page.PageText;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.reederhome.colin.mods.JAPTA.block.BlockBlaster;
import net.reederhome.colin.mods.JAPTA.block.BlockPowerCabinet;
import net.reederhome.colin.mods.JAPTA.item.ItemLevitator;
import net.reederhome.colin.mods.JAPTA.tileentity.*;

import java.awt.*;
import java.util.*;
import java.util.List;

@GuideBook
public class GuideJAPTA implements IGuideBook {
    private static GuideJAPTA instance;
    private Book book;

    public Book buildBook() {
        book = new Book();
        List<CategoryAbstract> categories = new ArrayList<CategoryAbstract>();
        Map<ResourceLocation, EntryAbstract> blockMap = new HashMap<ResourceLocation, EntryAbstract>();
        blockMap.put(new ResourceLocation("blocks", "blasters"), new EntryItemStack(Arrays.asList(
                new PageText("Blasters can provide a resource to the " + BlockBlaster.RANGE + " blocks in front of them." +
                        "  The nearest block takes priority.  When placed, it will face away from you." +
                        "  If you are sneaking, it will face toward you instead." +
                        "  It will also send resources up Elevator Shafts, which is especially useful with Flux Blasters and Elevators."),
                maybeRecipe(JAPTA.fluxBlaster),
                maybeRecipe(JAPTA.itemBlaster),
                maybeRecipe(JAPTA.fluidBlaster)
        ), "guide.title.blasters", new ItemStack(JAPTA.fluxBlaster)));
        blockMap.put(new ResourceLocation("blocks", "bonemealApplicator"), new EntryItemStack(Arrays.asList(
                new PageItemStack("The Bonemeal Applicator takes Bone Meal from adjacent inventory blocks, and uses RF to apply it to random plants in a " + TileEntityBonemealApplicator.RANGE + "-block radius.", JAPTA.bonemealApplicator),
                maybeRecipe(JAPTA.bonemealApplicator)
        ), "tile.bonemealApplicator.name", new ItemStack(JAPTA.bonemealApplicator)));
        blockMap.put(new ResourceLocation("blocks", "cakeConverter"), new EntryItemStack(Arrays.asList(
                new PageItemStack("The Cake Converter can convert between Cake and RF in a radius of " + TileEntityCakeConverter.RANGE + " blocks around it.", JAPTA.cakeConverter),
                maybeRecipe(JAPTA.cakeConverter)
        ), "tile.converterCake.name", new ItemStack(JAPTA.cakeConverter)));
        blockMap.put(new ResourceLocation("blocks", "chargingPlate"), new EntryItemStack(Arrays.asList(
                new PageItemStack("The Charging Plate is a pressure plate that only detects players.  If given RF, it will also charge items in player's inventories standing on it.", JAPTA.chargingPlate),
                maybeRecipe(JAPTA.chargingPlate)
        ), "tile.chargingPlate.name", new ItemStack(JAPTA.chargingPlate)));
        blockMap.put(new ResourceLocation("blocks", "chestCharger"), new EntryItemStack(Arrays.asList(
                new PageItemStack("The Chest Charger can charge items with RF in adjacent block inventories.", JAPTA.chestCharger),
                maybeRecipe(JAPTA.chestCharger)
        ), "tile.chestCharger.name", new ItemStack(JAPTA.chestCharger)));
        blockMap.put(new ResourceLocation("blocks", "dungeonMaker"), new EntryItemStack(Arrays.asList(
                new PageItemStack("The Dungeonifier is a block that can use RF to make adjacent empty Chests into dungeon chests.", JAPTA.dungeonMaker),
                maybeRecipe(JAPTA.dungeonMaker)
        ), "tile.dungeonMaker.name", new ItemStack(JAPTA.dungeonMaker)));
        blockMap.put(new ResourceLocation("blocks", "eater"), new EntryItemStack(Arrays.asList(
                new PageItemStack("The Eater can eat food items and generate RF.", JAPTA.eater),
                maybeRecipe(JAPTA.eater)
        ), "tile.eater.name", new ItemStack(JAPTA.eater)));
        blockMap.put(new ResourceLocation("blocks", "elevator"), new EntryItemStack(Arrays.asList(
                new PageItemStack("The Elevator is a multiblock structure consisting of an Elevator Top above Elevator Shafts.", JAPTA.elevatorShaft),
                new PageItemStack("If RF is given to the top, players jumping from the bottom will be teleported to the top," +
                        " and players sneaking from the top will be teleported to the bottom.", JAPTA.elevatorTop),
                maybeRecipe(JAPTA.elevatorShaft),
                maybeRecipe(JAPTA.elevatorTop)
        ), "guide.title.elevator", new ItemStack(JAPTA.elevatorShaft)));
        blockMap.put(new ResourceLocation("blocks", "fisher"), new EntryItemStack(Arrays.asList(
                new PageItemStack("The Fisher is a block that is able to turn RF into fishing loot." +
                        "  Items gained will be placed in adjacent chests, or spewed if there is no available slot.", JAPTA.fisher),
                maybeRecipe(JAPTA.fisher)
        ), "tile.fisher.name", new ItemStack(JAPTA.fisher)));
        blockMap.put(new ResourceLocation("blocks", "fluidHopper"), new EntryItemStack(Arrays.asList(
                new PageItemStack("The Fluid Hopper is a block that can transfer liquids.  It will drain liquids from the top, and give them to the front." +
                        "  It faces toward the block you placed it on.  It can only hold one liquid at a time.", JAPTA.fluidHopper),
                maybeRecipe(JAPTA.fluidHopper)
        ), "tile.fluidHopper.name", new ItemStack(JAPTA.fluidHopper)));
        blockMap.put(new ResourceLocation("blocks", "fluxHopper"), new EntryItemStack(Arrays.asList(
                new PageItemStack("The Flux Hopper is a block that can transfer RF.  It will drain RF from the top, and give it to the front." +
                        "  It faces toward the block you placed it on.", JAPTA.fluxHopper),
                maybeRecipe(JAPTA.fluxHopper)
        ), "tile.fluxHopper.name", new ItemStack(JAPTA.fluxHopper)));
        blockMap.put(new ResourceLocation("blocks", "furnaceBooster"), new EntryItemStack(Arrays.asList(
                new PageItemStack("The Furnace Booster is a block that can be placed next to vanilla Furnaces to make them faster.", JAPTA.furnaceBooster),
                maybeRecipe(JAPTA.furnaceBooster)
        ), "tile.furnaceBooster.name", new ItemStack(JAPTA.furnaceBooster)));
        blockMap.put(new ResourceLocation("blocks", "heatConverter"), new EntryItemStack(Arrays.asList(
                new PageItemStack("The Heat Converter converts between Furnace heat and RF if placed next to a Furnace." +
                        "  In Absorb mode, it will generate " + TileEntityHeatConverter.USE + " RF/t while the Furnace is burning.", JAPTA.heatConverter),
                new PageText("It will also make the Furnace use fuel even when there's nothing to smelt." +
                        "  In Heat mode, it will use " + TileEntityHeatConverter.USE + " RF/t to smelt items in the Furnace."),
                maybeRecipe(JAPTA.heatConverter)
        ), "tile.converterHeat.name", new ItemStack(JAPTA.heatConverter)));
        blockMap.put(new ResourceLocation("blocks", "inhalers"), new EntryItemStack(Arrays.asList(
                new PageItemStack("Inhalers can extract a resource from the " + BlockBlaster.RANGE + " blocks in front of them." +
                        "  The closest block takes priority.  When placed, it will face away from you.  If you're sneaking, it will face toward you instead.", JAPTA.itemInhaler),
                maybeRecipe(JAPTA.fluxInhaler),
                maybeRecipe(JAPTA.itemInhaler),
                maybeRecipe(JAPTA.fluidInhaler)
        ), "guide.title.inhalers", new ItemStack(JAPTA.itemInhaler)));
        blockMap.put(new ResourceLocation("blocks", "itemSplitter"), new EntryItemStack(Arrays.asList(
                new PageItemStack("The Splitting Item Blaster is an Item Blaster that tries to equally split the items instead of filling the nearest block first.", JAPTA.itemSplitter),
                maybeRecipe(JAPTA.itemSplitter)
        ), "tile.itemSplitter.name", new ItemStack(JAPTA.itemSplitter)));
        blockMap.put(new ResourceLocation("blocks", "machineBase"), new EntryItemStack(Arrays.asList(
                new PageItemStack("The Machine Base is an important crafting component for most machines.", JAPTA.machineBase),
                maybeRecipe(JAPTA.machineBase)
        ), "tile.machineBase.name", new ItemStack(JAPTA.machineBase)));
        blockMap.put(new ResourceLocation("blocks", "mover"), new EntryItemStack(Arrays.asList(
                new PageItemStack("The Mover is a block that moves entities on top or in front of it using RF.  It also transfers RF in front of it, so only the starting point of a long chain needs to be powered.", JAPTA.mover),
                maybeRecipe(JAPTA.mover)
        ), "tile.mover.name", new ItemStack(JAPTA.mover)));
        blockMap.put(new ResourceLocation("blocks", "powerCabinet"), new EntryItemStack(Arrays.asList(
                new PageItemStack("The Power Cabinet is a multiblock structure consisting of Power Cabinet Shafts above a Power Cabinet Base.  RF can be inserted or extracted from the base.", JAPTA.powerCabinet),
                new PageText("It will not send RF on its own, so you'll have to extract it using another block like the Flux Hopper." +
                        "  The base holds " + (BlockPowerCabinet.MAX_META_VALUE - 1) + " RF and each Power Cabinet Shaft holds  " + JAPTA.powerCabinet.getMetaValue() * 15 + " RF." +
                        "  Scorched Power Cabinet Shafts hold " + JAPTA.powerCabinet2.getMetaValue() * 15 + " RF each."),
                maybeRecipe(JAPTA.powerCabinet),
                maybeRecipe(JAPTA.powerCabinetBase),
                new PageFurnaceRecipe(JAPTA.powerCabinet)
        ), "guide.title.powerCabinet", new ItemStack(JAPTA.powerCabinet)));
        blockMap.put(new ResourceLocation("blocks", "sheepAdapter"), new EntryItemStack(Arrays.asList(
                new PageItemStack("It has been discovered that sheep can hold RF.  The SheepFlux Adapter is able to harness this possibility and send and receive RF from sheep up to "+ TileEntitySheepAdapter.RANGE+" blocks away.", JAPTA.sheepAdapter),
                maybeRecipe(JAPTA.sheepAdapter)
        ), "tile.sheepAdapter.name", new ItemStack(JAPTA.sheepAdapter)));
        blockMap.put(new ResourceLocation("blocks", "rngQuarry"), new EntryItemStack(Arrays.asList(
                new PageItemStack("The RNG Quarry can mine out a square area of radius " + TileEntityRNGQuarry.RANGE + ".  It can mine anything that needs a wooden tool, or higher if given a tool.", JAPTA.rngQuarry),
                new PageText("Tools last slightly longer than normal in an RNG Quarry.  Items obtained will be placed in adjacent inventory blocks, or spewed if no space is available."),
                maybeRecipe(JAPTA.rngQuarry)
        ), "tile.rngQuarry.name", new ItemStack(JAPTA.rngQuarry)));
        blockMap.put(new ResourceLocation("blocks", "voidStack"), new EntryItemStack(Arrays.asList(
                new PageItemStack("Ever needed to store a huge number of items?  Now you can, thanks to the Void Stack.  However, it has one problem: you can only take out the item you put in last.", JAPTA.voidStack),
                maybeRecipe(JAPTA.voidStack)
        ), "tile.voidStack.name", new ItemStack(JAPTA.voidStack)));
        categories.add(new CategoryItemStack(blockMap, "guide.title.blocks", new ItemStack(JAPTA.machineBase)));
        Map<ResourceLocation, EntryAbstract> itemMap = new HashMap<ResourceLocation, EntryAbstract>();
        itemMap.put(new ResourceLocation("items", "batteryPotato"), new EntryItemStack(Arrays.asList(
                new PageItemStack("The Battery Potato allows you to eat RF in the form of a delicious potato.  It can be charged with anything that can charge items with RF, like the Charging Plate or the Chest Charger.", JAPTA.batteryPotato),
                maybeRecipe(JAPTA.batteryPotato)
        ), "item.batteryPotato.name", new ItemStack(JAPTA.batteryPotato)));
        itemMap.put(new ResourceLocation("items", "capacitor"), new EntryItemStack(Arrays.asList(
                new PageItemStack("The Handheld Power Cabinet is an item that can store RF and charge the other items in your inventory.  Craft it with Power Cabinet Shafts to increase capacity.", JAPTA.capacitor),
                maybeRecipe(JAPTA.capacitor)
        ), "item.capacitor.name", new ItemStack(JAPTA.capacitor)));
        itemMap.put(new ResourceLocation("items", "rfMeter"), new EntryItemStack(Arrays.asList(
                new PageItemStack("The RF Meter is an item that can be used on blocks to read their RF content.", JAPTA.rfMeter),
                maybeRecipe(JAPTA.rfMeter)
        ), "item.rfMeter.name", new ItemStack(JAPTA.rfMeter)));
        itemMap.put(new ResourceLocation("items", "diagnosticTool"), new EntryItemStack(Arrays.asList(
                new PageItemStack("The Diagnostic Tool is an upgraded version of the RF Meter.  It can read RF content as well as give extra information for certain blocks.", JAPTA.diagnosticTool),
                maybeRecipe(JAPTA.diagnosticTool)
        ), "item.diagnosticTool.name", new ItemStack(JAPTA.diagnosticTool)));
        itemMap.put(new ResourceLocation("items", "coils"), new EntryItemStack(Arrays.asList(
                new PageItemStack("Coils are crafting components for several machines.  There are two types of coils, Transmission and Reception.", JAPTA.coilReception),
                maybeRecipe(JAPTA.coilReception),
                maybeRecipe(JAPTA.coilTransmission)
        ), "guide.title.coils", new ItemStack(JAPTA.coilReception)));
        itemMap.put(new ResourceLocation("items", "levitator"), new EntryItemStack(Arrays.asList(
                new PageItemStack("The Personal Levitator is an item capable of lifting you off the ground using up to "+ ItemLevitator.USE+" RF/t.  Right-click to go up, Sneak-right-click to go down.", JAPTA.levitator),
                maybeRecipe(JAPTA.levitator)
        ), "item.levitator.name", new ItemStack(JAPTA.levitator)));
        categories.add(new CategoryItemStack(itemMap, "guide.title.items", new ItemStack(JAPTA.batteryPotato)));
        book.setCategoryList(categories);
        book.setAuthor("VpzomTrrfrt");
        book.setTitle("The JAPTA Book");
        book.setRegistryName(new ResourceLocation(JAPTA.MODID, "guideJAPTA"));
        book.setCustomModel(true);
        book.setDisplayName("guide.title");
        book.setWelcomeMessage("guide.title");
        book.setColor(Color.white);
        return book;
    }

    @Override
    public void handleModel(ItemStack bookStack) {
        GuideAPI.setModel(book, new ResourceLocation(JAPTA.MODID + ":guideBook"), "inventory");
    }

    @Override
    public void handlePost(ItemStack bookStack) {
        JAPTA.addRecipe(new ShapelessOreRecipe(JAPTA.CRAFTING_CATEGORY, bookStack, "paper", "ingotIron", "dustRedstone", "dustRedstone"));
        JAPTA.saveConfig();
    }

    private IPage maybeRecipe(Item item) {
        return maybeRecipe(JAPTA.getRecipe(item));
    }

    public static GuideJAPTA get() {
        if (instance == null) instance = new GuideJAPTA();
        return instance;
    }

    private IPage maybeRecipe(Block block) {
        return maybeRecipe(JAPTA.getRecipe(block));
    }

    private IPage maybeRecipe(IRecipe recipe) {
        if (recipe == null) {
            return new PageText("There's supposed to be a recipe here, but there isn't.");
        } else {
            return new PageIRecipe(recipe);
        }
    }

    public Book getBook() {
        return book;
    }
}
