package is.bradley.verdant.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;

public class FieryGlowParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    protected FieryGlowParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.spriteProvider = spriteProvider;
        this.scale *= 0.5F + this.random.nextFloat() * 0.5F;
        this.maxAge = 10 + this.random.nextInt(10);
        this.setSpriteForAge(spriteProvider);
        this.red = 1.0F;
        this.green = 0.6F + this.random.nextFloat() * 0.4F;
        this.blue = 0.2F;
        this.alpha = 0.8F;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            this.setSpriteForAge(this.spriteProvider);
            this.alpha = MathHelper.clamp(1.0F - (float) this.age / (float) this.maxAge, 0.0F, 1.0F) * 0.8F;
        }
    }

    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new FieryGlowParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider);
        }
    }
}