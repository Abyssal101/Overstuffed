package net.willsbr.overstuffed.Mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.willsbr.overstuffed.StuffedBar.PlayerStuffedBarProvider;
import net.willsbr.overstuffed.client.ClientStuffedBarData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value=Player.class)
public abstract class MixinCanEat {


    @Shadow
    public abstract FoodData getFoodData();

    @Inject(method = "canEat", at = @At("RETURN"), cancellable = true)
    protected void canEat(boolean pCanAlwaysEat, CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player) (Object) this;
        if (!player.level().isClientSide()) {
            player.getCapability(PlayerStuffedBarProvider.PLAYER_STUFFED_BAR).ifPresent(stuffedbar -> {
                if (stuffedbar.getCurrentStuffedLevel() <
                        (stuffedbar.getFullLevel() + stuffedbar.getStuffedLevel() + stuffedbar.getOverstuffedLevel())) {
                    cir.setReturnValue(true);
                }

            });
        } else {
            if (ClientStuffedBarData.getPlayerStuffedBar() < (ClientStuffedBarData.getSoftLimit() + ClientStuffedBarData.getFirmLimit() + ClientStuffedBarData.getHardLimit())) {
                cir.setReturnValue(true);
            }
        }

    }
}

