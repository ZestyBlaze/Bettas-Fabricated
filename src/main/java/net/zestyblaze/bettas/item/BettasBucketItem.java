package net.zestyblaze.bettas.item;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidFillable;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Supplier;

public class BettasBucketItem extends BucketItem {
    private final Supplier<EntityType<?>> entityType;
    private final boolean hasTooltip;
    private final Fluid fluid;
    private final Item item;

    public BettasBucketItem(Supplier<EntityType<?>> entityType, Fluid fluid, Item item, boolean hasTooltip, Settings settings) {
        super(fluid, settings);
        this.entityType = entityType;
        this.fluid = fluid;
        this.hasTooltip = hasTooltip;
        this.item = item;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        BlockHitResult rayTraceResult = raycast(world, user, RaycastContext.FluidHandling.NONE);
        if(rayTraceResult.getType() == HitResult.Type.MISS) {
            return TypedActionResult.pass(itemStack);
        } else if(rayTraceResult.getType() != HitResult.Type.BLOCK) {
            return TypedActionResult.pass(itemStack);
        } else {
            BlockPos blockPos = rayTraceResult.getBlockPos();
            Direction direction = rayTraceResult.getSide();
            BlockPos blockPos1 = blockPos.offset(direction);
            if(user.canPlaceOn(blockPos1, direction, itemStack)) {
                BlockState blockState = world.getBlockState(blockPos);
                BlockPos blockPos2 = blockState.getBlock() instanceof FluidFillable && ((FluidFillable) blockState.getBlock()).canFillWithFluid(world, blockPos, blockState, fluid) ? blockPos : blockPos1;
                this.placeFluid(user, world, blockPos2, rayTraceResult);
                if(world instanceof ServerWorld) this.placeEntity((ServerWorld)world, itemStack, blockPos2);
                if(user instanceof ServerPlayerEntity) {
                    Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity)user, blockPos2, itemStack);
                }
                user.incrementStat(Stats.USED.getOrCreateStat(this));
                return TypedActionResult.success(itemStack);
            } else {
                return TypedActionResult.fail(itemStack);
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if(hasTooltip && stack.hasNbt()) {
            assert stack.getNbt() != null;
            tooltip.add(new TranslatableText(getEntityType().getTranslationKey() + "." + stack.getNbt().getInt("Variant")).formatted(Formatting.GRAY, Formatting.ITALIC));
        }
    }

    private void placeEntity(ServerWorld world, ItemStack stack, BlockPos pos) {
        Entity entity = this.entityType.get().spawnFromItemStack(world, stack, null, pos, SpawnReason.BUCKET, true, false);
        if(entity != null) {
            if(entity instanceof FishEntity) {
                ((FishEntity)entity).setFromBucket(true);
            }
        }
    }

    private EntityType<?> getEntityType() {
        return entityType.get();
    }
}
