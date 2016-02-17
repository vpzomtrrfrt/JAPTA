package net.reederhome.colin.mods.JAPTA.tileentity;

import cofh.api.energy.IEnergyReceiver;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

import java.util.List;
import java.util.Random;

public class TileEntityRNGQuarry extends TileEntityJPT implements IEnergyReceiver, ITickable {

    public static final int RANGE = 8;
    public static final int USE = 500;

    public ItemStack item = null;

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return 2000;
    }

    @Override
    public void update() {
        BlockPos me = getPos();
        if (stored >= USE) {
            for (int i = 0; i < 2; i++) { // try twice to find a valid spot
                BlockPos cp = me.add(new Random().nextInt(RANGE * 2) - RANGE, -1, new Random().nextInt(RANGE * 2) - RANGE);
                while (worldObj.isAirBlock(cp) || worldObj.getBlockState(cp).getBlock().getMaterial().isLiquid()) {
                    cp = cp.down();
                }
                IBlockState state = worldObj.getBlockState(cp);
                int thl = 0;
                if (item != null) {
                    thl = Math.min(thl, item.getItem().getHarvestLevel(item, state.getBlock().getHarvestTool(state)));
                }
                int bhl = state.getBlock().getHarvestLevel(state);
                if (thl >= bhl && state.getBlock().getBlockHardness(worldObj, cp) != -1) {
                    List<ItemStack> drops = state.getBlock().getDrops(worldObj, cp, state, EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, item));
                    worldObj.setBlockToAir(cp);
                    for (ItemStack drop : drops) {
                        for(EnumFacing side : EnumFacing.VALUES) {
                            BlockPos cip = me.offset(side);
                            TileEntity ite = worldObj.getTileEntity(cip);
                            if(ite instanceof IInventory) {
                                drop = TileEntityHopper.putStackInInventoryAllSlots((IInventory) ite, drop, side.getOpposite());
                            }
                            if(drop == null) {
                                break;
                            }
                        }
                        if(drop != null) {
                            EntityItem ent = new EntityItem(worldObj, me.getX()+0.5, me.getY()+1, me.getZ()+0.5);
                            ent.setEntityItemStack(drop);
                            worldObj.spawnEntityInWorld(ent);
                        }
                    }
                    stored -= USE;
                    if(item != null && item.isItemStackDamageable() && Math.random() < 0.9 && bhl > 0) {
                        item.setItemDamage(item.getItemDamage()+1);
                        if(item.getItemDamage() >= item.getMaxDamage()) {
                            item = null;
                        }
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        item = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Item"));
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        NBTTagCompound nbt = new NBTTagCompound();
        item.writeToNBT(nbt);
        tag.setTag("Item", nbt);
    }
}