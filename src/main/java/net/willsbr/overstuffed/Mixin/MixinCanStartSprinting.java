package net.willsbr.overstuffed.Mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBarProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value=LocalPlayer.class)
public abstract class MixinCanStartSprinting {

    @Inject(method = "canStartSprinting",at =@At("RETURN"),cancellable = true)
    protected void canStartSprinting(CallbackInfoReturnable<Boolean> cir)
    {
//        assert Minecraft.getInstance().level != null;
//        if(Minecraft.getInstance().level.isClientSide)
//        {
//            Minecraft.getInstance().player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightbar -> {
//                if(weightbar.getLastWeightStage()>=4)
//                {
//                    cir.setReturnValue(false);
//                }
//            });
//        }

    }
}
