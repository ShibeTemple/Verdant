package is.bradley.verdant.item;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public class SickleItem extends ToolItem {
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.GRASS);
    private static final Set<TagKey<Block>> EFFECTIVE_TAGS = Set.of(
            BlockTags.FLOWERS,
            BlockTags.CROPS,
            BlockTags.LEAVES,
            BlockTags.SAPLINGS
    );

    private final int radius;
    private final float miningSpeed;

    public SickleItem(ToolMaterial material, int radius, Settings settings) {
        super(material, settings);
        this.radius = radius;
        this.miningSpeed = material.getMiningSpeedMultiplier();
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public boolean isSuitableFor(BlockState state) {
        return isEffectiveOn(state);
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        if (isEffectiveOn(state)) {
            return this.miningSpeed;
        }
        return super.getMiningSpeedMultiplier(stack, state);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, e -> e.sendToolBreakStatus(Hand.MAIN_HAND));
        return true;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient && miner instanceof PlayerEntity player) {
            breakNeighbours(stack, world, pos, player);
        }
        stack.damage(1, miner, e -> e.sendToolBreakStatus(Hand.MAIN_HAND));
        return true;
    }

    private void breakNeighbours(ItemStack tool, World world, BlockPos pos, PlayerEntity player) {
        int blocksBroken = 0;

        for (BlockPos target : nearbyBlocks(tool, pos, world, player)) {
            BlockState state = world.getBlockState(target);

            if (isEffectiveOn(state)) {
                world.breakBlock(target, false);
                state.getBlock().afterBreak(world, player, target, state, world.getBlockEntity(target), tool);
                ++blocksBroken;
            }
        }
        
        if (blocksBroken > 0) {
            tool.damage(Math.round(blocksBroken / 2f), player, p -> p.sendToolBreakStatus(Hand.MAIN_HAND));
        }
    }

    private Set<BlockPos> nearbyBlocks(ItemStack tool, BlockPos pos, World world, PlayerEntity player) {
        Set<BlockPos> result = new HashSet<>();

        for (int y = -1; y < 2; y++) {
            for (int x = -radius; x < radius + 1; x++) {
                for (int z = -radius; z < radius + 1; z++) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }

                    BlockPos potential = pos.add(x, y, z);
                    BlockState state = world.getBlockState(potential);
                    
                    if (state.isIn(BlockTags.WITHER_IMMUNE)) {
                        continue;
                    }
                    
                    if (!player.getMainHandStack().isSuitableFor(state) && state.getHardness(world, potential) > 0) {
                        continue;
                    }

                    if (isEffectiveOn(state)) {
                        result.add(potential);
                    }
                }
            }
        }
        return result;
    }

    private boolean isEffectiveOn(BlockState state) {
        if (EFFECTIVE_ON.contains(state.getBlock())) {
            return true;
        }
        for (TagKey<Block> tag : EFFECTIVE_TAGS) {
            if (state.isIn(tag)) {
                return true;
            }
        }
        return false;
    }

    public Set<Block> getEffectiveBlocks() {
        return EFFECTIVE_ON;
    }

    public Set<TagKey<Block>> getEffectiveTags() {
        return EFFECTIVE_TAGS;
    }
}