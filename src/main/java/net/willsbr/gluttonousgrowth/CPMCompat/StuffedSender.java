package net.willsbr.gluttonousgrowth.CPMCompat;

import com.tom.cpm.api.IClientAPI;

public class StuffedSender {

        IClientAPI api;

        public StuffedSender(IClientAPI init)
        {
            api=init;
        }

        public void playAnim()
        {
            api.playAnimation("test");
        }





}
