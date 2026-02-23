package is.bradley.verdant.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.recipe.Ingredient;

public class BasicShieldItem extends ShieldItem {
    private final Ingredient repairIngredient;

    public BasicShieldItem(Settings settings, Item repairItem) {
        super(settings);
        this.repairIngredient = Ingredient.ofItems(repairItem);
    }

    public boolean isShield(ItemStack stack, LivingEntity entity) {
        return true;
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return this.repairIngredient.test(ingredient) || super.canRepair(stack, ingredient);
    }
}