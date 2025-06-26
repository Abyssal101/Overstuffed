package net.willsbr.overstuffed.WeightSystem;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerWeightBarProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<PlayerWeightBar> PLAYER_WEIGHT_BAR = CapabilityManager.get(
            new CapabilityToken<PlayerWeightBar>() {});
    private PlayerWeightBar weightBar =null;
    private final LazyOptional<PlayerWeightBar> optional=LazyOptional.of(this::createWeightBar);

    private PlayerWeightBar createWeightBar() {
        if(this.weightBar ==null)
        {
            this.weightBar =new PlayerWeightBar();
        }
        return this.weightBar;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap== PLAYER_WEIGHT_BAR)
        {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt= new CompoundTag();
        createWeightBar().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createWeightBar().loadNBTData(nbt);
    }
}
