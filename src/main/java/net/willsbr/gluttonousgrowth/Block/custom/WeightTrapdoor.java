package net.willsbr.gluttonousgrowth.Block.custom;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Tilt;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.willsbr.gluttonousgrowth.Entity.BlockEntity.ScaleBlockEntity;
import net.willsbr.gluttonousgrowth.Entity.BlockEntity.WeightTrapdoorBlockEntity;
import net.willsbr.gluttonousgrowth.Entity.ModEntities;
import net.willsbr.gluttonousgrowth.WeightSystem.PlayerWeightBarProvider;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class WeightTrapdoor extends BaseEntityBlock{

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final EnumProperty<Tilt> TILT = BlockStateProperties.TILT;
    private static final int NO_TICK = -1;
    private static int delay=10000;
    private static int resetDelay=200;

    // Tilt timing copied from Big Dripleaf (in ticks)
    private static final Object2IntMap<Tilt> DELAY_UNTIL_NEXT_TILT_STATE = Util.make(new Object2IntArrayMap<>(), map -> {
        map.defaultReturnValue(NO_TICK);
        map.put(Tilt.PARTIAL, 400);
        map.put(Tilt.FULL, 200);
    });

    // Collision shapes per tilt state matched to the provided model's thickness (y:12-14)
    private static final Map<Tilt, VoxelShape> LEAF_SHAPES = ImmutableMap.of(
            Tilt.NONE,     Block.box(0.0D, 12.0D, 0.0D, 16.0D, 14.0D, 16.0D),  // normal height (matches model)
            Tilt.PARTIAL,  Block.box(0.0D, 6.0D, 0.0D, 16.0D, 8.0D, 16.0D),  // slightly lowered to start falling through
            Tilt.FULL,     Shapes.empty()                                      // fully fallen through
    );

    public WeightTrapdoor(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(TILT, Tilt.NONE));
    }

    // Entity standing on top starts the tilt progression if not powered
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide)
        {
            if(entity instanceof Player player)
            {
                player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
                    if (state.getValue(TILT) == Tilt.NONE && canEntityTilt(pos, player) && !level.hasNeighborSignal(pos)) {

                        this.setTiltAndScheduleTick(state, level, pos, Tilt.PARTIAL, null);
                    }
                });

            }
        }
    }

    // Progress tilt over time, reset if powered
    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.hasNeighborSignal(pos)) {
            resetTilt(state, level, pos);
        } else {
            Tilt tilt = state.getValue(TILT);
             if (tilt == Tilt.PARTIAL) {
                this.setTiltAndScheduleTick(state, level, pos, Tilt.FULL, SoundEvents.IRON_TRAPDOOR_OPEN);
            } else if (tilt == Tilt.FULL) {
                resetTilt(state, level, pos);
            }
        }
    }

    // Instant reset when receiving redstone signal
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (level.hasNeighborSignal(pos)) {
            resetTilt(state, level, pos);
        }
    }

    private static void playTiltSound(Level level, BlockPos pos, SoundEvent sound) {
        float pitch = Mth.randomBetween(level.random, 0.8F, 1.2F);
        level.playSound((Player) null, pos, sound, SoundSource.BLOCKS, 1.0F, pitch);
    }

    private static boolean canEntityTilt(BlockPos pos, Player player) {
        // Matches Big Dripleaf check: must be above ~11/16ths + offset and on ground
        //Has to have weight bar to work
        AtomicBoolean validWeight = new AtomicBoolean(false);
        player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
            validWeight.set(weightBar.calculateCurrentWeightPercentage()>0.05);
        });
        return player.onGround() && player.position().y > (double) ((float) pos.getY() + 0.6875F) && validWeight.get();
    }

    private void setTiltAndScheduleTick(BlockState state, Level level, BlockPos pos, Tilt nextTilt, @Nullable SoundEvent sound) {
        setTilt(state, level, pos, nextTilt);
        if (sound != null) {
            playTiltSound(level, pos, sound);
        }
        if (delay != NO_TICK) {
            level.scheduleTick(pos, this, DELAY_UNTIL_NEXT_TILT_STATE.getInt(nextTilt));
        }
    }

    private static void resetTilt(BlockState state, Level level, BlockPos pos) {
        setTilt(state, level, pos, Tilt.NONE);
        if (state.getValue(TILT) != Tilt.NONE) {
            playTiltSound(level, pos, SoundEvents.IRON_DOOR_CLOSE);
        }
    }

    private static void setTilt(BlockState state, Level level, BlockPos pos, Tilt tilt) {
        Tilt prev = state.getValue(TILT);
        level.setBlock(pos, state.setValue(TILT, tilt), 2);
        if (tilt.causesVibration() && tilt != prev) {
            level.gameEvent((Entity) null, GameEvent.BLOCK_CHANGE, pos);
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return LEAF_SHAPES.get(state.getValue(TILT));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return LEAF_SHAPES.get(state.getValue(TILT));
    }
    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }


    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Face opposite the player by default
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(TILT, Tilt.NONE);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TILT);
    }

    //Block Entity Stuff
    @Override
    public @org.jetbrains.annotations.Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new WeightTrapdoorBlockEntity(pPos,pState);
    }

    @Override
    public @org.jetbrains.annotations.Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModEntities.WEIGHT_TRAPDOOR.get(), WeightTrapdoorBlockEntity::tick);
    }
}
