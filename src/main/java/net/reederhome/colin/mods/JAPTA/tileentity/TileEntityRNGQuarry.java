package net.reederhome.colin.mods.JAPTA.tileentity;

import cofh.api.energy.IEnergyReceiver;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.reederhome.colin.mods.JAPTA.IDiagnosable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TileEntityRNGQuarry extends TileEntityJPT implements IEnergyReceiver, ITickable, IDiagnosable {

    public static int RANGE = 8;
    public static final int USE = 500;

    public ItemStack item = null;

    private long lastMinedTick;

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return 2000;
    }

    @Override
    public void update() {
        BlockPos me = getPos();
        if (stored >= USE && worldObj instanceof WorldServer) {
            FakePlayer player = new FakePlayer((WorldServer) worldObj, new GameProfile(UUID.randomUUID(), "fake_player_"));
            for (int i = 0; i < 2; i++) { // try twice to find a valid spot
                BlockPos cp = me.add(new Random().nextInt(RANGE * 2) - RANGE, -1, new Random().nextInt(RANGE * 2) - RANGE);
                IBlockState state = worldObj.getBlockState(cp);
                while ((worldObj.isAirBlock(cp) || state.getBlock().getMaterial(state).isLiquid()) && cp.getY() >= 0) {
                    cp = cp.down();
                    state = worldObj.getBlockState(cp);
                }
                int thl = 0;
                boolean canUseItem = false;
                if (item != null) {
                    if(!isBroken(item)) {
                        canUseItem = true;
                        thl = Math.max(thl, item.getItem().getHarvestLevel(item, state.getBlock().getHarvestTool(state)));
                    }
                }
                int bhl = state.getBlock().getHarvestLevel(state);
                boolean usedItem = item != null && bhl > 0;
                if (thl >= bhl && state.getBlock().getBlockHardness(state, worldObj, cp) != -1) {
                    List<ItemStack> drops;
                    int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.field_185308_t, item);
                    if(canUseItem && EnchantmentHelper.getEnchantmentLevel(Enchantments.field_185306_r, item) > 0 && state.getBlock().canSilkHarvest(worldObj, cp, state, player)) {
                        drops = new ArrayList<ItemStack>();
                        drops.add(new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)));
                        usedItem = true;
                    }
                    else {
                        drops = state.getBlock().getDrops(worldObj, cp, state, fortune);
                    }
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
                    if(usedItem && Math.random() < 0.9) {
                        item.onBlockDestroyed(worldObj, state, cp, player);
                        if(item.stackSize < 1) {
                            item = null;
                        }
                        /*if(item.attemptDamageItem(1, new Random())) {
                            item = null;
                        }*/
                        /*item.setItemDamage(item.getItemDamage()+1);
                        if(item.getItemDamage() >= item.getMaxDamage()) {
                            item = null;
                        }*/
                    }
                    lastMinedTick = worldObj.getTotalWorldTime();
                    break;
                }
            }
        }
    }

    /**
     * Tinkers' Construct is annoying
     * @param item the item to check
     * @return whether it's a broken Tinkers' tool
     */
    private boolean isBroken(ItemStack item) {
        if(item.hasTagCompound()) {
            NBTTagCompound tag = item.getTagCompound();
            if(tag.hasKey("Stats")) {
                NBTTagCompound stats = tag.getCompoundTag("Stats");
                return stats.getBoolean("Broken");
            }
        }
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if(tag.hasKey("Item")) {
            item = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Item"));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        if(item != null) {
            NBTTagCompound nbt = new NBTTagCompound();
            item.writeToNBT(nbt);
            tag.setTag("Item", nbt);
        }
    }

    @Override
    public boolean addInformation(ICommandSender sender, IBlockAccess world, BlockPos pos) {
        if(isBroken(item)) {
            sender.addChatMessage(new TextComponentTranslation("tile.rngQuarry.diagnostic.brokenTool"));
            return true;
        }
        else if(lastMinedTick+10 < worldObj.getTotalWorldTime() && stored >= USE) {
            sender.addChatMessage(new TextComponentTranslation("tile.rngQuarry.diagnostic.notMining"));
            return true;
        }
        return false;
    }
}
