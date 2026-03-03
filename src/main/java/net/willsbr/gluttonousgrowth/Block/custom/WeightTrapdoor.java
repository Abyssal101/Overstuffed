package net.willsbr.gluttonousgrowth.Block.custom;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.willsbr.gluttonousgrowth.Entity.BlockEntity.WeightTrapdoorBlockEntity;
import net.willsbr.gluttonousgrowth.Entity.ModEntities;
import net.willsbr.gluttonousgrowth.WeightSystem.PlayerWeightBarProvider;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class WeightTrapdoor extends BaseEntityBlock{

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final EnumProperty<Tilt> TILT = BlockStateProperties.TILT;

    private int maxDelay=1000;

    // Collision shapes per tilt state matched to the provided model's thickness (y:12-14)
    private static final Map<Tilt, VoxelShape> LEAF_SHAPES = ImmutableMap.of(
            Tilt.NONE,     Block.box(0.0D, 12.0D, 0.0D, 16.0D, 14.0D, 16.0D),  // normal height (matches model)
            Tilt.UNSTABLE,     Block.box(0.0D, 12.0D, 0.0D, 16.0D, 14.0D, 16.0D),
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
                    if (state.getValue(TILT) == Tilt.NONE && canEntityTilt(pos, player,level))
                    {
                        if(level.getBestNeighborSignal(pos)!=15)
                        {
                            BlockEntity be=level.getBlockEntity(pos);
                            if(be instanceof WeightTrapdoorBlockEntity wtbe)
                            {
                                //TODO Revisit Delay
                                //Basically this code is so that based off whoever steps on it first, the timing of it gets dyanimcally adjusted
                                //So if your fat, this should happen much quicker than if your thin, but once it starts it doens't stop

                                //This value will be subtracted from the delay. Remove min weight from equation
                                //Three stages so each stage has to at least take a tick to fall through otherwise bad things could happen
                                wtbe.setTotalDelay(maxDelay-Math.min(weightBar.getCurrentWeight()-weightBar.getMinWeight(),maxDelay-3));
                                this.setTiltAndScheduleTick(state, level, pos, Tilt.UNSTABLE, (SoundEvent)null,wtbe.getTotalDelay()/2);
                            }

                        }

                    }
                });

            }
        }
    }
    private void setTiltAndScheduleTick(BlockState pState, Level pLevel, BlockPos pPos, Tilt pTilt, @Nullable SoundEvent pSound, int delay) {
        setTilt(pState, pLevel, pPos, pTilt);
        if (pSound != null) {
            playTiltSound(pLevel, pPos, pSound);
        }
        if(delay!=-1) {
            pLevel.scheduleTick(pPos, this, delay);
        }

    }
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pLevel.hasNeighborSignal(pPos)) {
            resetTilt(pState, pLevel, pPos);
        } else {

            Tilt tilt = pState.getValue(TILT);
            if (tilt == Tilt.UNSTABLE) {
                BlockEntity be = pLevel.getBlockEntity(pPos);
                if (be instanceof WeightTrapdoorBlockEntity wtbe) {
                    this.setTiltAndScheduleTick(pState, pLevel, pPos, Tilt.PARTIAL, SoundEvents.IRON_TRAPDOOR_OPEN, wtbe.getTotalDelay()/2);
                }
            }
            else if (tilt == Tilt.PARTIAL) {
                BlockEntity be=pLevel.getBlockEntity(pPos);
                if(be instanceof WeightTrapdoorBlockEntity wtbe) {
                    //this should be a set amount of time, not absed off of weight because this is how long it takes to reset
                    this.setTiltAndScheduleTick(pState, pLevel, pPos, Tilt.FULL, SoundEvents.IRON_TRAPDOOR_OPEN,140);
                }
            } else if (tilt == Tilt.FULL) {
                resetTilt(pState, pLevel, pPos);
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

    private static boolean canEntityTilt(BlockPos pos, Player player,Level level) {
        // Matches Big Dripleaf check: must be above ~11/16ths + offset and on ground
        //Has to have weight bar to work
        AtomicBoolean validWeight = new AtomicBoolean(false);
        player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
            int sigLevel=level.getBestNeighborSignal(pos);
            validWeight.set(weightBar.calculateCurrentWeightPercentage()>0.05*sigLevel);
        });

        return player.onGround() && player.position().y > (double) ((float) pos.getY() + 0.6875F) && validWeight.get();
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
}
