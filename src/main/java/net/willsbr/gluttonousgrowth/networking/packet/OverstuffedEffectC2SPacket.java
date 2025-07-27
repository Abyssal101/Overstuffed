package net.willsbr.gluttonousgrowth.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.gluttonousgrowth.Effects.ModEffects;
import net.willsbr.gluttonousgrowth.WeightSystem.PlayerWeightBarProvider;

import java.util.function.Supplier;

public class OverstuffedEffectC2SPacket {
    private static final String MESSAGE_OVERFULL_EFFECT ="message.overstuffed.effect";
    //private static final String MESSAGE_DRINK_WATER_FAILED ="message.overstuffed.drink_water_failed";

    private int effectIndex;
    private int effectDuration;
    private int effectAmplifier;
    public OverstuffedEffectC2SPacket(int effectInd, int duration, int amplifier){
        effectIndex=effectInd;
        effectDuration=duration;
        effectAmplifier=amplifier;

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
                    assert player != null;
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
                            else if(effectIndex==1)
                            {
                                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, effectDuration ,effectAmplifier));
                            }
                            else if(effectIndex==2)
                            {
                                MobEffectInstance wedgeSlowness=new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, effectDuration,effectAmplifier);
                                player.addEffect(wedgeSlowness);
                            }
                        });
                    }

                }
        );
        return true;
    }
}
