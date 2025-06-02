package net.willsbr.overstuffed.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.willsbr.overstuffed.OverStuffed;
import net.willsbr.overstuffed.ServerPlayerSettings.PlayerServerSettingsProvider;
import net.willsbr.overstuffed.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.overstuffed.config.OverstuffedClientConfig;

import java.util.ArrayList;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, OverStuffed.MODID);

    //registering individual sounds
    public static final RegistryObject<SoundEvent> BURP1 = registerSoundEvent("burp1");

    public static final RegistryObject<SoundEvent> BELCH2 = registerSoundEvent("belch2");
    public static final RegistryObject<SoundEvent> BELCH3 = registerSoundEvent("belch3");
    public static final RegistryObject<SoundEvent> BELCH4 = registerSoundEvent("belch4");
    public static final RegistryObject<SoundEvent> BELCH5 = registerSoundEvent("belch5");

    public static final ArrayList<RegistryObject<SoundEvent>> BURP_SOUNDS = new ArrayList<RegistryObject<SoundEvent>>();


    public static final RegistryObject<SoundEvent> GURGLE1 = registerSoundEvent("gurgle1");
    public static final RegistryObject<SoundEvent> GURGLE2 = registerSoundEvent("gurgle2");
    public static final RegistryObject<SoundEvent> GURGLE3 = registerSoundEvent("gurgle3");
    public static final RegistryObject<SoundEvent> GURGLE4 = registerSoundEvent("gurgle4");
    public static final RegistryObject<SoundEvent> GURGLE5 = registerSoundEvent("gurgle5");

    public static final ArrayList<RegistryObject<SoundEvent>> GURGLE_SOUNDS = new ArrayList<RegistryObject<SoundEvent>>();

    public static final RegistryObject<SoundEvent> SCALEON= registerSoundEvent("scaleon");

    public static final RegistryObject<SoundEvent> SCALEOFF= registerSoundEvent("scaleoff");


    public static void createArrays()
    {
        BURP_SOUNDS.add(BURP1);
        BURP_SOUNDS.add(BELCH2);
        BURP_SOUNDS.add(BELCH3);
        BURP_SOUNDS.add(BELCH4);
        BURP_SOUNDS.add(BELCH5);

        GURGLE_SOUNDS.add(GURGLE1);
        GURGLE_SOUNDS.add(GURGLE2);
        GURGLE_SOUNDS.add(GURGLE3);
        GURGLE_SOUNDS.add(GURGLE4);
        GURGLE_SOUNDS.add(GURGLE5);
    }

    public static RegistryObject<SoundEvent> registerSoundEvent(String name)
    {
        ResourceLocation id = new ResourceLocation(OverStuffed.MODID, name);

        //1.19.2 Version
        //return SOUND_EVENTS.register(name, () -> new SoundEvent.createVariableRangeEvent(id));

        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));

    }


    //TODO make a setting that dictates the volume of these
    //TODO Improve them with better variation and whatnot
    public static void playBurp(Player player)
    {
        if(player.level().isClientSide)
        {
            //effectively if the random number is LOWER than the set frequency,
            // it works! 0 should disable,a and 10 should be max

            if(player.getRandom().nextIntBetweenInclusive(0,10)<OverstuffedClientConfig.burpFrequency.get())
            {
                player.level().playSound(null, player.blockPosition(), ModSounds.BURP_SOUNDS.get(
                                player.getRandom().nextIntBetweenInclusive(1,ModSounds.BURP_SOUNDS.size()-1)).get(),
                        player.getSoundSource(), 1f, 1f);

            }
        }
        else
        {
            player.getCapability(PlayerServerSettingsProvider.PLAYER_SERVER_SETTINGS).ifPresent(serverSettings -> {
                if(player.getRandom().nextIntBetweenInclusive(0,10)<serverSettings.getBurpFrequency())
                {
                    player.level().playSound(null, player.blockPosition(), ModSounds.BURP_SOUNDS.get(
                                    player.getRandom().nextIntBetweenInclusive(1,ModSounds.BURP_SOUNDS.size()-1)).get(),
                            player.getSoundSource(), 1f, 1f);

                }
            });

        }

    }
    public static void playGurgle(Player player)
    {

        if(player.level().isClientSide)
        {
            if(player.getRandom().nextIntBetweenInclusive(0,10)<OverstuffedClientConfig.burpFrequency.get())
            {
                player.level().playSound(null, player.blockPosition(), ModSounds.BURP_SOUNDS.get(
                                player.getRandom().nextIntBetweenInclusive(1,ModSounds.BURP_SOUNDS.size()-1)).get(),
                        player.getSoundSource(), 1f, 1f);

            }
        }
        else
        {
            player.getCapability(PlayerServerSettingsProvider.PLAYER_SERVER_SETTINGS).ifPresent(serverSettings -> {
                player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
                    if (serverSettings.getGurgleFrequency() > 0 & weightBar.getLastWeightStage() >= 1 &&
                            player.getRandom().nextFloat() < (0.001f * Math.sqrt(serverSettings.getGurgleFrequency() * weightBar.getLastWeightStage())))
                    {
                        player.level().playSound(null, player.blockPosition(), ModSounds.GURGLE_SOUNDS.get(
                                        player.getRandom().nextIntBetweenInclusive(1, ModSounds.GURGLE_SOUNDS.size()) - 1).get(),
                                player.getSoundSource(), 0.5f, 1f);
                    }
                });
            });

        }

    }

    public static void register(IEventBus eventBus){
        SOUND_EVENTS.register(eventBus);
    }

}
