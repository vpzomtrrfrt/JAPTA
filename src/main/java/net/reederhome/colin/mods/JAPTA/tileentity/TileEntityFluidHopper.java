package net.reederhome.colin.mods.JAPTA.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.*;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.block.BlockFluidHopper;

public class TileEntityFluidHopper extends TileEntity implements IFluidHandler, ITickable {
    public static int MAX_HELD = 5000;
    private FluidStack content;

    @Override
    public int fill(EnumFacing enumFacing, FluidStack fluidStack, boolean b) {
        int tr;
        if (content == null || fluidStack.getFluid() == content.getFluid()) {
            tr = fluidStack.amount;
            if (tr > MAX_HELD) {
                tr = MAX_HELD;
            }
            if (content != null && content.amount + tr > MAX_HELD) {
                tr = MAX_HELD - content.amount;
            }
        } else {
            tr = 0;
        }
        if (b && tr != 0) {
            if (content == null) content = new FluidStack(fluidStack.getFluid(), tr);
            else content.amount += tr;
        }
        return tr;
    }

    @Override
    public FluidStack drain(EnumFacing enumFacing, FluidStack fluidStack, boolean b) {
        if (content != null && fluidStack.getFluid() == content.getFluid()) {
            if (fluidStack.amount < content.amount) {
                if (b) {
                    content.amount -= fluidStack.amount;
                }
                return fluidStack;
            } else {
                FluidStack tr = content;
                if (b) {
                    content = null;
                }
                return tr;
            }
        }
        return new FluidStack(fluidStack.getFluid(), 0);
    }

    @Override
    public FluidStack drain(EnumFacing enumFacing, int i, boolean b) {
        if (i < content.amount) {
            if (b) {
                content.amount -= i;
            }
            return new FluidStack(content.getFluid(), i);
        } else {
            FluidStack tr = content;
            if (b) {
                content = null;
            }
            return tr;
        }
    }

    @Override
    public boolean canFill(EnumFacing enumFacing, Fluid fluid) {
        return content == null || fluid == content.getFluid();
    }

    @Override
    public boolean canDrain(EnumFacing enumFacing, Fluid fluid) {
        return content != null && fluid == content.getFluid();
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing enumFacing) {
        return new FluidTankInfo[]{new FluidTankInfo(content, MAX_HELD)};
    }

    public boolean hasAmount(int i) {
        return content != null && content.amount >= i;
    }

    @Override
    public void update() {
        IBlockState state = worldObj.getBlockState(getPos());
        if (content != null) {
            EnumFacing facing = JAPTA.safeGetValue(state, BlockFluidHopper.FACING);
            BlockPos dest = getPos().offset(facing);
            TileEntity te = worldObj.getTileEntity(dest);
            if (te instanceof IFluidHandler) {
                content.amount -= ((IFluidHandler) te).fill(facing.getOpposite(), content, true);
                if (content.amount <= 0) {
                    content = null;
                }
            }
        }
        int remaining = MAX_HELD - (content == null ? 0 : content.amount);
        if (remaining > 0) {
            BlockPos src = getPos().up();
            TileEntity te = worldObj.getTileEntity(src);
            if(te instanceof IFluidHandler) {
                if(content != null) {
                    content.amount += ((IFluidHandler) te).drain(EnumFacing.DOWN, new FluidStack(content.getFluid(), remaining), true).amount;
                }
                else {
                    content = ((IFluidHandler) te).drain(EnumFacing.DOWN, remaining, true);
                }
            }
        }
    }
}
