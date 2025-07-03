package net.willsbr.gluttonousgrowth.ServerPlayerSettings;

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

public class PlayerServerSettingsProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<PlayerServerSettings> PLAYER_SERVER_SETTINGS = CapabilityManager.get(
            new CapabilityToken<PlayerServerSettings>() {});
    private PlayerServerSettings playerServerSettings =null;
    private final LazyOptional<PlayerServerSettings> optional=LazyOptional.of(this::createPlayerServerSettings);

    private PlayerServerSettings createPlayerServerSettings() {
        if(this.playerServerSettings ==null)
        {
            this.playerServerSettings =new PlayerServerSettings();
        }
        return this.playerServerSettings;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap== PLAYER_SERVER_SETTINGS)
        {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt= new CompoundTag();
        createPlayerServerSettings().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerServerSettings().loadNBTData(nbt);
    }
}
