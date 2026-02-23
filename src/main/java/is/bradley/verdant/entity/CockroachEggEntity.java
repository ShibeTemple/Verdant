package is.bradley.verdant.entity;

import is.bradley.verdant.registry.ModEntities;
import is.bradley.verdant.registry.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class CockroachEggEntity extends ThrownItemEntity {

    public CockroachEggEntity(EntityType<? extends CockroachEggEntity> type, World world) {
        super(type, world);
    }

    public CockroachEggEntity(World world, double x, double y, double z) {
        super(ModEntities.COCKROACH_EGG, x, y, z, world);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.COCKROACH_OOTHECA;
    }

    @Override
    public void handleStatus(byte status) {
        if (status == 3) {
            for (int i = 0; i < 8; i++) {
                getWorld().addParticle(
                        new ItemStackParticleEffect(ParticleTypes.ITEM, getStack()),
                        getX(), getY(), getZ(),
                        (random.nextFloat() - 0.5) * 0.08,
                        (random.nextFloat() - 0.5) * 0.08,
                        (random.nextFloat() - 0.5) * 0.08
                );
            }
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!getWorld().isClient()) {
            ServerWorld serverWorld = (ServerWorld) getWorld();
            serverWorld.sendEntityStatus(this, (byte) 3);
            int count = random.nextInt(3);
            for (int i = 0; i < count; i++) {
                CockroachEntity cockroach = ModEntities.COCKROACH.create(serverWorld);
                if (cockroach != null) {
                    cockroach.setBreedingAge(-24000);
                    cockroach.refreshPositionAndAngles(getX(), getY(), getZ(), getYaw(), 0.0F);
                    cockroach.initialize(serverWorld, serverWorld.getLocalDifficulty(getBlockPos()), SpawnReason.TRIGGERED, null, null);
                    serverWorld.spawnEntity(cockroach);
                }
            }
            discard();
        }
    }
}
