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
    private long foodTick;

    public CalorieMeterDelaySyncPacketS2C(int delay, long foodTick) {
        this.delay = delay;
        this.foodTick = foodTick;
    }

    public CalorieMeterDelaySyncPacketS2C(FriendlyByteBuf buf) {
        this.delay = buf.readInt();
        this.foodTick = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(delay);
        buf.writeLong(foodTick);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // Use DistExecutor to safely run client-only code
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleOnClient());
        });
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    private void handleOnClient() {
        // All client-side logic goes here
        ClientCalorieMeter.setCurrentDelay(delay);
        ClientCalorieMeter.setCurrentSavedTick(foodTick);

        Player player = Minecraft.getInstance().player;
        if (player != null) {
            player.getCapability(PlayerCalorieMeterProvider.PLAYER_CALORIE_METER)
                    .ifPresent(calorieMeter -> {
                        calorieMeter.setCalClearDelay(delay);
                        calorieMeter.setFoodEatenTick(foodTick);
                    });

            CPMData.checkIfUpdateCPM("stuffed");
        }
    }
}