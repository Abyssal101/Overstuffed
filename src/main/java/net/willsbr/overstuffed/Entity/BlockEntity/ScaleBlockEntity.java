package net.willsbr.overstuffed.Entity.BlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.willsbr.overstuffed.Entity.ModEntities;
import net.willsbr.overstuffed.GluttonousGrowth;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.overstuffed.sound.ModSounds;

import java.util.List;

public class ScaleBlockEntity extends BlockEntity {

    private int displayWeight;



    public static final AABB TOUCH_BOX = new AABB(0.125D, 0.1D, 0.125D, 0.875D, 1, 0.875D);

    public ScaleBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModEntities.SCALE.get(), pPos, pBlockState);
    }


    public static void tick(Level level, BlockPos pos, BlockState blockState, ScaleBlockEntity scaleBE) {

        if (!level.isClientSide()) {

            if((level.getGameTime()&20)==0)
            {
                int check=scaleBE.getDisplayWeight();
                int playerWeights = scaleBE.checkOnTop(level, pos);
                scaleBE.setDisplayWeight(playerWeights);
                //checks for on and off
                if(check==0 && playerWeights!=0)
                {
                    level.playSound(null, pos,ModSounds.SCALEON.get(), SoundSource.BLOCKS, 0.5f,1f);
                }
                else if(check!=0 && playerWeights==0)
                {
                    level.playSound(null, pos,ModSounds.SCALEOFF.get(), SoundSource.BLOCKS,0.5f,1f);

                }

                blockState.getSignal(level,pos, Direction.NORTH);
                //Apparently the 2 is for flagging a client update?
                level.sendBlockUpdated(pos,blockState,blockState, Block.UPDATE_ALL);
            }

        }

        //Syncing Client


    }

    public int getDisplayWeight() {
        return displayWeight;
    }

    public void setDisplayWeight(int displayWeight) {
        this.displayWeight = displayWeight;
    }

    //This should return the total weights of all the players ontop of the scale
    public int checkOnTop(Level level, BlockPos pos) {
        AABB aabb = TOUCH_BOX.move(pos);
        List<? extends Player> list;
        list = level.getEntitiesOfClass(Player.class, aabb);

        if (!list.isEmpty()) {
            //it's forcing me to do this because I'm trying to use it inside of the ifPresent. I got no idea why
            final int[] totalWeight = {0};
            for (Player currentPlayer : list) {

                if (!currentPlayer.isIgnoringBlockTriggers()) {
                    currentPlayer.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar ->
                    {
                        totalWeight[0] += weightBar.getCurrentWeight();
                    });


                }
            }
            return totalWeight[0];
        }
        return 0;
    }


    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        CompoundTag scaleData=new CompoundTag();
        scaleData.putInt("displayweight",this.displayWeight);
        pTag.put(GluttonousGrowth.MODID+"scaledata",scaleData);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        CompoundTag scaleData=pTag.getCompound(GluttonousGrowth.MODID+"scaledata");
        this.displayWeight=scaleData.getInt("displayweight");
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
       saveAdditional(tag);
        return tag;
    }


    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        // Will get tag from #getUpdateTag
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
       load(pkt.getTag());
    }
}
