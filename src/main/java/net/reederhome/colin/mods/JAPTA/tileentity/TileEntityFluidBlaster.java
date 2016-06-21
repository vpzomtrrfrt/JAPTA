package net.reederhome.colin.mods.JAPTA.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.*;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.block.BlockBlaster;

public class TileEntityFluidBlaster extends TileEntity implements IFluidHandler, ITickable {
    public static int MAX_HELD = 9000;
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
        if (content == null) {
            return new FluidStack(FluidRegistry.WATER, 0);
        } else if (i < content.amount) {
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

    @Override
    public void update() {
        IBlockState state = worldObj.getBlockState(getPos());
        EnumFacing facing = JAPTA.safeGetValue(state, BlockBlaster.FACING);
        for (int i = 1; i <= BlockBlaster.RANGE; i++) {
            BlockPos cp = getPos().offset(facing, i);
            while (worldObj.getBlockState(cp).getBlock() == JAPTA.elevatorShaft) {
                cp = cp.up();
            }
            TileEntity te = worldObj.getTileEntity(cp);
            if (te instanceof IFluidHandler) {
                if (((BlockBlaster) state.getBlock()).isInhaler()) {
                    if (content == null) {
                        content = ((IFluidHandler) te).drain(facing.getOpposite(), MAX_HELD, true);
                    } else {
                        int remaining = MAX_HELD - content.amount;
                        if(remaining > 0 && ((IFluidHandler) te).canDrain(facing.getOpposite(), content.getFluid())) {
                            content = ((IFluidHandler) te).drain(facing.getOpposite(), new FluidStack(content.getFluid(), remaining), true);
                        }
                    }
                } else if (content != null) {
                    content.amount -= ((IFluidHandler) te).fill(facing.getOpposite(), content, true);
                    if (content.amount <= 0) {
                        content = null;
                    }
                }
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);
        if (content != null) {
            tag.setTag("Content", content.writeToNBT(new NBTTagCompound()));
        }
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey("Content")) {
            content = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("Content"));
        }
    }
}
