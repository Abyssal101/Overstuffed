package net.willsbr.overstuffed.networking.packet.StuffedPackets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.AdvancementToggle.PlayerUnlocksProvider;
import net.willsbr.overstuffed.StuffedBar.PlayerStuffedBarProvider;
import net.willsbr.overstuffed.config.OverstuffedClientConfig;
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
                    Level level=player.level();
                    if(!level.isClientSide)
                    {
                        player.getCapability(PlayerStuffedBarProvider.PLAYER_STUFFED_BAR).ifPresent(stuffedBar ->
                        {
                            ItemStack lastFood=player.getItemInHand(player.getUsedItemHand());
                            stuffedBar.addStuffedLevel(1, level.getGameTime(), lastFood.getUseDuration());
                            player.getCapability(PlayerUnlocksProvider.PLAYER_UNLOCKS).ifPresent(playerToggles -> {
                                //effectively if the random number is LOWER than the set frequency, it works! 0 should disable,a and 10 should be max
                                ModSounds.playBurp(player);
                            });
                            ModMessages.sendToPlayer(new OverfullFoodDataSyncPacketS2C(stuffedBar.getCurrentStuffedLevel(), stuffedBar.getFullLevel(),stuffedBar.getStuffedLevel() ,stuffedBar.getOverstuffedLevel()),player);
                        });
                    }
                }
        );
        return true;
    }


}
