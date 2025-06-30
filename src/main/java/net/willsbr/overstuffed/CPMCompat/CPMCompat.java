package net.willsbr.overstuffed.CPMCompat;

import com.tom.cpm.api.ICPMPlugin;
import com.tom.cpm.api.IClientAPI;
import com.tom.cpm.api.ICommonAPI;
import net.minecraft.world.entity.player.Player;
import net.willsbr.overstuffed.GluttonousGrowth;
import net.willsbr.overstuffed.client.ClientCPMData;
import net.willsbr.overstuffed.config.GluttonousClientConfig;

public class CPMCompat implements ICPMPlugin {
    public void initClient(IClientAPI api) {
        //Init client
//          CPMMessageSenders.bellyPlaySender=((api.registerPluginMessage(Player.class, "playstuffedlevel",
//                  (player, message) -> {
//              //this plays the new animatiom
//
//              api.playAnimation(ClientCPMData.getStuffedBellyLayer(),message.getInteger("stuffedvalue"));
//              //this resets the old one if any
//              api.playAnimation(ClientCPMData.getLastStuffedBellyLayer(),0);
//
//              })));
        ClientCPMData.setPlayersAPI(api);


        //Is not used, but plays animations for EVERY player with the matching one. Sync Dances I guess?
        CPMMessageSenders.weightLevelSender=(api.registerPluginMessage(Player.class, "weightlevel",
                (player, message) -> {
                    //this plays the new animation
                    int temp=message.getInteger("currentweight");
                    api.playAnimation(GluttonousClientConfig.weightLayerConfigEntry.get(),temp);
                    //this resets the old one if any
                    api.playAnimation(GluttonousClientConfig.lastWeightLayer,0);
                }, true));
    }

    public void initCommon(ICommonAPI api) {
        //Init common
    }

    public String getOwnerModId() {
        return GluttonousGrowth.MODID;
    }
}
