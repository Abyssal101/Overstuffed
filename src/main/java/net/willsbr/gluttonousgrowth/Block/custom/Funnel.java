package net.willsbr.gluttonousgrowth.Block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.Nullable;
import net.willsbr.gluttonousgrowth.Entity.BlockEntity.FunnelBlockEntity;
import net.willsbr.gluttonousgrowth.Entity.ModEntities;

import java.util.stream.Stream;

public class Funnel extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING_HOPPER;
    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;
    public static final int MAXCAP = 20;
    public static final IntegerProperty CAPACITY =
            IntegerProperty.create("capacity", 0, MAXCAP);

    // Maximum stored nutrition. Kept in the block entity (not a blockstate property) so it
    // doesn't multiply the block's state permutations into the hundreds of thousands.
    public static final int MAX_NUTRITION = 500;

    public Funnel(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.DOWN)
                .setValue(ENABLED, Boolean.valueOf(false))
                .setValue(CAPACITY, Integer.valueOf(0))
        );
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {

        VoxelShape shape = Shapes.empty();

        shape = Shapes.join(shape, Shapes.box(0.375, 0, 0.375, 0.625, 0.125, 0.625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.125, 0.1875, 0.8125, 0.25, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.25, 0, 1, 1, 1), BooleanOp.OR);
        return shape;

    }

    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return getShape(pState, pLevel, pPos, pContext);
    }

    public VoxelShape getInteractionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return Shapes.block();
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction direction = pContext.getClickedFace().getOpposite();
        return this.defaultBlockState().setValue(FACING, direction.getAxis() == Direction.Axis.Y ? Direction.DOWN : direction).setValue(ENABLED, Boolean.valueOf(false));
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.getFoodProperties(pPlayer) != null) {
            if (!pLevel.isClientSide && pState.getValue(CAPACITY)!=MAXCAP)
            {
                BlockEntity be = pLevel.getBlockEntity(pPos);
                if (be instanceof FunnelBlockEntity funnelBE)
                {
                    int foodVal = itemstack.getFoodProperties(pPlayer).getNutrition();
                    int curVol = pState.getValue(CAPACITY);

                    funnelBE.setNutrition(Math.min(MAX_NUTRITION, funnelBE.getNutrition() + foodVal));
                    pLevel.setBlock(pPos, pState.setValue(CAPACITY, Integer.valueOf(curVol + 1)), Block.UPDATE_ALL);
                    pPlayer.awardStat(Stats.ITEM_USED.get(itemstack.getItem()));
                    if (!pPlayer.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                }
            }
            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        this.checkPoweredState(pLevel, pPos, pState, 4);
    }

    private void checkPoweredState(Level pLevel, BlockPos pPos, BlockState pState, int pFlags) {
        boolean flag = !pLevel.hasNeighborSignal(pPos);
        if (flag != pState.getValue(ENABLED)) {
            pLevel.setBlock(pPos, pState.setValue(ENABLED, Boolean.valueOf(flag)), pFlags);
        }

    }

//    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
//        if (!pState.is(pNewState.getBlock())) {
//            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
//            if (blockentity instanceof HopperBlockEntity) {
//                Containers.dropContents(pLevel, pPos, (HopperBlockEntity)blockentity);
//                pLevel.updateNeighbourForOutputSignal(pPos, this);
//            }
//
//            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
//        }
//    }



    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, ENABLED, CAPACITY);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new FunnelBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModEntities.FUNNEL.get(), FunnelBlockEntity::tick);
    }
}
