package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.tileentity.TileEntityBonemealApplicator;

public class BlockBonemealApplicator extends BlockModelContainer {
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBonemealApplicator();
    }

    public BlockBonemealApplicator() {
        super(Material.ROCK);
        setUnlocalizedName("bonemealApplicator");
        setHardness(1);
        setCreativeTab(JAPTA.tab);
    }
}
