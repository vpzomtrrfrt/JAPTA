package net.reederhome.colin.mods.JAPTA.item;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.darkhax.tesla.api.ITeslaHolder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.energy.IEnergyStorage;
import net.reederhome.colin.mods.JAPTA.IDiagnosable;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.tileentity.TileEntitySheepAdapter;

public class ItemRFMeter extends Item {
    private boolean advanced;

    public ItemRFMeter(boolean advanced) {
        super();
        this.advanced = advanced;
        setMaxStackSize(1);
        String name = advanced ? "diagnosticTool" : "rfMeter";
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(JAPTA.tab);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand p_onItemUse_4_, EnumFacing side, float p_onItemUse_6_, float p_onItemUse_7_, float p_onItemUse_8_) {
        if (!world.isRemote) {
            IBlockState state = world.getBlockState(pos);
            TileEntity te = world.getTileEntity(pos);
            int value = -2;
            int max = -1;
            String powerType = "RF";
            if (te instanceof IEnergyReceiver) {
                value = ((IEnergyReceiver) te).getEnergyStored(side);
                max = ((IEnergyReceiver) te).getMaxEnergyStored(side);
            } else if (te instanceof IEnergyProvider) {
                value = ((IEnergyProvider) te).getEnergyStored(side);
                max = ((IEnergyProvider) te).getMaxEnergyStored(side);
            } else if (te != null && te.hasCapability(JAPTA.CAPABILITY_TESLA_HOLDER, side)) {
                ITeslaHolder holder = te.getCapability(JAPTA.CAPABILITY_TESLA_HOLDER, side);
                value = (int) holder.getStoredPower();
                max = (int) holder.getCapacity();
                powerType = "T";
            }
            else if(te != null && te.hasCapability(JAPTA.CAPABILITY_FORGE_ENERGY_STORAGE, side)) {
                IEnergyStorage storage = te.getCapability(JAPTA.CAPABILITY_FORGE_ENERGY_STORAGE, side);
                value = storage.getEnergyStored();
                max = storage.getMaxEnergyStored();
                powerType = "CE";
            }
            boolean someinfo = false;
            if (value != -2) {
                player.sendMessage(new TextComponentTranslation("text.japta.rfmeter.power", value, max, powerType));
                someinfo = true;
            }
            if (advanced) {
                if (state.getBlock() instanceof IDiagnosable) {
                    if (((IDiagnosable) state.getBlock()).addInformation(player, world, pos)) {
                        someinfo = true;
                    }
                }
                if (te instanceof IDiagnosable) {
                    if (((IDiagnosable) te).addInformation(player, world, pos)) {
                        someinfo = true;
                    }
                }
            }
            if (!someinfo) {
                player.sendMessage(new TextComponentTranslation("text.japta.rfmeter." + (advanced ? "advancedNo" : "no")));
            }
        }
        return EnumActionResult.SUCCESS;
    }

    public void onEntityInteract(Entity target, ICommandSender player) {
        boolean someinfo = false;
        if(target instanceof EntitySheep) {
            int held = target.getEntityData().getInteger("Energy");
            player.sendMessage(new TextComponentTranslation("text.japta.rfmeter.powerEntity", held, TileEntitySheepAdapter.MAX_SHEEP_AMOUNT, "RF"));
            someinfo = true;
        }
        if(advanced && target instanceof IDiagnosable) {
            if(((IDiagnosable) target).addInformation(player, player.getEntityWorld(), null)) {
                someinfo = true;
            }
        }
        if(!someinfo) {
            player.sendMessage(new TextComponentTranslation("text.japta.rfmeter."+(advanced?"advancedNoEntity":"no")));
        }
    }
}
