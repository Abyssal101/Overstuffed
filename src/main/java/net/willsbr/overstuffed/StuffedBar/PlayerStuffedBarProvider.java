package net.willsbr.overstuffed.StuffedBar;

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

public class PlayerStuffedBarProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<PlayerStuffedBar> PLAYER_STUFFED_BAR = CapabilityManager.get(
            new CapabilityToken<PlayerStuffedBar>() {});
    private PlayerStuffedBar stuffedBar =null;
    private final LazyOptional<PlayerStuffedBar> optional=LazyOptional.of(this::createPlayerStuffedBar);

    private PlayerStuffedBar createPlayerStuffedBar() {
        if(this.stuffedBar ==null)
        {
            this.stuffedBar =new PlayerStuffedBar();
        }
        return this.stuffedBar;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap== PLAYER_STUFFED_BAR)
        {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt= new CompoundTag();
        createPlayerStuffedBar().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerStuffedBar().loadNBTData(nbt);
    }
}
