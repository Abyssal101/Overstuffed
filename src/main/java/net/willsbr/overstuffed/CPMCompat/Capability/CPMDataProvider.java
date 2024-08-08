package net.willsbr.overstuffed.CPMCompat.Capability;

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

public class CPMDataProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<CPMData> PLAYER_CPM_DATA = CapabilityManager.get(
            new CapabilityToken<CPMData>() {});
    private CPMData cpmData =null;
    private final LazyOptional<CPMData> optional=LazyOptional.of(this::createPlayerCPMData);

    private CPMData createPlayerCPMData() {
        if(this.cpmData ==null)
        {
            this.cpmData =new CPMData();
        }
        return this.cpmData;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap== PLAYER_CPM_DATA)
        {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt= new CompoundTag();
        createPlayerCPMData().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerCPMData().loadNBTData(nbt);
    }
}
