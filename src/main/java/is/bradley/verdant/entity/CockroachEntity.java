package is.bradley.verdant.entity;

import is.bradley.verdant.VerdantConfig;
import is.bradley.verdant.entity.ai.goal.FleeLightGoal;
import is.bradley.verdant.entity.ai.goal.TargetItemsGoal;
import is.bradley.verdant.registry.ModEntities;
import is.bradley.verdant.registry.ModItems;
import is.bradley.verdant.registry.ModSounds;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.event.EntityPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.EntityGameEventHandler;
import net.minecraft.world.event.listener.GameEventListener;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

public class CockroachEntity extends AnimalEntity implements Shearable {

    // Tags
    private static final TagKey<net.minecraft.item.Item> COCKROACH_BREEDABLES =
            TagKey.of(RegistryKeys.ITEM, new Identifier("verdant", "cockroach_breedables"));
    private static final TagKey<net.minecraft.item.Item> COCKROACH_FOODSTUFFS =
            TagKey.of(RegistryKeys.ITEM, new Identifier("verdant", "cockroach_foodstuffs"));

    // Loot table IDs
    private static final Identifier MARACA_LOOT = new Identifier("verdant", "entities/cockroach_maracas");
    private static final Identifier MARACA_HEADLESS_LOOT = new Identifier("verdant", "entities/cockroach_maracas_headless");

    // Maraca dance duration in ticks (20 seconds)
    private static final int MARACA_DANCE_DURATION = 400;

    // Synced state
    private static final TrackedData<Boolean> DANCING =
            DataTracker.registerData(CockroachEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> HEADLESS =
            DataTracker.registerData(CockroachEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> MARACAS =
            DataTracker.registerData(CockroachEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Optional<UUID>> NEAREST_MUSICIAN =
            DataTracker.registerData(CockroachEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private static final TrackedData<Boolean> BREADED =
            DataTracker.registerData(CockroachEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    // Animation fields (accessed by model)
    public int randomWingFlapTick = 0;
    public float prevDanceProgress;
    public float danceProgress;

    // Private state
    private boolean prevStand = false;
    private BlockPos jukeboxPos;
    private int laCucarachaTimer = 0;
    private int maracaDanceTimer = 0;
    private int maracaCooldown = 0;
    public int timeUntilNextEgg;

    // Jukebox game event handler — registered/unregistered by the entity framework
    private final EntityGameEventHandler<JukeboxEventListener> jukeboxEventHandler;

    public CockroachEntity(EntityType<? extends CockroachEntity> type, World world) {
        super(type, world);
        this.timeUntilNextEgg = this.random.nextInt(24000) + 24000;
        this.jukeboxEventHandler = new EntityGameEventHandler<>(
                new JukeboxEventListener(new EntityPositionSource(this, this.getStandingEyeHeight()), 3)
        );
    }

    public static DefaultAttributeContainer.Builder createCockroachAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 6.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0);
    }

    // ==================== Jukebox game event listener ====================

    @Override
    public void updateEventHandler(BiConsumer<EntityGameEventHandler<?>, ServerWorld> callback) {
        World world = this.getWorld();
        if (world instanceof ServerWorld serverWorld) {
            callback.accept(this.jukeboxEventHandler, serverWorld);
        }
    }

    /**
     * Called by the inner JukeboxEventListener when a JUKEBOX_PLAY or JUKEBOX_STOP_PLAY
     * game event is received. Sets or clears the tracked jukebox position.
     */
    private void onJukeboxEvent(BlockPos pos, boolean playing) {
        if (playing) {
            this.jukeboxPos = pos;
        } else {
            if (pos.equals(this.jukeboxPos) || this.jukeboxPos == null) {
                this.jukeboxPos = null;
            }
        }
    }

    private class JukeboxEventListener implements GameEventListener {
        private final PositionSource positionSource;
        private final int range;

        JukeboxEventListener(PositionSource positionSource, int range) {
            this.positionSource = positionSource;
            this.range = range;
        }

        @Override
        public PositionSource getPositionSource() {
            return positionSource;
        }

        @Override
        public int getRange() {
            return range;
        }

        @Override
        public boolean listen(ServerWorld world, GameEvent event, GameEvent.Emitter emitter, Vec3d emitterPos) {
            if (event == GameEvent.JUKEBOX_PLAY) {
                CockroachEntity.this.onJukeboxEvent(BlockPos.ofFloored(emitterPos), true);
                return true;
            } else if (event == GameEvent.JUKEBOX_STOP_PLAY) {
                CockroachEntity.this.onJukeboxEvent(BlockPos.ofFloored(emitterPos), false);
                return true;
            }
            return false;
        }
    }

    // ==================== DataTracker ====================

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(DANCING, false);
        this.dataTracker.startTracking(HEADLESS, false);
        this.dataTracker.startTracking(MARACAS, false);
        this.dataTracker.startTracking(NEAREST_MUSICIAN, Optional.empty());
        this.dataTracker.startTracking(BREADED, false);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new FleeEntityGoal<>(this, PlayerEntity.class, 8.0F, 1.3, 1.0,
                target -> !this.isBreaded()));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0));
        this.goalSelector.add(3, new TemptGoal(this, 1.0, Ingredient.fromTag(COCKROACH_BREEDABLES), false));
        this.goalSelector.add(4, new FleeLightGoal(this, 1.0) {
            @Override
            public boolean canStart() {
                return !CockroachEntity.this.isBreaded() && super.canStart();
            }
        });
        this.goalSelector.add(5, new TargetItemsGoal(this));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0, 80));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    // ==================== Synced data accessors ====================

    public boolean isDancing() {
        return this.dataTracker.get(DANCING);
    }

    public void setDancing(boolean dancing) {
        this.dataTracker.set(DANCING, dancing);
    }

    public boolean isHeadless() {
        return this.dataTracker.get(HEADLESS);
    }

    public void setHeadless(boolean headless) {
        this.dataTracker.set(HEADLESS, headless);
    }

    public boolean hasMaracas() {
        return this.dataTracker.get(MARACAS);
    }

    public void setMaracas(boolean maracas) {
        this.dataTracker.set(MARACAS, maracas);
    }

    public boolean isBreaded() {
        return this.dataTracker.get(BREADED);
    }

    public void setBreaded(boolean breaded) {
        this.dataTracker.set(BREADED, breaded);
    }

    public UUID getNearestMusicianId() {
        return this.dataTracker.get(NEAREST_MUSICIAN).orElse(null);
    }

    public void setNearestMusician(UUID id) {
        this.dataTracker.set(NEAREST_MUSICIAN, Optional.ofNullable(id));
    }

    // ==================== NBT ====================

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("Maracas", this.hasMaracas());
        nbt.putBoolean("Dancing", this.isDancing());
        nbt.putBoolean("Breaded", this.isBreaded());
        nbt.putInt("EggTime", this.timeUntilNextEgg);
        nbt.putInt("MaracaTimer", this.maracaDanceTimer);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setMaracas(nbt.getBoolean("Maracas"));
        this.setDancing(nbt.getBoolean("Dancing"));
        this.setBreaded(nbt.getBoolean("Breaded"));
        if (nbt.contains("EggTime")) {
            this.timeUntilNextEgg = nbt.getInt("EggTime");
        }
        if (nbt.contains("MaracaTimer")) {
            this.maracaDanceTimer = nbt.getInt("MaracaTimer");
        }
    }

    // ==================== Entity properties ====================

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.ARTHROPOD;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.COCKROACH_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.COCKROACH_HURT;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.isOf(DamageTypes.FALL)
                || source.isOf(DamageTypes.DROWN)
                || source.isOf(DamageTypes.IN_WALL)
                || source.isOf(DamageTypes.FALLING_ANVIL)
                || source.isIn(TagKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("minecraft", "is_explosion")))
                || super.isInvulnerableTo(source);
    }

    @Override
    public float getPathfindingFavor(BlockPos pos, net.minecraft.world.WorldView world) {
        return 0.5F - Math.max(world.getLightLevel(LightType.BLOCK, pos), world.getLightLevel(LightType.SKY, pos));
    }

    @Override
    public boolean shouldDropXp() {
        return true;
    }

    @Override
    public boolean cannotDespawn() {
        return super.cannotDespawn() || this.hasCustomName() || this.isBreaded() || this.isDancing() || this.hasMaracas() || this.isHeadless();
    }

    @Override
    protected Identifier getLootTableId() {
        if (this.hasMaracas()) {
            return this.isHeadless() ? MARACA_HEADLESS_LOOT : MARACA_LOOT;
        }
        return super.getLootTableId();
    }

    // ==================== Interaction ====================

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.isOf(ModItems.MARACA) && this.isAlive() && !this.hasMaracas()) {
            this.setMaracas(true);
            this.maracaDanceTimer = 0;
            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }
            return ActionResult.success(this.getWorld().isClient());
        } else if (!stack.isOf(ModItems.MARACA) && this.isAlive() && this.hasMaracas()) {
            this.setMaracas(false);
            this.setDancing(false);
            this.maracaDanceTimer = 0;
            this.dropStack(new ItemStack(ModItems.MARACA));
            return ActionResult.SUCCESS;
        } else if (stack.isOf(Items.SHEARS) && this.isShearable()) {
            this.sheared(SoundCategory.PLAYERS);
            if (!player.getAbilities().creativeMode) {
                stack.damage(1, player, p -> p.sendToolBreakStatus(hand));
            }
            return ActionResult.success(this.getWorld().isClient());
        }
        return super.interactMob(player, hand);
    }

    // ==================== Tick ====================

    @Override
    public void tick() {
        super.tick();
        prevDanceProgress = danceProgress;
        final boolean dance = isDancing();

        // Dance progress animation
        if (dance) {
            if (danceProgress < 5F) danceProgress++;
        } else {
            if (danceProgress > 0F) danceProgress--;
        }

        // Random wing flap (when airborne or occasionally)
        if (!this.isOnGround() || random.nextInt(200) == 0) {
            randomWingFlapTick = 5 + random.nextInt(15);
        }
        if (randomWingFlapTick > 0) {
            randomWingFlapTick--;
        }

        // Refresh dimensions if dance state changed
        if (prevStand != dance) {
            if (hasMaracas()) {
                tellOthersImPlayingLaCucaracha();
            }
            this.calculateDimensions();
        }

        // Decrement maraca pickup cooldown
        if (!this.getWorld().isClient() && maracaCooldown > 0) {
            maracaCooldown--;
        }

        // Determine dance state (server authoritative, synced to client via TrackedData)
        if (!this.getWorld().isClient()) {
            if (hasMaracas()) {
                maracaDanceTimer++;
                if (maracaDanceTimer >= MARACA_DANCE_DURATION) {
                    // Timer expired: drop the maraca and stop dancing
                    this.dropStack(new ItemStack(ModItems.MARACA));
                    this.setMaracas(false);
                    this.setDancing(false);
                    this.maracaDanceTimer = 0;
                    this.maracaCooldown = 200; // 10 second cooldown before re-picking up
                    laCucarachaTimer = 0;
                } else {
                    // Still dancing: broadcast to nearby cockroaches
                    laCucarachaTimer++;
                    if (laCucarachaTimer % 20 == 0 && random.nextFloat() < 0.3F) {
                        tellOthersImPlayingLaCucaracha();
                    }
                    this.setDancing(true);
                    if (!this.isSilent()) {
                        this.getWorld().sendEntityStatus(this, (byte) 67);
                    }
                }
            } else if (jukeboxPos != null) {
                // Near a playing jukebox
                laCucarachaTimer = 0;
                maracaDanceTimer = 0;
                this.setDancing(true);
            } else {
                // No maracas, no jukebox: follow the nearest musician or stop
                laCucarachaTimer = 0;
                maracaDanceTimer = 0;
                Entity musician = this.getNearestMusician();
                if (musician != null) {
                    if (!musician.isAlive()
                            || this.distanceTo(musician) > 10
                            || (musician instanceof CockroachEntity rc && !rc.hasMaracas())) {
                        this.setNearestMusician(null);
                        this.setDancing(false);
                    } else {
                        this.setDancing(true);
                    }
                } else {
                    this.setDancing(false);
                }
            }
        }

        // Stop movement while dancing
        if (isDancing() || danceProgress > 0) {
            if (this.getNavigation().getCurrentPath() != null) {
                this.getNavigation().stop();
            }
        }

        // Egg laying
        if (!this.getWorld().isClient() && this.isAlive() && !this.isBaby() && --this.timeUntilNextEgg <= 0) {
            ItemEntity dropped = this.dropStack(new ItemStack(ModItems.COCKROACH_OOTHECA));
            if (dropped != null) {
                dropped.setPickupDelay(40);
            }
            this.timeUntilNextEgg = this.random.nextInt(24000) + 24000;
        }

        prevStand = dance;
    }

    private void tellOthersImPlayingLaCucaracha() {
        List<CockroachEntity> nearby = this.getWorld().getEntitiesByClass(
                CockroachEntity.class,
                this.getBoundingBox().expand(10, 10, 10),
                entity -> !entity.hasMaracas());
        for (CockroachEntity roach : nearby) {
            roach.setNearestMusician(this.getUuid());
        }
    }

    public Entity getNearestMusician() {
        UUID id = getNearestMusicianId();
        if (id != null && !this.getWorld().isClient()) {
            return ((ServerWorld) this.getWorld()).getEntity(id);
        }
        return null;
    }

    @Override
    public void handleStatus(byte status) {
        if (status == 67) {
            // Maraca clack sound - plays on client
            if (this.getWorld().isClient()) {
                this.getWorld().playSound(
                        this.getX(), this.getY(), this.getZ(),
                        SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.NEUTRAL,
                        0.5F, 1.8F + this.random.nextFloat() * 0.4F, false);
            }
        } else {
            super.handleStatus(status);
        }
    }

    // ==================== Breeding ====================

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isIn(COCKROACH_BREEDABLES);
    }

    @Override
    public CockroachEntity createChild(ServerWorld world, PassiveEntity other) {
        CockroachEntity baby = ModEntities.COCKROACH.create(world);
        if (baby != null) {
            baby.setBreaded(true);
        }
        return baby;
    }

    // ==================== Shearable ====================

    @Override
    public boolean isShearable() {
        return this.isAlive() && !this.isBaby() && !this.isHeadless();
    }

    @Override
    public void sheared(SoundCategory soundCategory) {
        this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_SHEEP_SHEAR, soundCategory, 1.0F, 1.0F);
        this.emitGameEvent(net.minecraft.world.event.GameEvent.ENTITY_INTERACT);
        this.damage(this.getDamageSources().generic(), 0F);
        if (!this.getWorld().isClient()) {
            for (int i = 0; i < 3; i++) {
                ((ServerWorld) this.getWorld()).spawnParticles(
                        ParticleTypes.SNEEZE,
                        randomBodyX(0.52), this.getBodyY(1.0), randomBodyZ(0.52),
                        1, 0, 0, 0, 0);
            }
        }
        this.setHeadless(true);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        boolean result = super.damage(source, amount);
        if (result) {
            randomWingFlapTick = 5 + random.nextInt(15);
            if (this.getHealth() <= 1.0F && amount > 0 && !this.isHeadless() && this.getRandom().nextInt(3) == 0) {
                this.setHeadless(true);
                if (!this.getWorld().isClient()) {
                    for (int i = 0; i < 3; i++) {
                        ((ServerWorld) this.getWorld()).spawnParticles(
                                ParticleTypes.SNEEZE,
                                randomBodyX(0.52), this.getBodyY(1.0), randomBodyZ(0.52),
                                1, 0, 0, 0, 0);
                    }
                }
            }
        }
        return result;
    }

    private double randomBodyX(double scale) {
        return this.getX() + (this.random.nextDouble() * 2.0 - 1.0) * this.getWidth() * scale * 0.5;
    }

    private double randomBodyZ(double scale) {
        return this.getZ() + (this.random.nextDouble() * 2.0 - 1.0) * this.getWidth() * scale * 0.5;
    }

    // ==================== Item pickup callback ====================

    public boolean canTargetItem(ItemStack stack) {
        if (stack.isOf(ModItems.MARACA)) {
            return VerdantConfig.get().cockroachMaracaPickup && maracaCooldown <= 0;
        }
        return stack.getItem().isFood() || stack.isIn(COCKROACH_BREEDABLES);
    }

    public void onGetItem(net.minecraft.entity.ItemEntity e) {
        if (e.getStack().isOf(ModItems.MARACA)) {
            this.setMaracas(true);
            this.maracaDanceTimer = 0;
        } else {
            this.heal(5);
            if (e.getStack().isIn(COCKROACH_FOODSTUFFS)) {
                this.setBreaded(true);
            }
        }
    }

    // ==================== Spawn ====================

    public static boolean canSpawn(EntityType<CockroachEntity> type,
                                    net.minecraft.world.ServerWorldAccess world,
                                    SpawnReason reason,
                                    BlockPos pos,
                                    net.minecraft.util.math.random.Random random) {
        if (reason == SpawnReason.SPAWNER) return true;
        if (world.isSkyVisible(pos)) return false;
        if (pos.getY() > 64) return false;
        int skyLight = world.getLightLevel(LightType.SKY, pos);
        if (skyLight > random.nextInt(32)) return false;
        int rawLight = world.getBaseLightLevel(pos, 0);
        return rawLight <= random.nextInt(8);
    }
}
