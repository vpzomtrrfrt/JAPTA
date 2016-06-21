package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.tileentity.TileEntityDungeonMaker;

public class BlockDungeonMaker extends BlockModelContainer {

    public BlockDungeonMaker() {
        super(Material.ROCK);
        setHardness(1);
        setUnlocalizedName("dungeonMaker");
        setRegistryName("dungeonMaker");
        setCreativeTab(JAPTA.tab);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityDungeonMaker();
    }
}
