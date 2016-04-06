package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;

public abstract class BlockModelContainer extends BlockContainer {
    public BlockModelContainer(Material p_i45386_1_) {
        super(p_i45386_1_);
    }

    @Override
    public int getRenderType() {
        return 3;
    }
}
