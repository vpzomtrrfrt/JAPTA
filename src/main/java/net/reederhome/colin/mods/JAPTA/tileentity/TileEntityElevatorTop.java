package net.reederhome.colin.mods.JAPTA.tileentity;

import cofh.api.energy.IEnergyReceiver;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.reederhome.colin.mods.JAPTA.IDiagnosable;
import net.reederhome.colin.mods.JAPTA.JAPTA;

import java.util.List;

public class TileEntityElevatorTop extends TileEntityJPT implements IEnergyReceiver, ITickable, IDiagnosable {

    public static int USE_BASE = 1000;
    public static int USE_EXTRA = 100;

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return 26000;
    }

    public static int getEnergyCost(int d) {
        return USE_BASE+(USE_EXTRA-1)*d;
    }

    @Override
    public void update() {
        BlockPos me = getPos();
        if(stored >= USE_BASE) {
            int d = 1;
            while(true) {
                BlockPos cp = me.down(d);
                Block b = worldObj.getBlockState(cp).getBlock();
                if(b == Blocks.air) {
                    break;
                }
                else if(b != JAPTA.elevatorShaft) {
                    return;
                }
                d++;
            }
            int cost = getEnergyCost(d);
            List<EntityLivingBase> l = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.fromBounds(me.getX(), me.getY()+1, me.getZ(), me.getX()+1, me.getY()+1.5, me.getZ()+1));
            for(EntityLivingBase b : l) {
                if(stored >= cost && b.isSneaking()) {
                    b.setPositionAndUpdate(me.getX() + 0.5, me.getY() - d - 1, me.getZ() + 0.5);
                    worldObj.playSoundAtEntity(b, "mob.chicken.plop", 1, 1);
                    stored -= cost;
                }
            }
            List<EntityLivingBase> l2 = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.fromBounds(me.getX(), me.getY() - d - 1, me.getZ(), me.getX()+1, me.getY() - d, me.getZ()+1));
            for(EntityLivingBase b : l2) {
                if(stored >= cost && b.motionY > 0) {
                    b.setPositionAndUpdate(me.getX() + 0.5, me.getY() + 1, me.getZ() + 0.5);
                    worldObj.playSoundAtEntity(b, "mob.chicken.plop", 1, 1);
                    stored -= cost;
                }
            }
        }
    }

    @Override
    public boolean addInformation(ICommandSender sender, IBlockAccess world, BlockPos pos) {
        int i = 1;
        boolean air;
        while(true) {
            IBlockState state = world.getBlockState(pos.down(i));
            if(state.getBlock() == JAPTA.elevatorShaft) {
                i++;
            }
            else {
                air = state.getBlock() == Blocks.air;
                break;
            }
        }
        sender.addChatMessage(new ChatComponentTranslation("tile.elevatorTop.diagnostic", i, getEnergyCost(i)));
        if(!air) {
            sender.addChatMessage(new ChatComponentTranslation("tile.elevatorTop.diagnostic.noAir"));
        }
        return true;
    }
}
