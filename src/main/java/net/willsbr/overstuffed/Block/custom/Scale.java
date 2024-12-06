package net.willsbr.overstuffed.Block.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.willsbr.overstuffed.Entity.BlockEntity.ScaleBlockEntity;
import net.willsbr.overstuffed.Entity.ModEntities;
import net.willsbr.overstuffed.StuffedBar.PlayerStuffedBarProvider;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBarProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Scale extends BaseEntityBlock {
    private static final VoxelShape SHAPE_NORTH=makeShapeNorth();

    private static final VoxelShape SHAPE_SOUTH=makeShapeSouth();
    private static final VoxelShape SHAPE_WEST=makeShapeWest();

    private static final VoxelShape SHAPE_EAST=makeShapeEast();

    public static final AABB TOUCH_BOX = new AABB(0.125D, 0.1D, 0.125D, 0.875D, 1, 0.875D);

    private static final DirectionProperty FACING= BlockStateProperties.HORIZONTAL_FACING;

   private static final BooleanProperty POWERED = BlockStateProperties.POWERED;


   //IF you get a missing texture file when you change version from the block model, ITS BECAUSE a properrty that should be axis is vector2f


    public Scale(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
        pBuilder.add(POWERED);

    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        //this.defaultBlockState().setValue(POWERED,false);
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection());
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }


    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if(pState.getValue(FACING).equals(Direction.NORTH))
        {
            return SHAPE_NORTH;
        }
        else if(pState.getValue(FACING).equals(Direction.WEST))
        {
            return SHAPE_WEST;
        }
        else if(pState.getValue(FACING).equals(Direction.EAST))
        {
            return SHAPE_EAST;
        }
        else if(pState.getValue(FACING).equals(Direction.SOUTH))
        {
            return SHAPE_SOUTH;
        }
        else {
            return SHAPE_NORTH;
        }
    }


    public static VoxelShape makeShapeNorth(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0, 0.125, 0.125, 0.125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.875, 0, 0, 1, 0.125, 0.125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.875, 0.125, 0.125, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.375, 0.1875, 0, 0.625, 1.1875, 0.0625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.125, 0, 1, 0.25, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.875, 0, 0.875, 1, 0.125, 1), BooleanOp.OR);

        return shape;
    }
    public static VoxelShape makeShapeSouth()
    {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.875, 0, 0.875, 1, 0.125, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.875, 0.125, 0.125, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.875, 0, 0, 1, 0.125, 0.125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.375, 0.1875, 0.9375, 0.625, 1.1875, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.125, 0, 1, 0.25, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0, 0, 0.125, 0.125, 0.125), BooleanOp.OR);

        return shape;
    }

    public static VoxelShape makeShapeWest(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.875, 0.125, 0.125, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0, 0, 0.125, 0.125, 0.125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.875, 0, 0.875, 1, 0.125, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.1875, 0.375, 0.0625, 1.1875, 0.625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.125, 0, 1, 0.25, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.875, 0, 0, 1, 0.125, 0.125), BooleanOp.OR);

        return shape;
    }
    public static VoxelShape makeShapeEast()
    {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.875, 0, 0, 1, 0.125, 0.125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.875, 0, 0.875, 1, 0.125, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0, 0, 0.125, 0.125, 0.125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.9375, 0.1875, 0.375, 1, 1.1875, 0.625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.125, 0, 1, 0.25, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.875, 0.125, 0.125, 1), BooleanOp.OR);

        return shape;
    }


    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ScaleBlockEntity(pPos,pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModEntities.SCALE.get(),ScaleBlockEntity::tick);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> GameEventListener getListener(ServerLevel pLevel, T pBlockEntity) {
        return super.getListener(pLevel, pBlockEntity);
    }



    //REDSTONE/PRESSURE PLATE LOGIC


    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return true;
    }



    @Override
    public int getSignal(BlockState pState, BlockGetter level, BlockPos pos, Direction pDirection) {
        if(level.getBlockEntity(pos) instanceof ScaleBlockEntity)
        {
            ScaleBlockEntity scaleBE=(ScaleBlockEntity)level.getBlockEntity(pos);
            int displayWeight=scaleBE.getDisplayWeight();
            if(displayWeight<=1500)
            {
                return displayWeight/100;
            }

        }
       return 0;
    }


    @Override
    public int getDirectSignal(BlockState pState, BlockGetter level, BlockPos pos, Direction pDirection) {
        if(level.getBlockEntity(pos) instanceof ScaleBlockEntity)
        {
            ScaleBlockEntity scaleBE=(ScaleBlockEntity)level.getBlockEntity(pos);
            int displayWeight=scaleBE.getDisplayWeight();
            if(displayWeight<=1500)
            {
                return displayWeight/100;
            }

        }
        return 0;

    }
}


