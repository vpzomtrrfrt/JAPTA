package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.reederhome.colin.mods.JAPTA.JAPTA;

public class BlockElevatorShaft extends Block {
    public BlockElevatorShaft() {
        super(Material.glass);
        setHardness(1);
        setCreativeTab(JAPTA.tab);
        setUnlocalizedName("elevatorShaft");
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
}
