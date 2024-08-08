package net.willsbr.overstuffed.Effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBar;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBarProvider;

public class GoldenDietEffect extends MobEffect {

    protected GoldenDietEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level.isClientSide()) {
            Player effectedPlayer=(Player)pLivingEntity;
            effectedPlayer.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar ->
            {
                //So if the weight loss is greater than 2 seconds, lowers it to one second, if its already lower, then it goes even farther down
                if(weightBar.getWeightLossDelay()>20)
                {
                    //weightBar.setWeightLossDelay(20);
                }
                else if(false){
                    //COME BACk TO, THIS LOGIC NEEDS TO PAIR WITH TREADMILL
                    //weightBar.setWeightLossDelay(weightBar.getWeightLossDelay()/2);

                }
            });
            {

            }
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }


}
