package is.bradley.verdant.registry;

import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class ModToolMaterials {
    public static final ToolMaterial BONE = new ToolMaterial() {
        @Override
        public int getDurability() {
            return 250;
        }

        @Override
        public float getMiningSpeedMultiplier() {
            return 5.0f;
        }

        @Override
        public float getAttackDamage() {
            return 1.0f;
        }

        @Override
        public int getMiningLevel() {
            return 1;
        }

        @Override
        public int getEnchantability() {
            return 18;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.ofItems(Items.BONE);
        }
    };
}