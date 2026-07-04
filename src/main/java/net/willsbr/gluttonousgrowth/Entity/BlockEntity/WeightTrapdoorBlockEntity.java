package net.willsbr.gluttonousgrowth.Entity.BlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.willsbr.gluttonousgrowth.Entity.ModEntities;
import net.willsbr.gluttonousgrowth.GluttonousGrowth;

public class WeightTrapdoorBlockEntity extends BlockEntity {

    private int totalDelay =-1;


    public WeightTrapdoorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModEntities.WEIGHT_TRAPDOOR.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        CompoundTag scaleData=new CompoundTag();
        scaleData.putInt("delay",this.totalDelay);

        pTag.put(GluttonousGrowth.MODID+"weighttrapdoordata",scaleData);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        CompoundTag scaleData=pTag.getCompound(GluttonousGrowth.MODID+"weighttrapdoordata");
        this.totalDelay =scaleData.getInt("delay");
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

    public int getTotalDelay() {
        return totalDelay;
    }

    public void setTotalDelay(int totalDelay) {
        this.totalDelay = totalDelay;
    }


}
