package net.willsbr.overstuffed.Menu.Buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractOptionSliderButton;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

public class OptionSlider extends AbstractSliderButton {

    private String name;

    public OptionSlider(int pX, int pY, int pWidth, int pHeight, Component pMessage, double pValue) {
        super(pX, pY, pWidth, pHeight, pMessage, pValue);
        name=pMessage.getString();
        super.setMessage(Component.literal(name+": "+(int)(this.value*10)));
    }

    @Override
    protected void updateMessage() {

        this.setMessage(Component.literal(name+": "+(int)(this.value*10)));
    }


    @Override
    protected void applyValue() {

    }
    public void onRelease(double pMouseX, double pMouseY) {
        super.onRelease(pMouseX,pMouseY);
        double result=Math.round(this.value*10);
        this.value=result/10;

    }

    @Override
    public boolean keyReleased(int pKeyCode, int pScanCode, int pModifiers) {
        return super.keyReleased(pKeyCode, pScanCode, pModifiers);
    }
    public int getValue()
    {
        return (int)(this.value*10);
    }

}
