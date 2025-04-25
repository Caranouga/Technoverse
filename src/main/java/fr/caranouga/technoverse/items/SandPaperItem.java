package fr.caranouga.technoverse.items;

import fr.caranouga.technoverse.recipes.sanding.SandingRecipe;
import fr.caranouga.technoverse.recipes.sanding.SandingRecipeInput;
import fr.caranouga.technoverse.registry.ModDataComponents;
import fr.caranouga.technoverse.registry.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * This class is heavily inspired by the <a href="https://github.com/Creators-of-Create/Create/blob/mc1.21.1/dev/src/main/java/com/simibubi/create/content/equipment/sandPaper/SandPaperItem.java">SandPaperItem</a> item from Create.
 * Thank you to the Create team for their amazing work!
 */
public class SandPaperItem extends Item {
    public SandPaperItem() {
        super(new Item.Properties().durability(16));
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);

        if (itemstack.has(ModDataComponents.POLISHING.get())) {
            pPlayer.startUsingItem(pUsedHand);
            return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
        }

        InteractionHand otherHand = pUsedHand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack itemInOtherHand = pPlayer.getItemInHand(otherHand);

        if (canPolish(itemInOtherHand, pLevel)) {
            ItemStack item = itemInOtherHand.copy();
            ItemStack toPolish = item.split(1);
            pPlayer.startUsingItem(pUsedHand);
            itemstack.set(ModDataComponents.POLISHING.get(), toPolish);
            pPlayer.setItemInHand(otherHand, item);

            return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
        }

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
    }

    @Override
    @NotNull
    public ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull LivingEntity entityLiving) {
        if (!(entityLiving instanceof Player player)) return stack;

        if (stack.has(ModDataComponents.POLISHING.get())) {
            ItemStack toPolish = stack.get(ModDataComponents.POLISHING.get());
            ItemStack polished = applyPolish(toPolish, worldIn);

            /*Optional<RecipeHolder<SandingRecipe>> recipe = worldIn.getRecipeManager().getRecipeFor(
                    ModRecipes.SANDING_TYPE.get(), new SandingRecipeInput(toPolish), worldIn);
            if(recipe.isEmpty()) {
                Technoverse.LOGGER.debug("No recipe found");
                stack.remove(ModDataComponents.POLISHING.get());
                return stack;
            }
            ItemStack polished = recipe.get().value().output();
            Technoverse.LOGGER.debug("Polished: {}", polished);
            Technoverse.LOGGER.debug("Polished2: {}", recipe.get().value());*/

            if (worldIn.isClientSide) {
                spawnParticles(entityLiving.getEyePosition(1)
                                .add(entityLiving.getLookAngle()
                                        .scale(.5f)),
                        toPolish, worldIn);
                return stack;
            }

            if (!polished.isEmpty()) {
                player.getInventory().placeItemBackInInventory(polished);
            }

            stack.remove(ModDataComponents.POLISHING.get());
            stack.hurtAndBreak(1, entityLiving, LivingEntity.getSlotForHand(entityLiving.getUsedItemHand()));
        }

        return stack;
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull LivingEntity entityLiving, int timeLeft) {
        if (!(entityLiving instanceof Player player)) return;
        if (stack.has(ModDataComponents.POLISHING.get())) {
            ItemStack toPolish = stack.get(ModDataComponents.POLISHING.get());

            // noinspection DataFlowIssue - toPolish is not null because we checked it with has() before
            player.getInventory().placeItemBackInInventory(toPolish);

            stack.remove(ModDataComponents.POLISHING.get());
        }
    }

    @Override
    @NotNull
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        BlockState newState = state.getToolModifiedState(context, ToolActions.AXE_SCRAPE, false);
        if (newState != null) {
            //AllSoundEvents.SANDING_LONG.play(level, player, pos, 1, 1 + (level.random.nextFloat() * 0.5f - 1f) / 5f);
            level.levelEvent(player, 3005, pos, 0); // Spawn particles
        } else {
            newState = state.getToolModifiedState(context, ToolActions.AXE_WAX_OFF, false);
            if (newState != null) {
                /*AllSoundEvents.SANDING_LONG.play(level, player, pos, 1,
                        1 + (level.random.nextFloat() * 0.5f - 1f) / 5f);*/
                level.levelEvent(player, 3004, pos, 0); // Spawn particles
            }
        }

        if (newState != null) {
            level.setBlockAndUpdate(pos, newState);
            if (player != null) {
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return toolAction == ToolActions.AXE_SCRAPE || toolAction == ToolActions.AXE_WAX_OFF;
    }

    /*@Override
    public TriState shouldTriggerUseEffects(ItemStack stack, LivingEntity entity) {
        // Trigger every tick so that we have more fine grain control over the animation
        return TriState.TRUE;
    }

    @Override
    public boolean triggerUseEffects(ItemStack stack, LivingEntity entity, int count, RandomSource random) {
        if(stack.has(ModDataComponents.POLISHING.get())) {
            ItemStack polishing = stack.get(ModDataComponents.POLISHING.get());
            ((LivingEntityAccessor) entity).callSpawnItemParticles(polishing, 1);
        }

        // After 6 ticks play the sound every 7th
        if ((entity.getTicksUsingItem() - 6) % 7 == 0)
            entity.playSound(entity.getEatingSound(stack), 0.9F + 0.2F * random.nextFloat(),
                    random.nextFloat() * 0.2F + 0.9F);

        return true;
    }

    @Override
    @NotNull
    public SoundEvent getEatingSound() {
        return AllSoundEvents.SANDING_SHORT.getMainEvent();
    }*/

    @Override
    @NotNull
    public UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack pStack, @NotNull LivingEntity pEntity) {
        return 32;
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    /*@Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new SandPaperItemRenderer()));
    }*/

    private boolean canPolish(ItemStack stack, Level level) {
        Optional<RecipeHolder<SandingRecipe>> recipe = level.getRecipeManager().getRecipeFor(
                ModRecipes.SANDING_TYPE.get(), new SandingRecipeInput(stack), level);
        return recipe.isPresent();
    }

    private ItemStack applyPolish(ItemStack stack, Level level) {
        Optional<RecipeHolder<SandingRecipe>> recipe = level.getRecipeManager().getRecipeFor(
                ModRecipes.SANDING_TYPE.get(), new SandingRecipeInput(stack), level);

        if (recipe.isPresent()) {
            ItemStack polished = recipe.get().value().output();
            return polished.copy();
        } else {
            return ItemStack.EMPTY;
        }
    }

    private void spawnParticles(Vec3 location, ItemStack polishedStack, Level world) {
        for (int i = 0; i < 20; i++) {
            //Vec3 motion = VecHelper.offsetRandomly(Vec3.ZERO, world.random, 1 / 8f);
            RandomSource r = world.random;
            float radius = 1 / 8f;
            Vec3 motion = new Vec3((r.nextFloat() - .5f) * 2 * radius, (r.nextFloat() - .5f) * 2 * radius,
                    (r.nextFloat() - .5f) * 2 * radius);
            world.addParticle(new ItemParticleOption(ParticleTypes.ITEM, polishedStack), location.x, location.y,
                    location.z, motion.x, motion.y, motion.z);
        }
    }
}
