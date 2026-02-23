package is.bradley.verdant.registry;

import is.bradley.verdant.Verdant;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class ModArmorMaterials {
    public static final ArmorMaterial BONE = new ArmorMaterial() {
        // Durability per armor type (HELMET, CHESTPLATE, LEGGINGS, BOOTS)
        private static final int[] DURABILITY = {66, 96, 90, 78};
        // Protection per armor type (HELMET, CHESTPLATE, LEGGINGS, BOOTS)
        private static final int[] PROTECTION = {1, 4, 5, 2};

        @Override
        public int getDurability(ArmorItem.Type type) {
            return DURABILITY[type.ordinal()];
        }

        @Override
        public int getProtection(ArmorItem.Type type) {
            return PROTECTION[type.ordinal()];
        }

        @Override
        public int getEnchantability() {
            return 18;
        }

        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ITEM_ARMOR_EQUIP_GOLD;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.ofItems(Items.BONE);
        }

        @Override
        public String getName() {
            return Verdant.MOD_ID + ":bone";
        }

        @Override
        public float getToughness() {
            return 0.0f;
        }

        @Override
        public float getKnockbackResistance() {
            return 0.0f;
        }
    };

    public static void register() {
        // Materials are registered via static initializers
    }
}