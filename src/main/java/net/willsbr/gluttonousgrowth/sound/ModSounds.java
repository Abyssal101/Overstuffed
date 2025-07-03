package net.willsbr.gluttonousgrowth.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.willsbr.gluttonousgrowth.GluttonousGrowth;
import net.willsbr.gluttonousgrowth.ServerPlayerSettings.PlayerServerSettingsProvider;
import net.willsbr.gluttonousgrowth.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.gluttonousgrowth.networking.ModMessages;
import net.willsbr.gluttonousgrowth.networking.packet.AudioPackets.FilteredSoundS2C;

import java.util.ArrayList;
import java.util.List;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, GluttonousGrowth.MODID);

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
        ResourceLocation id = new ResourceLocation(GluttonousGrowth.MODID, name);

        //1.19.2 Version
        //return SOUND_EVENTS.register(name, () -> new SoundEvent.createVariableRangeEvent(id));
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));

    }


    //TODO make a setting that dictates the volume of these
    //TODO Improve them with better variation and whatnot

    //ONLY CALL ON SERVER
    public static void playBurp(Player player)
    {
            if(!player.level().isClientSide)
            {
                player.getCapability(PlayerServerSettingsProvider.PLAYER_SERVER_SETTINGS).ifPresent(serverSettings -> {
                    if(player.getRandom().nextIntBetweenInclusive(0,10)<serverSettings.getBurpFrequency())
                    {
                        int soundIndex=player.getRandom().nextIntBetweenInclusive(1,ModSounds.BURP_SOUNDS.size()-1);
                        if(player.level().getServer().getPlayerList()!=null)
                        {
                            List< ServerPlayer> players = player.level().getServer().getPlayerList().getPlayers();
                            for(ServerPlayer eachPlayer : players)
                            {
                                if(eachPlayer.level().dimension() == player.level().dimension())
                                {
                                    ModMessages.sendToPlayer(new FilteredSoundS2C(soundIndex,player.blockPosition(),"burp"), eachPlayer);
                                }
                            }
                        }
                    }
                });
            }
    }
    public static void playGurgle(Player player)
    {

        if(!player.level().isClientSide)
        {
            player.getCapability(PlayerServerSettingsProvider.PLAYER_SERVER_SETTINGS).ifPresent(serverSettings -> {
                player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
                    if (serverSettings.getGurgleFrequency() > 0 & weightBar.getLastWeightStage() >= 1 &&
                            player.getRandom().nextFloat() < (0.001f * Math.sqrt(serverSettings.getGurgleFrequency() * weightBar.getLastWeightStage())))
                    {
                        int soundIndex=player.getRandom().nextIntBetweenInclusive(1,ModSounds.GURGLE_SOUNDS.size()-1);
                        if(player.level().getServer().getPlayerList()!=null)
                        {
                            List<ServerPlayer> players = player.level().getServer().getPlayerList().getPlayers();
                            for(ServerPlayer eachPlayer : players)
                            {
                                if(eachPlayer.level().dimension() == player.level().dimension())
                                {
                                    ModMessages.sendToPlayer(new FilteredSoundS2C(soundIndex,player.blockPosition(),"gurgle"), eachPlayer);
                                }
                            }
                        }
                    }
                });
            });
        }
    }




    public static void register(IEventBus eventBus){
        SOUND_EVENTS.register(eventBus);
    }

}
