package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.reederhome.colin.mods.JAPTA.JAPTA;

public class SimpleTEBlock extends BlockModelContainer {
    private Class<? extends TileEntity> teClass;

    public SimpleTEBlock(Material material, Class<? extends TileEntity> teClass, String name) {
        super(material);
        this.teClass = teClass;
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(JAPTA.tab);
        setHardness(1);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        try {
            return teClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
