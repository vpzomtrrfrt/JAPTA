package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.reederhome.colin.mods.JAPTA.tileentity.TileEntityFurnaceBooster;

public class BlockFurnaceBooster extends BlockModelContainer {

    public BlockFurnaceBooster() {
        super(Material.rock);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityFurnaceBooster();
    }
}
