package net.reederhome.colin.mods.JAPTA.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.reederhome.colin.mods.JAPTA.IDiagnosable;
import net.reederhome.colin.mods.JAPTA.JAPTA;

public class BlockElevatorShaft extends Block implements IDiagnosable {
    public BlockElevatorShaft() {
        super(Material.glass);
        setHardness(1);
        setCreativeTab(JAPTA.tab);
        setUnlocalizedName("elevatorShaft");
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean addInformation(ICommandSender sender, IBlockAccess world, BlockPos pos) {
        BlockPos cp = pos.up();
        while(true) {
            IBlockState state = world.getBlockState(cp);
            if(state.getBlock() != this) {
                if(state.getBlock() == JAPTA.elevatorTop) {
                    ((IDiagnosable)world.getTileEntity(cp)).addInformation(sender, world, cp);
                }
                else {
                    sender.addChatMessage(new ChatComponentTranslation("tile.elevatorShaft.diagnostic.noTop"));
                }
                return true;
            }
            cp = cp.up();
        }
    }
}
