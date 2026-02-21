package net.willsbr.gluttonousgrowth.StuffedBar;

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

public class PlayerCalorieMeterProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<PlayerCalorieMeter> PLAYER_CALORIE_METER = CapabilityManager.get(
            new CapabilityToken<PlayerCalorieMeter>() {});
    private PlayerCalorieMeter calorieMeter =null;
    private final LazyOptional<PlayerCalorieMeter> optional=LazyOptional.of(this::createPlayerCalorieMeter);

    private PlayerCalorieMeter createPlayerCalorieMeter() {
        if(this.calorieMeter ==null)
        {
            this.calorieMeter =new PlayerCalorieMeter();
        }
        return this.calorieMeter;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap== PLAYER_CALORIE_METER)
        {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt= new CompoundTag();
        createPlayerCalorieMeter().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerCalorieMeter().loadNBTData(nbt);
    }
}
