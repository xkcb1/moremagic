package com.custom.magic.armor;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;


class CustomArmorMaterial implements ArmorMaterial {
    private static final int[] BASE_DURABILITY = new int[] {13, 15, 16, 11};
    private static final int[] PROTECTION_VALUES = new int[] {1,1,1,1};

    @Override
    public int getDurability(ArmorItem.Type type) {
        return BASE_DURABILITY[type.getEquipmentSlot().getEntitySlotId()];
    }

    @Override
    public int getProtection(ArmorItem.Type type) {
        return PROTECTION_VALUES[type.getEquipmentSlot().getEntitySlotId()];
    }

    @Override
    public int getEnchantability() {
        return 2;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_IRON;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(Items.IRON_INGOT);
    }

    @Override
    public String getName() {
        // Must be all lowercase
        return "magic_armor";
    }

    @Override
    public float getToughness() {
        return 1.0F;
    }

    @Override
    public float getKnockbackResistance() {
        return 0.1F;
    }
}
public class MagicArmor {
    // 创建盔甲材料
    public static final ArmorMaterial CUSTOM_ARMOR_MATERIAL = new CustomArmorMaterial();
    // 创建并注册盔甲物品
    public static final Item CUSTOM_HELMET_Leather = new ArmorItem(CUSTOM_ARMOR_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings());
    public static final Item CUSTOM_CHESTPLATE_Leather = new ArmorItem(CUSTOM_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings());
    public static final Item CUSTOM_LEGGINGS_Leather = new ArmorItem(CUSTOM_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings());
    public static final Item CUSTOM_BOOTS_Leather = new ArmorItem(CUSTOM_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings());

    // 注册盔甲物品
    public static void registerItems() {
        Registry.register(Registries.ITEM, new Identifier("magic", "custom_helmet_leather"), CUSTOM_HELMET_Leather);
        Registry.register(Registries.ITEM, new Identifier("magic", "custom_chestplate_leather"), CUSTOM_CHESTPLATE_Leather);
        Registry.register(Registries.ITEM, new Identifier("magic", "custom_leggings_leather"), CUSTOM_LEGGINGS_Leather);
        Registry.register(Registries.ITEM, new Identifier("magic", "custom_boots_leather"), CUSTOM_BOOTS_Leather);
    }

}
