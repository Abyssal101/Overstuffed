package net.willsbr.overstuffed.networking.packet;

import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.AdvancementToggle.PlayerTogglesProvider;
import net.willsbr.overstuffed.StuffedBar.PlayerStuffedBarProvider;
import net.willsbr.overstuffed.networking.ModMessages;
import net.willsbr.overstuffed.sound.ModSounds;

import java.util.function.Supplier;

public class OverfullFoodC2SPacket {

    private static final String MESSAGE_OVERFULL_FOOD ="message.overstuffed.OverfullFood";
    //private static final String MESSAGE_DRINK_WATER_FAILED ="message.overstuffed.drink_water_failed";
    public OverfullFoodC2SPacket(){

    }

    public OverfullFoodC2SPacket(FriendlyByteBuf buf){

    }

    public void toBytes(FriendlyByteBuf buf){

    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
                {

                    //here we are on the server
                    ServerPlayer player=context.getSender();
                    ServerLevel level=player.getLevel();
                    if(!level.isClientSide)
                    {
                        player.getCapability(PlayerStuffedBarProvider.PLAYER_STUFFED_BAR).ifPresent(stuffedBar ->
                        {
                            ItemStack lastFood=player.getItemInHand(player.getUsedItemHand());
                            stuffedBar.addStuffedLevel(1, level.getGameTime(), lastFood.getUseDuration());
                            player.getCapability(PlayerTogglesProvider.PLAYER_TOGGLES).ifPresent(playerToggles -> {
                                //effectively if the random number is LOWER than the set frequency, it works! 0 should disable,a and 10 should be max
                                if(player.getRandom().nextIntBetweenInclusive(0,10)<playerToggles.getToggleValue(1))
                                {
                                    player.getLevel().playSound(null, player.blockPosition(), ModSounds.BURP_SOUNDS.get(
                                                    player.getRandom().nextIntBetweenInclusive(1,ModSounds.BURP_SOUNDS.size()-1)).get(),
                                            player.getSoundSource(), 1f, 1f);
                                }
                            });
                            ModMessages.sendToPlayer(new OverfullFoodDataSyncPacketS2C(stuffedBar.getCurrentStuffedLevel(), stuffedBar.getStuffedMax(),stuffedBar.getOverstuffedMax() ,stuffedBar.getSuperStuffedMax()),player);
                        });
                    }
                }
        );
        return true;
    }


}
