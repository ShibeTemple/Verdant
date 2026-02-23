package is.bradley.verdant.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.World;

public class OreXPBlock extends Block {
    private final IntProvider experience;

    public OreXPBlock(Settings settings, IntProvider experience) {
        super(settings);
        this.experience = experience;
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, net.minecraft.block.entity.BlockEntity blockEntity, ItemStack stack) {
        super.afterBreak(world, player, pos, state, blockEntity, stack);
        if (EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, stack) == 0) {
            int i = this.experience.get(world.random);
            if (i > 0 && world instanceof ServerWorld serverWorld) {
                ExperienceOrbEntity.spawn(serverWorld, Vec3d.ofCenter(pos), i);
            }
        }
    }
}