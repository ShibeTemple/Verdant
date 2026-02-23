package is.bradley.verdant.entity.ai.goal;

import is.bradley.verdant.entity.CockroachEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.predicate.entity.EntityPredicates;

import java.util.EnumSet;
import java.util.List;

/**
 * Makes cockroaches search for and pick up nearby dropped items.
 * Ported from Alex's Mobs CreatureAITargetItems.
 */
public class TargetItemsGoal extends Goal {
    private final CockroachEntity entity;
    private ItemEntity targetItem;

    public TargetItemsGoal(CockroachEntity entity) {
        this.entity = entity;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (entity.getWorld().isClient()) return false;
        if (entity.isDancing()) return false;
        List<ItemEntity> nearby = entity.getWorld().getEntitiesByClass(
                ItemEntity.class,
                entity.getBoundingBox().expand(8.0),
                EntityPredicates.VALID_ENTITY);
        if (nearby.isEmpty()) return false;
        for (ItemEntity item : nearby) {
            if (entity.canTargetItem(item.getStack())) {
                targetItem = item;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldContinue() {
        if (targetItem == null || !targetItem.isAlive()) return false;
        if (!entity.canTargetItem(targetItem.getStack())) return false;
        if (entity.isDancing()) return false;
        return entity.squaredDistanceTo(targetItem) > 1.0;
    }

    @Override
    public void start() {
        if (targetItem != null) {
            entity.getLookControl().lookAt(targetItem, 10.0F, (float) entity.getMaxLookPitchChange());
        }
    }

    @Override
    public void tick() {
        if (targetItem == null) return;
        entity.getLookControl().lookAt(targetItem, 10.0F, (float) entity.getMaxLookPitchChange());
        entity.getNavigation().startMovingTo(targetItem, 1.1);
        if (entity.squaredDistanceTo(targetItem) < 1.5) {
            entity.onGetItem(targetItem);
            targetItem.discard();
            targetItem = null;
        }
    }

    @Override
    public void stop() {
        targetItem = null;
        entity.getNavigation().stop();
    }
}
