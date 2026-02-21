package net.willsbr.gluttonousgrowth.networking.packet.StuffedPackets;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.gluttonousgrowth.CPMCompat.Capability.CPMData;
import net.willsbr.gluttonousgrowth.StuffedBar.PlayerCalorieMeterProvider;
import net.willsbr.gluttonousgrowth.client.ClientCalorieMeter;

import java.util.function.Supplier;

public class CalorieMeterDelaySyncPacketS2C {
    private int delay;
    // Remaining ticks until calorie clear, as computed on the server.
    // Using remaining ticks (not absolute foodEatenTick) avoids server/client tickCount drift
    // that caused the debug display to show garbage values after dimension changes or death.
    private int remainingTicks;

    /**
     * @param delay          calClearDelay value
     * @param remainingTicks ticks remaining until the next calorie clear (-1 if timer not running)
     */
    public CalorieMeterDelaySyncPacketS2C(int delay, int remainingTicks) {
        this.delay = delay;
        this.remainingTicks = remainingTicks;
    }

    public CalorieMeterDelaySyncPacketS2C(FriendlyByteBuf buf) {
        this.delay = buf.readInt();
        this.remainingTicks = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(delay);
        buf.writeInt(remainingTicks);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleOnClient());
        });
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    private void handleOnClient() {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        ClientCalorieMeter.setCurrentDelay(delay);

        if (remainingTicks <= 0) {
            // Timer not running
            ClientCalorieMeter.setCurrentSavedTick(-1);
        } else {
            // Anchor to the client's own tickCount so drawDebug can compute:
            //   countdown = delay - (clientPlayer.tickCount - currentSavedTick)
            // using only client-side ticks, avoiding server/client drift entirely.
            ClientCalorieMeter.setCurrentSavedTick(player.tickCount - (delay - remainingTicks));
        }

        player.getCapability(PlayerCalorieMeterProvider.PLAYER_CALORIE_METER)
                .ifPresent(calorieMeter -> {
                    calorieMeter.setCalClearDelay(delay);
                });

        CPMData.checkIfUpdateCPM("stuffed");
    }
}
