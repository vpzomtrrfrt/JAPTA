package net.reederhome.colin.mods.JAPTA;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IDiagnosable {
    boolean addInformation(ICommandSender sender, IBlockAccess world, BlockPos pos);
}
