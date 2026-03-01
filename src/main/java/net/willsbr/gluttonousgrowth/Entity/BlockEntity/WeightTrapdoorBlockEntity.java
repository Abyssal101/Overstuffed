package net.willsbr.gluttonousgrowth.Entity.BlockEntity;

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
import net.willsbr.gluttonousgrowth.Entity.ModEntities;
import net.willsbr.gluttonousgrowth.GluttonousGrowth;
import net.willsbr.gluttonousgrowth.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.gluttonousgrowth.sound.ModSounds;

import java.util.List;

public class WeightTrapdoorBlockEntity extends BlockEntity {

    private int remaining=-1;
    private int appliedWeight=0;

    public WeightTrapdoorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModEntities.WEIGHT_TRAPDOOR.get(), pPos, pBlockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState blockState, WeightTrapdoorBlockEntity trapdoorBE) {
        if (!level.isClientSide())
        {

        }
        //Syncing Client
    }
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

//        CompoundTag scaleData=new CompoundTag();
//        scaleData.putInt("displayweight",this.displayWeight);
//        pTag.put(GluttonousGrowth.MODID+"scaledata",scaleData);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
//        CompoundTag scaleData=pTag.getCompound(GluttonousGrowth.MODID+"scaledata");
//        this.displayWeight=scaleData.getInt("displayweight");
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
