package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.tileentity.TileEntityElevatorTop;

public class BlockElevatorTop extends BlockModelContainer {
    public BlockElevatorTop() {
        super(Material.glass);
        setCreativeTab(JAPTA.tab);
        setUnlocalizedName("elevatorTop");
        setHardness(1);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityElevatorTop();
    }

}
