package net.willsbr.overstuffed.CPMCompat;

import com.tom.cpm.api.ICPMPlugin;
import com.tom.cpm.api.IClientAPI;
import com.tom.cpm.api.ICommonAPI;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.willsbr.overstuffed.OverStuffed;
import net.willsbr.overstuffed.client.ClientCPMConfigData;

public class CPMCompat implements ICPMPlugin {
    public void initClient(IClientAPI api) {
        //Init client
//          CPMMessageSenders.bellyPlaySender=((api.registerPluginMessage(Player.class, "playstuffedlevel",
//                  (player, message) -> {
//              //this plays the new animatiom
//
//              api.playAnimation(ClientCPMConfigData.getStuffedBellyLayer(),message.getInteger("stuffedvalue"));
//              //this resets the old one if any
//              api.playAnimation(ClientCPMConfigData.getLastStuffedBellyLayer(),0);
//
//              })));
        ClientCPMConfigData.setPlayersAPI(api);

        CPMMessageSenders.weightLevelSender=(api.registerPluginMessage(Player.class, "weightlevel",
                (player, message) -> {
                    //this plays the new animation
                    int temp=message.getInteger("currentweight");
                        System.out.println("WHAT ARE YOU "+temp);
                    api.playAnimation(ClientCPMConfigData.getWeightLayer(),temp);
                    //this resets the old one if any
                    api.playAnimation(ClientCPMConfigData.getLastWeightLayer(),0);
                }, true));
    }

    public void initCommon(ICommonAPI api) {
        //Init common
    }

    public String getOwnerModId() {
        return OverStuffed.MODID;
    }
}
