package net.willsbr.overstuffed.CPMCompat;

import com.tom.cpm.api.IClientAPI;

public abstract class CPMMessageSenders {

    //now we need to be able to create these at the begining but shouldn't tie them directly to a class
    //this saves me the issue of figure out how the fuck any of this works
    //all the personalized data will be handled in ClientCPMConfigData

    public static IClientAPI.MessageSender bellyPlaySender;

    public static IClientAPI.MessageSender weightLevelSender;


}
