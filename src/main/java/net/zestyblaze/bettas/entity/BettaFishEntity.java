package net.zestyblaze.bettas.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.zestyblaze.bettas.registry.BettaBlocksInit;
import net.zestyblaze.bettas.registry.BettaItemsInit;
import org.jetbrains.annotations.Nullable;

public class BettaFishEntity extends FishEntity implements Bucketable {
    public static final int MAX_VARIANTS = 120;
    private static final TrackedData<Integer> VARIANT = DataTracker.registerData(BettaFishEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> FROM_BUCKET = DataTracker.registerData(BettaFishEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public BettaFishEntity(EntityType<? extends FishEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new MeleeAttackGoal(this, 2.0d, true));
        this.goalSelector.add(1, new SwimAroundGoal(this, 1.0d, 20));
        this.goalSelector.add(0, new RevengeGoal(this, BettaFishEntity.class));
    }

    @Override
    public boolean isFromBucket() {
        return this.dataTracker.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.dataTracker.set(FROM_BUCKET, fromBucket);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VARIANT, 0);
        this.dataTracker.startTracking(FROM_BUCKET, false);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 6.0d).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0d);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if(this.isInvulnerableTo(source)) {
            return false;
        } else {
            Entity entity = source.getAttacker();
            if(entity != null && !(entity instanceof PlayerEntity) && !(entity instanceof ArrowEntity)) {
                amount = (amount + 1.0f) / 2.0f;
            }
            return super.damage(source, amount);
        }
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean flag = target.damage(DamageSource.mob(this), (float)((int)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)));
        if(flag) {
            this.applyDamageEffects(this, target);
        }
        return flag;
    }

    public int getVariant() {
        return this.dataTracker.get(VARIANT);
    }

    private void setVariant(int variant) {
        this.dataTracker.set(VARIANT, variant);
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return !this.isFromBucket() && !this.hasCustomName();
    }

    @Override
    public boolean cannotDespawn() {
        return super.cannotDespawn() || this.isFromBucket();
    }

    @Override
    public void tick() {
        super.tick();
        if(isMossBallNearby()) {
            this.setTarget(null);
        }
    }

    private boolean isMossBallNearby() {
        BlockPos blockPos = this.getBlockPos();
        BlockPos.Mutable blockPos$mutable = new BlockPos.Mutable();

        for(int i = 0; i < 8; ++i) {
            for(int j = 0; j < 8; ++j) {
                for (int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                    for (int l = k < j && k > -j ? j : 0; l <= j; l = l > 0 ? -l : 1 - l) {
                        blockPos$mutable.set(blockPos, k, i, j);
                        if(this.world.getBlockState(blockPos$mutable).isOf(BettaBlocksInit.MOSS_BALL_BLOCK)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Variant", this.getVariant());
        nbt.putBoolean("FromBucket", this.isFromBucket());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        setVariant(MathHelper.clamp(nbt.getInt("Variant"), 0, MAX_VARIANTS - 1));
        this.setFromBucket(nbt.getBoolean("FromBucket"));
    }

    @Override
    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt) {
        if(entityNbt == null) {
            double chance = getRandom().nextDouble();
            if(chance <= 0.45) setVariant(getRandom().nextInt(MAX_VARIANTS));
            else if(chance <= 0.7) setVariant(24);
            else setVariant(100);
        } else {
            if(entityNbt.contains("Variant", 3)) {
                this.setVariant(entityNbt.getInt("Variant"));
                this.targetSelector.add(1, new ActiveTargetGoal<>(this, BettaFishEntity.class, false));
            }
            if(entityNbt.contains("Health", 99)) {
                this.setHealth(entityNbt.getFloat("Health"));
            }
        }
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public void copyDataToStack(ItemStack bucket) {
        NbtCompound nbtCompound = bucket.getOrCreateNbt();
        nbtCompound.putInt("Variant", this.getVariant());
        nbtCompound.putFloat("Health", this.getHealth());
    }

    @Override
    public ItemStack getBucketItem() {
        return null;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_COD_FLOP;
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_COD_DEATH;
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_COD_HURT;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        float maxHealth = this.getMaxHealth();
        float health = this.getHealth();
        if(stack.getItem() == BettaItemsInit.BLACKWATER_BOTTLE && health < maxHealth) {
            if(!player.isCreative()) {
                stack.decrement(1);
            }
            heal(3);
            double d0 = this.random.nextGaussian() * 0.02d;
            double d1 = this.random.nextGaussian() * 0.02d;
            double d2 = this.random.nextGaussian() * 0.02d;
            this.world.addParticle(ParticleTypes.HEART, this.getParticleX(1.0d), this.getRandomBodyY() + 0.5d, this.getParticleZ(1.0d), d0, d1, d2);
            return ActionResult.PASS;
        }
        return interactMob(player, hand);
    }
}
