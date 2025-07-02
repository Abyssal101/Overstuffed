package net.willsbr.overstuffed.Menu.Buttons;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class StateButton extends AbstractButton {
    private Component baseSetting;
    private Component altSetting;
    private boolean setting = true;


    private Component tooltipText;
    public StateButton(int pX, int pY, int pWidth, int pHeight, Component base, Component alt,boolean startVal)
    {
        super(pX, pY, pWidth, pHeight,Component.literal(""));
        baseSetting = base;
        altSetting = alt;
        setting = startVal;

        Component newName= setting ? baseSetting : altSetting;
        super.setMessage(newName);
    }

    public void onPress()
    {
        setting = !setting;
        Component newName= setting ? baseSetting : altSetting;
        super.setMessage(newName);
    }


    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float pPartialTick) {
        super.render(guiGraphics, mouseX, mouseY, pPartialTick);
    }

    public Component isTooltipText() {
        return tooltipText;
    }

    public void setTooltipText(Component tooltipText) {
        this.setTooltip(Tooltip.create(tooltipText));
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    public boolean getSetting() {
        return setting;
    }

    public void setSetting(boolean setting) {
        this.setting = setting;
    }

}

