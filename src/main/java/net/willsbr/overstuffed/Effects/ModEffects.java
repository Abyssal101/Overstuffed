package net.willsbr.overstuffed.Effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.willsbr.overstuffed.OverStuffed;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOD_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, OverStuffed.MODID);

    public static final RegistryObject<MobEffect> GOLDEN_DIET = MOD_EFFECTS.register("goldendiet", () ->  new GoldenDietEffect(MobEffectCategory.NEUTRAL, 16755200));

    public static void register(IEventBus eventBus)
    {
        MOD_EFFECTS.register(eventBus);
    }
}
