package net.willsbr.overstuffed.AdvancementToggle;

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

public class PlayerUnlocksProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<PlayerUnlocks> PLAYER_UNLOCKS = CapabilityManager.get(
            new CapabilityToken<PlayerUnlocks>() {});
    private PlayerUnlocks playerUnlocks =null;
    private final LazyOptional<PlayerUnlocks> optional=LazyOptional.of(this::createToggles);

    private PlayerUnlocks createToggles() {
        if(this.playerUnlocks ==null)
        {
            this.playerUnlocks =new PlayerUnlocks();

            //COme back for default setup
        }
        return this.playerUnlocks;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap== PLAYER_UNLOCKS)
        {
            return optional.cast();

        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt= new CompoundTag();
        createToggles().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createToggles().loadNBTData(nbt);
    }
}
