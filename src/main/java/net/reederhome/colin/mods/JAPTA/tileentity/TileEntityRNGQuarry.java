package net.reederhome.colin.mods.JAPTA.tileentity;

import com.mojang.authlib.GameProfile;
import mcjty.lib.tools.ItemStackTools;
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
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.reederhome.colin.mods.JAPTA.IDiagnosable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TileEntityRNGQuarry extends TileEntityJPT implements TileEntityJPT.EnergyReceiver, ITickable, IDiagnosable {

    public static int RANGE = 8;
    public static final int USE = 500;

    public ItemStack item = ItemStack.EMPTY;

    private long lastMinedTick;

    private FakePlayer player;

    @Override
    public void setWorld(World p_setWorld_1_) {
        super.setWorld(p_setWorld_1_);
        if(getWorld() instanceof WorldServer) {
            player = new FakePlayer((WorldServer) getWorld(), new GameProfile(UUID.randomUUID(), "fake_player_"));
        }
    }

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return 2000;
    }

    @Override
    public void update() {
        BlockPos me = getPos();
        if (stored >= USE && getWorld() instanceof WorldServer && player != null) {
            BlockPos cp = me.add(new Random().nextInt(RANGE * 2) - RANGE, -1, new Random().nextInt(RANGE * 2) - RANGE);
            IBlockState state = getWorld().getBlockState(cp);
            while ((getWorld().isAirBlock(cp) || state.getBlock().getMaterial(state).isLiquid()) && cp.getY() >= 0) {
                cp = cp.down();
                state = getWorld().getBlockState(cp);
            }
            if (cp.getY() < 0) {
                return;
            }
            int thl = 0;
            boolean canUseItem = false;
            if (ItemStackTools.isValid(item)) {
                if (!isBroken(item)) {
                    canUseItem = true;
                    String harvestTool = state.getBlock().getHarvestTool(state);
                    if (harvestTool != null) {
                        thl = Math.max(thl, item.getItem().getHarvestLevel(item, harvestTool, player, state));
                    }
                }
            }
            int bhl = state.getBlock().getHarvestLevel(state);
            boolean usedItem = ItemStackTools.isValid(item) && bhl > 0;
            if (thl >= bhl && state.getBlock().getBlockHardness(state, getWorld(), cp) != -1) {
                List<ItemStack> drops;
                int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, item);
                if (canUseItem && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, item) > 0 && state.getBlock().canSilkHarvest(getWorld(), cp, state, player)) {
                    drops = new ArrayList<ItemStack>();
                    drops.add(new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)));
                    usedItem = true;
                } else {
                    drops = state.getBlock().getDrops(getWorld(), cp, state, fortune);
                }
                getWorld().setBlockToAir(cp);
                for (ItemStack drop : drops) {
                    for (EnumFacing side : EnumFacing.VALUES) {
                        BlockPos cip = me.offset(side);
                        TileEntity ite = getWorld().getTileEntity(cip);
                        if (ite instanceof IInventory) {
                            drop = TileEntityHopper.putStackInInventoryAllSlots(null, (IInventory) ite, drop, side.getOpposite());
                        }
                        if (drop == null) {
                            break;
                        }
                    }
                    if (drop != null) {
                        EntityItem ent = new EntityItem(getWorld(), me.getX() + 0.5, me.getY() + 1, me.getZ() + 0.5);
                        ent.setEntityItemStack(drop);
                        getWorld().spawnEntity(ent);
                    }
                }
                stored -= USE;
                if (usedItem && Math.random() < 0.9) {
                    item.onBlockDestroyed(getWorld(), state, cp, player);
                   /*if(item.attemptDamageItem(1, new Random())) {
                    item = null;
					}*/
				   /*item.setItemDamage(item.getItemDamage()+1);
					if(item.getItemDamage() >= item.getMaxDamage()) {
					item = null;
					}*/
                }
                lastMinedTick = getWorld().getTotalWorldTime();
            }
        }
    }

    /**
     * Tinkers' Construct is annoying
     *
     * @param item the item to check
     * @return whether it's a broken Tinkers' tool
     */
    private boolean isBroken(ItemStack item) {
        if (item != null && item.hasTagCompound()) {
            NBTTagCompound tag = item.getTagCompound();
            if (tag.hasKey("Stats")) {
                NBTTagCompound stats = tag.getCompoundTag("Stats");
                return stats.getBoolean("Broken");
            }
        }
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey("Item")) {
            item = new ItemStack(tag.getCompoundTag("Item"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);
        if (item != null) {
            NBTTagCompound nbt = new NBTTagCompound();
            item.writeToNBT(nbt);
            tag.setTag("Item", nbt);
        }
        return tag;
    }

    @Override
    public boolean addInformation(ICommandSender sender, IBlockAccess world, BlockPos pos) {
        if (isBroken(item)) {
            sender.sendMessage(new TextComponentTranslation("tile.rngQuarry.diagnostic.brokenTool"));
            return true;
        } else if (lastMinedTick + 10 < getWorld().getTotalWorldTime() && stored >= USE) {
            sender.sendMessage(new TextComponentTranslation("tile.rngQuarry.diagnostic.notMining"));
            return true;
        }
        return false;
    }
}
