package fr.caranouga.technoverse.blocks;

import com.mojang.serialization.MapCodec;
import fr.caranouga.technoverse.Technoverse;
import fr.caranouga.technoverse.blocks.entity.AbstractMachineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

public abstract class AbstractMachineBlock<B extends AbstractMachineBlock<B, BE>, BE extends AbstractMachineBlockEntity<?>> extends BaseEntityBlock {
    private final VoxelShape SHAPE;
    private final MapCodec<B> CODEC;
    private final Class<BE> blockEntityClass;

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.LIT;

    public AbstractMachineBlock(Properties pProperties, VoxelShape SHAPE, Function<Properties, B> thisClass, Class<BE> beClass) {
        super(pProperties);

        this.SHAPE = SHAPE;
        this.CODEC = simpleCodec(thisClass);
        this.blockEntityClass = beClass;

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection());
    }

    @Override
    @NotNull
    protected BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    @NotNull
    protected BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, POWERED);
    }

    @Override
    @NotNull
    protected VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    @NotNull
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    @NotNull
    protected RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        try {
            Constructor<?> constructor = blockEntityClass.getConstructor(BlockPos.class, BlockState.class);
            return (BlockEntity) constructor.newInstance(pPos, pState);
        }catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException err){
            Technoverse.LOGGER.error("Error during reflection to get block entity:", err);
        }

        return null;
    }

    @Override
    protected void onRemove(BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if(pState.getBlock() != pNewState.getBlock()){
            if(isInstanceOf(pLevel.getBlockEntity(pPos))){
                BE blockEnt = blockEntityClass.cast(pLevel.getBlockEntity(pPos));
                blockEnt.drops();
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    @NotNull
    protected ItemInteractionResult useItemOn(@NotNull ItemStack pStack, @NotNull BlockState pState, Level pLevel,
                                              @NotNull BlockPos pPos, @NotNull Player pPlayer,
                                              @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult) {
        if(isInstanceOf(pLevel.getBlockEntity(pPos))){
            BE blockEntity = blockEntityClass.cast(pLevel.getBlockEntity(pPos));

            return use(blockEntity, pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
        }

        return ItemInteractionResult.SUCCESS;
    }

    protected abstract ItemInteractionResult use(BE blockEntity, @NotNull ItemStack pStack, @NotNull BlockState pState, Level pLevel,
                                                 @NotNull BlockPos pPos, @NotNull Player pPlayer,
                                                 @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult);

    private boolean isInstanceOf(Object obj){
        return blockEntityClass.isInstance(obj);
    }

    protected static class CommonUses {
        public static ItemInteractionResult openScreen(Level pLevel, Player pPlayer, AbstractMachineBlockEntity<?> blockEntity, BlockPos pPos, String id){
            if(!pLevel.isClientSide()){
                ((ServerPlayer) pPlayer).openMenu(new SimpleMenuProvider(blockEntity, Component.translatable("container." + Technoverse.MODID + "." + id)), pPos);
            }
            return ItemInteractionResult.SUCCESS;
        }
    }
}
