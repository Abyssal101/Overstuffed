package net.willsbr.gluttonousgrowth.Entity.BlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.willsbr.gluttonousgrowth.Block.custom.Funnel;
import net.willsbr.gluttonousgrowth.Entity.ModEntities;
import net.willsbr.gluttonousgrowth.GluttonousGrowth;
import net.willsbr.gluttonousgrowth.StuffedBar.PlayerCalorieMeterProvider;

import java.util.List;

public class FunnelBlockEntity extends BlockEntity {

    //Two items will be discarded per activation
    private static int CapacityRate=2;

    // Stored nutrition. Lives here instead of on a blockstate property so the Funnel block
    // doesn't explode into hundreds of thousands of state permutations.
    private int nutrition = 0;

    public FunnelBlockEntity(BlockPos pos, BlockState state) {
        super(ModEntities.FUNNEL.get(), pos, state);
    }

    public int getNutrition() {
        return nutrition;
    }

    public void setNutrition(int nutrition) {
        this.nutrition = nutrition;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FunnelBlockEntity be) {
        if (level.isClientSide) return;
        //makes it so this only happens once a second
        if(level.getGameTime() % 20 != 0) return;

        // Build AABB for a 2-block-high column directly below the funnel block
        // from pos.y-2 (inclusive) up to pos.y (exclusive top), covering the 1x1 column beneath
        AABB scanBox = new AABB(pos.getX(), pos.getY() - 2, pos.getZ(), pos.getX() + 1, pos.getY(), pos.getZ() + 1);
        List<? extends Player> players = level.getEntitiesOfClass(Player.class, scanBox, p -> !p.isSpectator());
        if (players.isEmpty()) return;

        int capacity = state.getValue(Funnel.CAPACITY);
        int nutrition = be.getNutrition();
        if (capacity <= 0 || nutrition <= 0) return;

        // Track capacity/nutrition locally and decrement as each player is fed so multiple
        // players below the funnel share the paste instead of each draining the full amount.
        for (Player player : players) {
            if (capacity <= 0 || nutrition <= 0) break;

            //density of the remaining paste: calories per unit of capacity
            int calRatio = nutrition / capacity;
            //makes certain you don't remove more than the capacity is currently holding
            int amount = Math.min(CapacityRate, capacity);
            int totalNutrition = calRatio * amount;

            capacity -= amount;
            nutrition -= totalNutrition;

            final int feed = totalNutrition;
            player.getCapability(PlayerCalorieMeterProvider.PLAYER_CALORIE_METER).ifPresent(calorieMeter ->
                    calorieMeter.handleCalorieAddition(feed, 0, (ServerPlayer) player));

            ((ServerLevel) level).sendParticles(ParticleTypes.HAPPY_VILLAGER,
                    pos.getX() + 0.5, pos.getY() - 0.25, pos.getZ() + 0.5, // Position
                    5, // Count
                    0.2, 0.2, 0.2, // Spread (dx, dy, dz)
                    1); // Speed
        }

        be.setNutrition(nutrition);
        level.setBlock(pos, state.setValue(Funnel.CAPACITY, capacity), Block.UPDATE_ALL);
        be.setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        CompoundTag funnelData = new CompoundTag();
        funnelData.putInt("nutrition", this.nutrition);
        pTag.put(GluttonousGrowth.MODID + "funneldata", funnelData);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        CompoundTag funnelData = pTag.getCompound(GluttonousGrowth.MODID + "funneldata");
        this.nutrition = funnelData.getInt("nutrition");
    }
}
