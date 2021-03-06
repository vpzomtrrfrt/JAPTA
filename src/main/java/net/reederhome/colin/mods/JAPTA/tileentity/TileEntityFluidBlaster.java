package net.reederhome.colin.mods.JAPTA.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.block.BlockBlaster;

public class TileEntityFluidBlaster extends TileEntity implements ITickable {
    public static int MAX_HELD = 9000;
    private FluidStack content;

    public class Handler implements IFluidHandler {

        @Override
        public int fill(FluidStack fluidStack, boolean b) {
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
        public FluidStack drain(FluidStack fluidStack, boolean b) {
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
        public FluidStack drain(int i, boolean b) {
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
        public IFluidTankProperties[] getTankProperties() {
            return new IFluidTankProperties[]{new FluidTankProperties(content, MAX_HELD)};
        }
    }

    @Override
    public void update() {
        IBlockState state = getWorld().getBlockState(getPos());
        EnumFacing facing = JAPTA.safeGetValue(state, BlockBlaster.FACING);
        for (int i = 1; i <= BlockBlaster.RANGE; i++) {
            BlockPos cp = getPos().offset(facing, i);
            while (getWorld().getBlockState(cp).getBlock() == JAPTA.elevatorShaft) {
                cp = cp.up();
            }
            TileEntity te = getWorld().getTileEntity(cp);
            if (te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite())) {
                IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite());
                if (((BlockBlaster) state.getBlock()).isInhaler()) {
                    if (content == null) {
                        content = handler.drain(MAX_HELD, true);
                    } else {
                        int remaining = MAX_HELD - content.amount;
                        if (remaining > 0) {
                            content = handler.drain(new FluidStack(content.getFluid(), remaining), true);
                        }
                    }
                } else if (content != null) {
                    content.amount -= handler.fill(content, true);
                    if (content.amount <= 0) {
                        content = null;
                    }
                }
            }
        }
    }

    @Override
    public boolean hasCapability(Capability<?> p_hasCapability_1_, EnumFacing p_hasCapability_2_) {
        if(p_hasCapability_1_ == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(p_hasCapability_1_, p_hasCapability_2_);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> p_getCapability_1_, EnumFacing p_getCapability_2_) {
        if(p_getCapability_1_ == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T)new Handler();
        }
        return super.getCapability(p_getCapability_1_, p_getCapability_2_);
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
