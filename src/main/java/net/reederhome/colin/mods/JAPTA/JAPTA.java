package net.reederhome.colin.mods.JAPTA;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.reederhome.colin.mods.JAPTA.block.*;
import net.reederhome.colin.mods.JAPTA.item.ItemBatteryPotato;
import net.reederhome.colin.mods.JAPTA.item.ItemRFMeter;
import net.reederhome.colin.mods.JAPTA.tileentity.TileEntityCakeConverter;
import net.reederhome.colin.mods.JAPTA.tileentity.TileEntityChargingPlate;
import net.reederhome.colin.mods.JAPTA.tileentity.TileEntityElevatorTop;
import net.reederhome.colin.mods.JAPTA.tileentity.TileEntityFluxHopper;

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

    public static ItemRFMeter rfMeter;
    public static ItemBatteryPotato batteryPotato;

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

        rfMeter = new ItemRFMeter();
        batteryPotato = new ItemBatteryPotato();

        GameRegistry.registerBlock(cakeConverter, "cakeConverter");
        GameRegistry.registerBlock(fluxHopper, "fluxHopper");
        GameRegistry.registerBlock(chargingPlate, "chargingPlate");
        GameRegistry.registerBlock(elevatorShaft, "elevatorShaft");
        GameRegistry.registerBlock(elevatorTop, "elevatorTop");

        GameRegistry.registerItem(rfMeter, "rfMeter");
        GameRegistry.registerItem(batteryPotato, "batteryPotato");

        GameRegistry.registerTileEntity(TileEntityCakeConverter.class, "CakeConverter");
        GameRegistry.registerTileEntity(TileEntityFluxHopper.class, "FluxHopper");
        GameRegistry.registerTileEntity(TileEntityChargingPlate.class, "ChargingPlate");
        GameRegistry.registerTileEntity(TileEntityElevatorTop.class, "ElevatorTop");

        config.save();
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void clientInit(FMLInitializationEvent ev) {
        JAPTAClient.registerClientThings();
    }
}
