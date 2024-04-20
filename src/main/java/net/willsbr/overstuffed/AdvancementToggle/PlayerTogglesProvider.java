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

public class PlayerTogglesProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<PlayerToggles> PLAYER_TOGGLES = CapabilityManager.get(
            new CapabilityToken<PlayerToggles>() {});
    private PlayerToggles playerToggles =null;
    private final LazyOptional<PlayerToggles> optional=LazyOptional.of(this::createToggles);

    private PlayerToggles createToggles() {
        if(this.playerToggles ==null)
        {
            this.playerToggles =new PlayerToggles();

            //COme back for default setup
        }
        return this.playerToggles;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap== PLAYER_TOGGLES)
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
