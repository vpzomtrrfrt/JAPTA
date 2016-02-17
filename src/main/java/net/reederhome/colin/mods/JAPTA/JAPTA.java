package net.reederhome.colin.mods.JAPTA;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
        fluxBlaster = new BlockFluxBlaster();
        itemBlaster = new BlockItemBlaster();
        rngQuarry = new BlockRNGQuarry();
        chestCharger = new BlockChestCharger();

        rfMeter = new ItemRFMeter();
        batteryPotato = new ItemBatteryPotato();

        GameRegistry.registerBlock(cakeConverter, "cakeConverter");
        GameRegistry.registerBlock(fluxHopper, "fluxHopper");
        GameRegistry.registerBlock(chargingPlate, "chargingPlate");
        GameRegistry.registerBlock(elevatorShaft, "elevatorShaft");
        GameRegistry.registerBlock(elevatorTop, "elevatorTop");
        GameRegistry.registerBlock(fluxBlaster, "fluxBlaster");
        GameRegistry.registerBlock(itemBlaster, "itemBlaster");
        GameRegistry.registerBlock(rngQuarry, "rngQuarry");
        GameRegistry.registerBlock(chestCharger, "chestCharger");

        GameRegistry.registerItem(rfMeter, "rfMeter");
        GameRegistry.registerItem(batteryPotato, "batteryPotato");

        GameRegistry.registerTileEntity(TileEntityCakeConverter.class, "CakeConverter");
        GameRegistry.registerTileEntity(TileEntityFluxHopper.class, "FluxHopper");
        GameRegistry.registerTileEntity(TileEntityChargingPlate.class, "ChargingPlate");
        GameRegistry.registerTileEntity(TileEntityElevatorTop.class, "ElevatorTop");
        GameRegistry.registerTileEntity(TileEntityFluxBlaster.class, "FluxBlaster");
        GameRegistry.registerTileEntity(TileEntityItemBlaster.class, "ItemBlaster");
        GameRegistry.registerTileEntity(TileEntityRNGQuarry.class, "RNGQuarry");
        GameRegistry.registerTileEntity(TileEntityChestCharger.class, "ChestCharger");

        GameRegistry.addRecipe(new ShapedOreRecipe(cakeConverter, "frf", "rgr", "frf", 'f', Items.cake, 'r', "dustRedstone", 'g', "ingotGold"));
        GameRegistry.addRecipe(new ShapedOreRecipe(fluxHopper, "i i", "iri", " i ", 'i', "ingotIron", 'r', "dustRedstone"));
        GameRegistry.addRecipe(new ShapedOreRecipe(chargingPlate, "   ", "gpg", "oro", 'g', "dustGlowstone", 'p', Blocks.stone_pressure_plate, 'o', Blocks.obsidian, 'r', "dustRedstone"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(elevatorShaft, 4), "igi", "igi", "igi", 'i', "ingotIron", 'g', "blockGlass"));
        GameRegistry.addRecipe(new ShapedOreRecipe(elevatorTop, "grg", "rer", "rsr", 'r', "dustRedstone", 'e', Items.ender_pearl, 's', elevatorShaft, 'g', "nuggetGold"));

        GameRegistry.addRecipe(new ShapedOreRecipe(rfMeter, "n", "d", 'n', "nuggetGold", 'd', "dustRedstone"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(batteryPotato, 1, batteryPotato.getMaxDamage()), "cropPotato", "nuggetGold", "ingotIron", "dustRedstone"));

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void clientInit(FMLInitializationEvent ev) {
        JAPTAClient.registerClientThings();

        if(config.get("Enable Version Checker", "misc", true, "").getBoolean()) {
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
}
