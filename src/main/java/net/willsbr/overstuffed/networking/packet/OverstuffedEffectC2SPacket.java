package net.willsbr.overstuffed.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.Effects.ModEffects;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBarProvider;

import java.util.function.Supplier;

public class OverstuffedEffectC2SPacket {
    private static final String MESSAGE_OVERFULL_EFFECT ="message.overstuffed.effect";
    //private static final String MESSAGE_DRINK_WATER_FAILED ="message.overstuffed.drink_water_failed";

    private static int effectIndex;
    private static int effectDuration;
    private static int effectAmplifier;
    public OverstuffedEffectC2SPacket(int effectIndex, int duration, int amplifier){
        this.effectIndex=effectIndex;
        effectDuration=duration;
        this.effectAmplifier=amplifier;

    }

    public OverstuffedEffectC2SPacket(FriendlyByteBuf buf){
        effectIndex=buf.readInt();
        effectDuration=buf.readInt();
        effectAmplifier= buf.readInt();

    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(effectIndex);
        buf.writeInt(effectDuration);
        buf.writeInt(effectAmplifier);

    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
                {

                    //here we are on the server
                    ServerPlayer player=context.getSender();
                    //TODO Abstract level() and getLevel()
                    Level level=player.level();
                    if(!level.isClientSide)
                    {

                        player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar ->
                        {
                            //0 represents golden diet
                            if(effectIndex==0)
                            {
                                player.addEffect(new MobEffectInstance(ModEffects.GOLDEN_DIET.get(), effectDuration ,effectAmplifier));

                            }
                        });
                    }

                }
        );
        return true;
    }
}
