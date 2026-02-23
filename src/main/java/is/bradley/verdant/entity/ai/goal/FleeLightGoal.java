package is.bradley.verdant.entity.ai.goal;

import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.LightType;

import java.util.EnumSet;

/**
 * Makes the entity flee to darker areas when light level is too high.
 * Ported from Alex's Mobs AnimalAIFleeLight.
 */
public class FleeLightGoal extends Goal {
    private final PathAwareEntity entity;
    private final double speed;
    private BlockPos targetDarkPos;

    public FleeLightGoal(PathAwareEntity entity, double speed) {
        this.entity = entity;
        this.speed = speed;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (entity.getWorld().isClient()) return false;
        BlockPos pos = entity.getBlockPos();
        int blockLight = entity.getWorld().getLightLevel(LightType.BLOCK, pos);
        int skyLight = entity.getWorld().getLightLevel(LightType.SKY, pos);
        if (Math.max(blockLight, skyLight) <= 0) return false;
        targetDarkPos = findDarkPos();
        return targetDarkPos != null;
    }

    @Override
    public boolean shouldContinue() {
        if (entity.getNavigation().isIdle()) return false;
        BlockPos pos = entity.getBlockPos();
        int blockLight = entity.getWorld().getLightLevel(LightType.BLOCK, pos);
        int skyLight = entity.getWorld().getLightLevel(LightType.SKY, pos);
        return Math.max(blockLight, skyLight) > 0;
    }

    @Override
    public void start() {
        if (targetDarkPos != null) {
            entity.getNavigation().startMovingTo(targetDarkPos.getX() + 0.5, targetDarkPos.getY(), targetDarkPos.getZ() + 0.5, speed);
        }
    }

    @Override
    public void stop() {
        entity.getNavigation().stop();
    }

    private BlockPos findDarkPos() {
        BlockPos origin = entity.getBlockPos();
        BlockPos best = null;
        int bestLight = Integer.MAX_VALUE;
        for (int attempts = 0; attempts < 10; attempts++) {
            int dx = entity.getRandom().nextInt(13) - 6;
            int dy = entity.getRandom().nextInt(5) - 2;
            int dz = entity.getRandom().nextInt(13) - 6;
            BlockPos candidate = origin.add(dx, dy, dz);
            if (!entity.getWorld().getBlockState(candidate).isSolidBlock(entity.getWorld(), candidate)) {
                BlockState floor = entity.getWorld().getBlockState(candidate.down());
                if (floor.isSolidBlock(entity.getWorld(), candidate.down())) {
                    int light = Math.max(
                            entity.getWorld().getLightLevel(LightType.BLOCK, candidate),
                            entity.getWorld().getLightLevel(LightType.SKY, candidate));
                    if (light < bestLight) {
                        bestLight = light;
                        best = candidate;
                    }
                }
            }
        }
        return (best != null && bestLight == 0) ? best : null;
    }
}
