package net.reederhome.colin.mods.JAPTA.item;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class ItemPoweredMultiTool extends ItemJPT {

    public static final int USE = 200;

    public ItemPoweredMultiTool() {
        super();
        setUnlocalizedName("poweredMultiTool");
        setRegistryName("poweredMultiTool");
    }

    private ToolMaterial getMaterial(ItemStack stack, String type) {
        NBTTagCompound materials = getMaterialsTag(stack);
        if (materials != null && materials.hasKey(type)) {
            return ToolMaterial.valueOf(materials.getString(type));
        }
        return ToolMaterial.WOOD;
    }

    private NBTTagCompound getMaterialsTag(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) {
            NBTTagCompound materials = tag.getCompoundTag("Materials");
            if (materials != null) {
                return materials;
            }
        }
        return null;
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String type, @Nullable EntityPlayer p_getHarvestLevel_3_, @Nullable IBlockState p_getHarvestLevel_4_) {
        if (!isDead(stack)) {
            return getMaterial(stack, type).getHarvestLevel();
        }
        return 0;
    }

    private boolean isDead(ItemStack stack) {
        return stack.getItemDamage() + USE > stack.getMaxDamage();
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        if (!isDead(stack)) {
            ToolMaterial material = getMaterial(stack, state.getBlock().getHarvestTool(state));
            if (material.getHarvestLevel() >= state.getBlock().getHarvestLevel(state)) {
                return material.getEfficiencyOnProperMaterial();
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        NBTTagCompound materials = getMaterialsTag(stack);
        int tr = 0;
        if (materials != null) {
            for (String type : materials.getKeySet()) {
                tr += getMaterial(stack, type).getMaxUses();
            }
        }
        return tr * USE;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag p_addInformation_4_) {
        super.addInformation(stack, world, list, p_addInformation_4_);
        NBTTagCompound materials = getMaterialsTag(stack);
        if (materials != null) {
            for (String type : materials.getKeySet()) {
                list.add(materials.getString(type) + " " + type);
            }
        }
    }

    public int getColorFromItemStack(ItemStack stack, int layer) {
        String[] layerTools = {"no", "sword", "pickaxe", "axe", "hoe", "shovel"};
        if (layerTools[layer].equals("no")) {
            return 0xFFFFFF;
        } else {
            ToolMaterial material = getMaterial(stack, layerTools[layer]);
            if (material != null) {
                switch (material) {
                    case WOOD:
                        return 0x866526;
                    case STONE:
                        return 0x9a9a9a;
                    case IRON:
                        return 0xffffff;
                    case GOLD:
                        return 0xeaee57;
                    case DIAMOND:
                        return 0x33ebcb;
                }
            }
            return 0;
        }
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase player) {
        if (!isDead(stack)) {
            stack.damageItem(USE, player);
        }
        return true;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (!isDead(stack)) {
            stack.damageItem(USE, attacker);
        }
        return true;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        int tr = 0;
        NBTTagCompound materials = getMaterialsTag(stack);
        if (materials != null) {
            for (String type : materials.getKeySet()) {
                tr += getMaterial(stack, type).getEnchantability();
            }
        }
        return tr;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> tr = super.getAttributeModifiers(slot, stack);
        tr.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", getMaterial(stack, "sword").getDamageVsEntity() + 4, 0));
        return tr;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World p_onItemUse_2_, BlockPos p_onItemUse_3_, EnumHand hand, EnumFacing p_onItemUse_5_, float p_onItemUse_6_, float p_onItemUse_7_, float p_onItemUse_8_) {
        ItemStack stack = player.getHeldItem(hand);
        if (!isDead(stack)) {
            EnumActionResult tr = Items.DIAMOND_HOE.onItemUse(player, p_onItemUse_2_, p_onItemUse_3_, hand, p_onItemUse_5_, p_onItemUse_6_, p_onItemUse_7_, p_onItemUse_8_);
            if (tr == EnumActionResult.SUCCESS) {
                stack.damageItem(USE - 1, player);
            }
            return tr;
        }
        return EnumActionResult.PASS;
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        NBTTagCompound materials = getMaterialsTag(stack);
        if (materials != null) {
            return materials.getKeySet();
        } else {
            return ImmutableSet.of();
        }
    }
}