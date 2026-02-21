package net.willsbr.gluttonousgrowth.networking.packet.AudioPackets;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.gluttonousgrowth.config.GluttonousClientConfig;
import net.willsbr.gluttonousgrowth.sound.ModSounds;

import java.util.function.Supplier;

public class FilteredSoundS2C {
    //sending data from server to client here
    int soundIndex;
    BlockPos source;
    String name;
    boolean isSource;

    public FilteredSoundS2C(int soundIndex, BlockPos source, String name) {
        this.soundIndex = soundIndex;
        this.source = source;
        this.name = name;
    }

    public FilteredSoundS2C(FriendlyByteBuf buf) {
        this.soundIndex = buf.readInt();
        this.source = buf.readBlockPos();
        this.name = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(soundIndex);
        buf.writeBlockPos(source);
        buf.writeUtf(name);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        // Use DistExecutor to safely run client-only code
        context.enqueueWork(() -> {
            // This ensures the client code only runs on the client side
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleOnClient());
        });

        return true;
    }

    // Move all client-only code to a separate method with @OnlyIn annotation
    @OnlyIn(Dist.CLIENT)
    private void handleOnClient() {
        Player player = Minecraft.getInstance().player;
        if (player != null && GluttonousClientConfig.digestiveSoundsVolume.get() > 0) {
            if (name.contentEquals("burp")) {
                player.level().playSound(player, source, ModSounds.BURP_SOUNDS.get(soundIndex).get(),
                        SoundSource.PLAYERS, (float) GluttonousClientConfig.digestiveSoundsVolume.get() / 10, 1f);
            } else if (name.contentEquals("gurgle")) {
                player.level().playSound(player, source, ModSounds.GURGLE_SOUNDS.get(
                                player.getRandom().nextIntBetweenInclusive(1, ModSounds.GURGLE_SOUNDS.size() - 1)).get(),
                        player.getSoundSource(), (float) GluttonousClientConfig.digestiveSoundsVolume.get() / 10, 1f);
            }
        }
    }
}