package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.tileentity.TileEntityPowerCabinetBase;

public class BlockPowerCabinetBase extends BlockModelContainer {
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityPowerCabinetBase();
    }

    public BlockPowerCabinetBase() {
        super(Material.IRON);
        setCreativeTab(JAPTA.tab);
        setUnlocalizedName("powerCabinetBase");
        setRegistryName("powerCabinetBase");
    }
}
