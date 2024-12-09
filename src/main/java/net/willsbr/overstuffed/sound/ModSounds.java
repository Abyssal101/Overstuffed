package net.willsbr.overstuffed.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.willsbr.overstuffed.OverStuffed;

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

    public static void register(IEventBus eventBus){
        SOUND_EVENTS.register(eventBus);
    }

}
