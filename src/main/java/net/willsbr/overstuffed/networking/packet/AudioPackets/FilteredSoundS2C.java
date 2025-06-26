package net.willsbr.overstuffed.networking.packet.AudioPackets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMDataProvider;
import net.willsbr.overstuffed.config.OverstuffedClientConfig;
import net.willsbr.overstuffed.sound.ModSounds;

import java.util.function.Supplier;

public class FilteredSoundS2C {

    //sending data from server to client here'
     int soundIndex;
     BlockPos source;
     String name;

    public FilteredSoundS2C(int soundIndex, BlockPos source,String name)
    {
        this.soundIndex=soundIndex;
        this.source=source;
        this.name=name;
    }

    public FilteredSoundS2C(FriendlyByteBuf buf){
        this.soundIndex=buf.readInt();
        this.source=buf.readBlockPos();
        this.name=buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(soundIndex);
        buf.writeBlockPos(source);
        buf.writeUtf(name);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {

            if(Minecraft.getInstance().player!=null)
            {
                LocalPlayer player= Minecraft.getInstance().player;
                if(OverstuffedClientConfig.digestiveSoundsVolume.get()>0)
                {
                    if(name.contentEquals("burp"))
                    {
                        player.level().playSound(player,source, ModSounds.BURP_SOUNDS.get(soundIndex).get(),
                                SoundSource.PLAYERS, (float)OverstuffedClientConfig.digestiveSoundsVolume.get()/10, 1f);
                    }
                    else if(name.contentEquals("gurgle"))
                    {
                        player.level().playSound(player, source, ModSounds.GURGLE_SOUNDS.get(
                                        player.getRandom().nextIntBetweenInclusive(1,ModSounds.GURGLE_SOUNDS.size()-1)).get(),
                                player.getSoundSource(), (float)OverstuffedClientConfig.digestiveSoundsVolume.get()/10, 1f);
                    }
                }

            }

        });
        return true;
    }
}
