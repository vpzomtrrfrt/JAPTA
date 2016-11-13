package net.reederhome.colin.mods.JAPTA;

import amerifrance.guideapi.api.GuideAPI;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

public class JAPTAClient {

    public static void init() {
        registerBlock(JAPTA.cakeConverter, "cakeConverter");
        registerBlock(JAPTA.fluxHopper, "fluxHopper");
        registerBlock(JAPTA.chargingPlate, "chargingPlate");
        registerBlock(JAPTA.elevatorShaft, "elevatorShaft");
        registerBlock(JAPTA.elevatorTop, "elevatorTop");
        registerBlock(JAPTA.fluxBlaster, "fluxBlaster");
        registerBlock(JAPTA.itemBlaster, "itemBlaster");
        registerBlock(JAPTA.fluidBlaster, "fluidBlaster");
        registerBlock(JAPTA.fluxInhaler, "fluxInhaler");
        registerBlock(JAPTA.itemInhaler, "itemInhaler");
        registerBlock(JAPTA.fluidInhaler, "fluidInhaler");
        registerBlock(JAPTA.itemSplitter, "itemSplitter");
        registerBlock(JAPTA.rngQuarry, "rngQuarry");
        registerBlock(JAPTA.chestCharger, "chestCharger");
        registerBlock(JAPTA.mover, "mover");
        registerBlock(JAPTA.bonemealApplicator, "bonemealApplicator");
        registerBlock(JAPTA.powerCabinetBase, "powerCabinetBase");
        registerBlock(JAPTA.heatConverter, "heatConverter");
        registerBlock(JAPTA.furnaceBooster, "furnaceBooster");
        registerBlock(JAPTA.machineBase, "machineBase");
        registerBlock(JAPTA.fluidHopper, "fluidHopper");
        registerBlock(JAPTA.eater, "eater");
        registerBlock(JAPTA.dungeonMaker, "dungeonMaker");
        registerBlock(JAPTA.fisher, "fisher");
        registerBlock(JAPTA.sheepAdapter, "sheepAdapter");
        registerBlock(JAPTA.voidStack, "voidStack");

        ModelResourceLocation[] powerCabinetLocations = new ModelResourceLocation[16];

        for (int i = 0; i < 16; i++) {
            registerBlock(JAPTA.powerCabinet, i, "powerCabinet" + i);
            registerBlock(JAPTA.powerCabinet2, i, "powerCabinet" + i);
            powerCabinetLocations[i] = locationForName("powerCabinet" + i);
        }

        ModelBakery.registerItemVariants(Item.getItemFromBlock(JAPTA.powerCabinet), powerCabinetLocations);
        ModelBakery.registerItemVariants(Item.getItemFromBlock(JAPTA.powerCabinet2), powerCabinetLocations);

        registerItem(JAPTA.rfMeter, "rfMeter");
        registerItem(JAPTA.batteryPotato, "batteryPotato");
        registerItem(JAPTA.diagnosticTool, "diagnosticTool");
        registerItem(JAPTA.coilReception, "coilReception");
        registerItem(JAPTA.coilTransmission, "coilTransmission");
        registerItem(JAPTA.poweredMultiTool, "poweredMultiTool");
        registerItem(JAPTA.capacitor, "capacitor");

        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
            @Override
            public int getColorFromItemstack(ItemStack stack, int i) {
                return JAPTA.poweredMultiTool.getColorFromItemStack(stack, i);
            }
        }, JAPTA.poweredMultiTool);
    }

    public static void preInit() {
        if (Loader.isModLoaded("guideapi")) {
            System.out.println("Setting guideBook model");
            GuideAPI.setModel(GuideJAPTA.get().getBook(), new ResourceLocation(JAPTA.MODID + ":guideBook"), "inventory");
        }
    }

    public static void registerItem(Item item, int meta, ModelResourceLocation loc) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, loc);
    }

    public static void registerItem(Item item, int meta, String name) {
        registerItem(item, meta, locationForName(name));
    }

    public static void registerItem(Item item, String name) {
        registerItem(item, 0, name);
    }

    public static void registerBlock(Block block, String name) {
        registerBlock(block, 0, name);
    }

    public static void registerBlock(Block block, int meta, String name) {
        registerItem(Item.getItemFromBlock(block), meta, name);
    }

    private static ModelResourceLocation locationForName(String name) {
        return new ModelResourceLocation(JAPTA.MODID + ":" + name, "inventory");
    }
}
