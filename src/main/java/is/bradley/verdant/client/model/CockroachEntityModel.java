package is.bradley.verdant.client.model;

import is.bradley.verdant.entity.CockroachEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

/**
 * Cockroach entity model, ported from Alex's Mobs Forge to Fabric.
 * Original used Citadel's AdvancedEntityModel / AdvancedModelBox.
 * Animation helpers (walk, swing, flap, bob, progressRotation, progressPosition)
 * are reimplemented here using vanilla ModelPart.
 */
public class CockroachEntityModel extends SinglePartEntityModel<CockroachEntity> {

    // Default pivot values (for resetPose)
    private static final float[] ABDOMEN_DEF_PIVOT    = { 0.0F, -1.6F, -1.0F };
    private static final float[] LLF_DEF_ROT          = { 0.0F, 0.0F, 0.1309F };
    private static final float[] RLF_DEF_ROT          = { 0.0F, 0.0F, -0.1309F };
    private static final float[] LLB_DEF_ROT          = { -0.0436F, -0.5236F, 0.1745F };
    private static final float[] RLB_DEF_ROT          = { -0.0436F,  0.5236F, -0.1745F };
    private static final float[] LLM_DEF_ROT          = { -0.0436F, -0.2182F, 0.1309F };
    private static final float[] RLM_DEF_ROT          = { -0.0436F,  0.2182F, -0.1309F };
    private static final float[] LANT_DEF_ROT         = { -0.2182F, -0.2618F, 0.1309F };
    private static final float[] RANT_DEF_ROT         = { -0.2182F,  0.2618F, -0.1309F };

    private final net.minecraft.client.model.ModelPart root;
    public final net.minecraft.client.model.ModelPart abdomen;
    public final net.minecraft.client.model.ModelPart leftLegFront;
    public final net.minecraft.client.model.ModelPart rightLegFront;
    public final net.minecraft.client.model.ModelPart leftLegBack;
    public final net.minecraft.client.model.ModelPart rightLegBack;
    public final net.minecraft.client.model.ModelPart leftLegMid;
    public final net.minecraft.client.model.ModelPart rightLegMid;
    public final net.minecraft.client.model.ModelPart leftWing;
    public final net.minecraft.client.model.ModelPart rightWing;
    public final net.minecraft.client.model.ModelPart neck;
    public final net.minecraft.client.model.ModelPart head;
    public final net.minecraft.client.model.ModelPart leftAntenna;
    public final net.minecraft.client.model.ModelPart rightAntenna;

    public CockroachEntityModel(net.minecraft.client.model.ModelPart root) {
        // The baked root from EntityModelLoader is modelData.getRoot()
        // Our actual root bone is a child named "root"
        this.root = root.getChild("root");
        this.abdomen = this.root.getChild("abdomen");
        this.leftLegFront  = this.abdomen.getChild("left_leg_front");
        this.rightLegFront = this.abdomen.getChild("right_leg_front");
        this.leftLegBack   = this.abdomen.getChild("left_leg_back");
        this.rightLegBack  = this.abdomen.getChild("right_leg_back");
        this.leftLegMid    = this.abdomen.getChild("left_leg_mid");
        this.rightLegMid   = this.abdomen.getChild("right_leg_mid");
        this.leftWing      = this.abdomen.getChild("left_wing");
        this.rightWing     = this.abdomen.getChild("right_wing");
        this.neck          = this.abdomen.getChild("neck");
        this.head          = this.neck.getChild("head");
        this.leftAntenna   = this.head.getChild("left_antenna");
        this.rightAntenna  = this.head.getChild("right_antenna");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData partData = modelData.getRoot();

        // Root bone at ground level (Y=24 in model space = entity's feet)
        ModelPartData root = partData.addChild("root",
                ModelPartBuilder.create(),
                ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        // Abdomen – the body
        ModelPartData abdomen = root.addChild("abdomen",
                ModelPartBuilder.create().uv(0, 12).cuboid(-2.0F, -0.9F, -2.0F, 4, 2, 9),
                ModelTransform.pivot(0.0F, -1.6F, -1.0F));

        // Legs
        abdomen.addChild("left_leg_front",
                ModelPartBuilder.create().uv(0, 24).cuboid(0.0F, 0.0F, 0.0F, 7, 0, 3),
                ModelTransform.of(1.5F, 0.6F, -2.0F, 0.0F, 0.0F, 0.1309F));

        abdomen.addChild("right_leg_front",
                ModelPartBuilder.create().uv(0, 24).mirrored().cuboid(-7.0F, 0.0F, 0.0F, 7, 0, 3),
                ModelTransform.of(-1.5F, 0.6F, -2.0F, 0.0F, 0.0F, -0.1309F));

        abdomen.addChild("left_leg_back",
                ModelPartBuilder.create().uv(18, 12).cuboid(0.0F, 0.0F, 0.0F, 7, 0, 5),
                ModelTransform.of(1.5F, 0.6F, 3.0F, -0.0436F, -0.5236F, 0.1745F));

        abdomen.addChild("right_leg_back",
                ModelPartBuilder.create().uv(18, 12).mirrored().cuboid(-7.0F, 0.0F, 0.0F, 7, 0, 5),
                ModelTransform.of(-1.5F, 0.6F, 3.0F, -0.0436F, 0.5236F, -0.1745F));

        abdomen.addChild("left_leg_mid",
                ModelPartBuilder.create().uv(23, 20).cuboid(0.0F, 0.0F, 0.0F, 7, 0, 4),
                ModelTransform.of(1.5F, 0.6F, 0.0F, -0.0436F, -0.2182F, 0.1309F));

        abdomen.addChild("right_leg_mid",
                ModelPartBuilder.create().uv(23, 20).mirrored().cuboid(-7.0F, 0.0F, 0.0F, 7, 0, 4),
                ModelTransform.of(-1.5F, 0.6F, 0.0F, -0.0436F, 0.2182F, -0.1309F));

        // Wings
        abdomen.addChild("left_wing",
                ModelPartBuilder.create().uv(0, 0).cuboid(0.0F, 0.0F, 0.0F, 3, 1, 10),
                ModelTransform.pivot(0.0F, -1.4F, -2.0F));

        abdomen.addChild("right_wing",
                ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-3.0F, 0.0F, 0.0F, 3, 1, 10),
                ModelTransform.pivot(0.0F, -1.4F, -2.0F));

        // Neck
        ModelPartData neck = abdomen.addChild("neck",
                ModelPartBuilder.create().uv(21, 25).cuboid(-2.5F, -1.6F, -2.0F, 5, 3, 2),
                ModelTransform.pivot(0.0F, 0.0F, -2.0F));

        // Head
        ModelPartData head = neck.addChild("head",
                ModelPartBuilder.create().uv(0, 28).cuboid(-1.5F, -1.0F, -2.0F, 3, 2, 2),
                ModelTransform.pivot(0.0F, -0.1F, -2.0F));

        // Antennae
        head.addChild("left_antenna",
                ModelPartBuilder.create().uv(17, 0).cuboid(0.0F, 0.0F, -8.0F, 5, 0, 8),
                ModelTransform.of(0.1F, -1.0F, -2.0F, -0.2182F, -0.2618F, 0.1309F));

        head.addChild("right_antenna",
                ModelPartBuilder.create().uv(17, 0).mirrored().cuboid(-5.0F, 0.0F, -8.0F, 5, 0, 8),
                ModelTransform.of(-0.1F, -1.0F, -2.0F, -0.2182F, 0.2618F, -0.1309F));

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public net.minecraft.client.model.ModelPart getPart() {
        return root;
    }

    // ==================== Animation ====================

    @Override
    public void setAngles(CockroachEntity entity, float limbAngle, float limbDistance,
                          float animationProgress, float headYaw, float headPitch) {
        resetPose();

        float partialTick = MinecraftClient.getInstance().getTickDelta();
        float danceProgress = entity.prevDanceProgress
                + (entity.danceProgress - entity.prevDanceProgress) * partialTick;

        // --- Dance pose ---
        progressRotation(abdomen,      danceProgress, rad(-70), 0, 0, 5F);
        progressRotation(leftLegFront, danceProgress, 0, rad(-10), 0, 5F);
        progressRotation(rightLegFront,danceProgress, 0, rad( 10), 0, 5F);
        progressRotation(leftLegMid,   danceProgress, 0, rad(-10), 0, 5F);
        progressRotation(rightLegMid,  danceProgress, 0, rad( 10), 0, 5F);
        progressPosition(abdomen, danceProgress, 0, -15, 2, 5F);

        if (danceProgress > 0) {
            // Antenna waggle while dancing
            walk(leftAntenna,  0.5F, 0.5F, false, -1, -0.05F, animationProgress, 1);
            walk(rightAntenna, 0.5F, 0.5F, false, -1, -0.05F, animationProgress, 1);

            if (entity.hasMaracas()) {
                // Full maraca dance
                swing(abdomen, 0.5F, 0.15F, false, 0, 0F, animationProgress, 1);
                flap(abdomen,  0.5F, 0.15F, false, 1, 0F, animationProgress, 1);
                bob(abdomen,   0.25F, 10F, true, animationProgress, 1);
                swing(rightLegFront, 0.5F, 0.5F, false, 0, -0.05F, animationProgress, 1);
                swing(leftLegFront,  0.5F, 0.5F, false, 0, -0.05F, animationProgress, 1);
                swing(rightLegMid,   0.5F, 0.5F, false, 2, -0.05F, animationProgress, 1);
                swing(leftLegMid,    0.5F, 0.5F, false, 2, -0.05F, animationProgress, 1);
            } else {
                // Spinny dance (no maracas)
                float spinDeg = MathHelper.wrapDegrees(animationProgress * 15F);
                abdomen.yaw = (float) (Math.toRadians(spinDeg) * danceProgress * 0.2F);
                bob(abdomen, 0.25F, 10F, true, animationProgress, 1);
            }
        }

        // --- Idle antenna sway ---
        swing(leftAntenna,  0.25F, 0.25F, true,  1, -0.1F, animationProgress, 1);
        swing(rightAntenna, 0.25F, 0.25F, false, 1, -0.1F, animationProgress, 1);
        walk(leftAntenna,   0.25F, 0.0625F, false, -1, -0.05F, animationProgress, 1);
        walk(rightAntenna,  0.25F, 0.0625F, false, -1, -0.05F, animationProgress, 1);
        walk(head,          0.125F, 0.125F, false, 0, 0.1F, animationProgress, 1);

        // --- Wing flap when airborne ---
        if (entity.randomWingFlapTick > 0) {
            swing(leftWing,  1.65F, 0.3F, true,  0, -0.2F, animationProgress, 1);
            swing(rightWing, 1.65F, 0.3F, false, 0, -0.2F, animationProgress, 1);
        }

        // --- Walk cycle ---
        swing(rightLegFront, 1.25F, 0.5F, false, -1, 0F, limbAngle, limbDistance);
        swing(rightLegBack,  1.25F, 0.5F, false,  1, 0F, limbAngle, limbDistance);
        swing(leftLegMid,    1.25F, 0.5F, false,  0, 0F, limbAngle, limbDistance);
        bob(abdomen,         1.25F, 1.25F, true, limbAngle, limbDistance);
        swing(leftLegFront,  1.25F, 0.5F, true,   1, 0F, limbAngle, limbDistance);
        swing(leftLegBack,   1.25F, 0.5F, true,  -1, 0F, limbAngle, limbDistance);
        swing(rightLegMid,   1.25F, 0.5F, true,   0, 0F, limbAngle, limbDistance);

        // --- Head tracking ---
        head.pitch += headPitch * (MathHelper.PI / 180F);
        head.yaw   += headYaw   * (MathHelper.PI / 180F);

        // --- Visibility states ---
        head.visible        = !entity.isHeadless();
        leftAntenna.visible = !entity.isHeadless();
        rightAntenna.visible= !entity.isHeadless();
        leftWing.visible    = !entity.isBaby();
        rightWing.visible   = !entity.isBaby();
    }

    private void resetPose() {
        // Abdomen
        abdomen.pitch = 0; abdomen.yaw = 0; abdomen.roll = 0;
        abdomen.pivotX = 0; abdomen.pivotY = ABDOMEN_DEF_PIVOT[1]; abdomen.pivotZ = ABDOMEN_DEF_PIVOT[2];

        // Left leg front
        leftLegFront.pitch = LLF_DEF_ROT[0]; leftLegFront.yaw = LLF_DEF_ROT[1]; leftLegFront.roll = LLF_DEF_ROT[2];
        // Right leg front
        rightLegFront.pitch = RLF_DEF_ROT[0]; rightLegFront.yaw = RLF_DEF_ROT[1]; rightLegFront.roll = RLF_DEF_ROT[2];
        // Left leg back
        leftLegBack.pitch = LLB_DEF_ROT[0]; leftLegBack.yaw = LLB_DEF_ROT[1]; leftLegBack.roll = LLB_DEF_ROT[2];
        // Right leg back
        rightLegBack.pitch = RLB_DEF_ROT[0]; rightLegBack.yaw = RLB_DEF_ROT[1]; rightLegBack.roll = RLB_DEF_ROT[2];
        // Left leg mid
        leftLegMid.pitch = LLM_DEF_ROT[0]; leftLegMid.yaw = LLM_DEF_ROT[1]; leftLegMid.roll = LLM_DEF_ROT[2];
        // Right leg mid
        rightLegMid.pitch = RLM_DEF_ROT[0]; rightLegMid.yaw = RLM_DEF_ROT[1]; rightLegMid.roll = RLM_DEF_ROT[2];

        // Wings
        leftWing.pitch = 0; leftWing.yaw = 0; leftWing.roll = 0;
        rightWing.pitch = 0; rightWing.yaw = 0; rightWing.roll = 0;

        // Neck / head
        neck.pitch = 0; neck.yaw = 0; neck.roll = 0;
        head.pitch = 0; head.yaw = 0; head.roll = 0;

        // Antennae
        leftAntenna.pitch  = LANT_DEF_ROT[0]; leftAntenna.yaw  = LANT_DEF_ROT[1]; leftAntenna.roll  = LANT_DEF_ROT[2];
        rightAntenna.pitch = RANT_DEF_ROT[0]; rightAntenna.yaw = RANT_DEF_ROT[1]; rightAntenna.roll = RANT_DEF_ROT[2];

        // Visibility defaults
        head.visible = true;
        leftAntenna.visible = true;
        rightAntenna.visible = true;
        leftWing.visible = true;
        rightWing.visible = true;
    }

    // ==================== Animation helpers ====================

    /** Sine-based walk (pitch). invert negates. offset shifts phase by offset*PI. */
    private static void walk(net.minecraft.client.model.ModelPart part, float speed, float degree,
                              boolean invert, float offset, float staticOffset,
                              float swing, float swingAmount) {
        part.pitch += MathHelper.cos(swing * speed + offset * MathHelper.PI) * degree
                * (invert ? -1 : 1) * swingAmount + staticOffset;
    }

    /** Cosine-based swing (yaw). */
    private static void swing(net.minecraft.client.model.ModelPart part, float speed, float degree,
                               boolean invert, float offset, float staticOffset,
                               float swing, float swingAmount) {
        part.yaw += MathHelper.cos(swing * speed + offset * MathHelper.PI) * degree
                * (invert ? -1 : 1) * swingAmount + staticOffset;
    }

    /** Cosine-based flap (roll). */
    private static void flap(net.minecraft.client.model.ModelPart part, float speed, float degree,
                              boolean invert, float offset, float staticOffset,
                              float swing, float swingAmount) {
        part.roll += MathHelper.cos(swing * speed + offset * MathHelper.PI) * degree
                * (invert ? -1 : 1) * swingAmount + staticOffset;
    }

    /** Sine-based vertical bob (pivotY). up=true means bob upward. */
    private static void bob(net.minecraft.client.model.ModelPart part, float speed, float degree,
                             boolean up, float swing, float swingAmount) {
        part.pivotY += MathHelper.sin(swing * speed) * degree * (up ? -1 : 1) * swingAmount;
    }

    /** Interpolate rotation toward target by progress/maxProgress fraction. */
    private static void progressRotation(net.minecraft.client.model.ModelPart part,
                                          float progress, float targetPitch, float targetYaw, float targetRoll,
                                          float maxProgress) {
        float t = progress / maxProgress;
        part.pitch += t * targetPitch;
        part.yaw   += t * targetYaw;
        part.roll  += t * targetRoll;
    }

    /** Interpolate pivot toward target by progress/maxProgress fraction. */
    private static void progressPosition(net.minecraft.client.model.ModelPart part,
                                          float progress, float x, float y, float z,
                                          float maxProgress) {
        float t = progress / maxProgress;
        part.pivotX += t * x;
        part.pivotY += t * y;
        part.pivotZ += t * z;
    }

    private static float rad(float degrees) {
        return (float) Math.toRadians(degrees);
    }
}
