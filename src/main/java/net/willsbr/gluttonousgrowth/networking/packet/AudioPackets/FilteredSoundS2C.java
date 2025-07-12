package net.willsbr.gluttonousgrowth.networking.packet.AudioPackets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.willsbr.gluttonousgrowth.config.GluttonousClientConfig;
import net.willsbr.gluttonousgrowth.sound.ModSounds;

import java.util.Locale;
import java.util.function.Supplier;

public class FilteredSoundS2C {

    //sending data from server to client here'
     int soundIndex;
     BlockPos source;
     String name;
     boolean isSource;

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
        this.isSource=buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(soundIndex);
        buf.writeBlockPos(source);
        buf.writeUtf(name);
        buf.writeBoolean(isSource);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context context= supplier.get();
        context.enqueueWork(() ->
        {
            if(context.getSender().level().isClientSide)
            {
                Player player=Minecraft.getInstance().player;
                if(GluttonousClientConfig.digestiveSoundsVolume.get()>0) {
                    if (name.contentEquals("burp"))
                    {
                        player.level().playSound(null,source, ModSounds.BURP_SOUNDS.get(soundIndex).get(),
                                SoundSource.PLAYERS, (float) GluttonousClientConfig.digestiveSoundsVolume.get()/10, 1f);
                    }
                    else if(name.contentEquals("gurgle"))
                    {
                        player.level().playSound(player, source, ModSounds.GURGLE_SOUNDS.get(
                                        player.getRandom().nextIntBetweenInclusive(1,ModSounds.GURGLE_SOUNDS.size()-1)).get(),
                                player.getSoundSource(), (float) GluttonousClientConfig.digestiveSoundsVolume.get()/10, 1f);
                    }
                }
            }



        });
        return true;
    }
}
