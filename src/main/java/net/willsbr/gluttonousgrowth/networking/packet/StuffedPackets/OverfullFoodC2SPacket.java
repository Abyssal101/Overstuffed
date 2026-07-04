package net.willsbr.gluttonousgrowth.networking.packet.StuffedPackets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.gluttonousgrowth.ServerPlayerSettings.PlayerServerSettingsProvider;
import net.willsbr.gluttonousgrowth.StuffedBar.PlayerCalorieMeterProvider;
import net.willsbr.gluttonousgrowth.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.gluttonousgrowth.config.GluttonousWorldConfig;
import net.willsbr.gluttonousgrowth.networking.ModMessages;
import net.willsbr.gluttonousgrowth.sound.ModSounds;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class OverfullFoodC2SPacket {

    private static final String MESSAGE_OVERFULL_FOOD ="message.overstuffed.OverfullFood";
    //private static final String MESSAGE_DRINK_WATER_FAILED ="message.overstuffed.drink_water_failed";

    private int nutrition=0;
    private float saturationModifier=0;

    public OverfullFoodC2SPacket(int nut,float sat){
    this.nutrition=nut;
    this.saturationModifier=sat;
    }

    public OverfullFoodC2SPacket(FriendlyByteBuf buf){
        this.nutrition=buf.readInt();
        this.saturationModifier=buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(nutrition);
        buf.writeFloat(saturationModifier);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
                {

                    //here we are on the server
                    ServerPlayer player=context.getSender();
                    Level level=player.level();
                    if(!level.isClientSide)
                    {
                        player.getCapability(PlayerCalorieMeterProvider.PLAYER_CALORIE_METER).ifPresent(calorieMeter ->
                        {

                               calorieMeter.handleCalorieAddition(this.nutrition,this.saturationModifier,player);
                        });
                    }
                }
        );
        return true;
    }


}
