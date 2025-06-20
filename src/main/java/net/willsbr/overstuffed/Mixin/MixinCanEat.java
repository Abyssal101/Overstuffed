package net.willsbr.overstuffed.Mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.willsbr.overstuffed.StuffedBar.PlayerCalorieMeterProvider;
import net.willsbr.overstuffed.client.ClientCalorieMeter;
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
            player.getCapability(PlayerCalorieMeterProvider.PLAYER_CALORIE_METER).ifPresent(calorieMeter -> {
                //todo make it so actually accounts for the current food being held?
                if (calorieMeter.getCurrentCalories() <
                        calorieMeter.getMaxCalories()) {
                    cir.setReturnValue(true);
                }

            });
        } else {
            if (ClientCalorieMeter.getCurrentCalories() <ClientCalorieMeter.getMax()) {
                cir.setReturnValue(true);
            }
        }

    }
}

