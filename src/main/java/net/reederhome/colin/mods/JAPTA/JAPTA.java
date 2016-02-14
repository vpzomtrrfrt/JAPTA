package net.reederhome.colin.mods.JAPTA;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(name="JAPTA", modid=JAPTA.MODID)
public class JAPTA {
    public static final String MODID = "japta";

    public static Block cakeConverter;

    public static Item rfMeter;

    private Configuration config;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent ev) {
        config = new Configuration(ev.getSuggestedConfigurationFile());
        config.load();
        config.addCustomCategoryComment("recipes", "Enable/Disable crafting of certain items");

        cakeConverter = new BlockCakeConverter();

        rfMeter = new ItemRFMeter();

        GameRegistry.registerBlock(cakeConverter, "cakeConverter");

        GameRegistry.registerItem(rfMeter, "rfMeter");

        GameRegistry.registerTileEntity(TileEntityCakeConverter.class, "CakeConverter");

        config.save();
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void clientInit(FMLInitializationEvent ev) {
        JAPTAClient.registerClientThings();
    }
}
