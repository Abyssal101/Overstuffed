package net.willsbr.gluttonousgrowth.Menu.Buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class SwapScreenButton extends AbstractButton {

    public static final int WIDTH = 100;
    public static final int HEIGHT = 20;

    private boolean viewing = true;

    private Component tooltipText;
    private Screen targetScreen;
    public SwapScreenButton(int pX, int pY, Component pMessage, Screen screen,Component tooltipText) {
        super(pX, pY, SwapScreenButton.WIDTH, SwapScreenButton.HEIGHT, pMessage);
        this.tooltipText=tooltipText;
        this.targetScreen=screen;
    }
    public void onPress()
    {
        Screen oldScreen=Minecraft.getInstance().screen;
        if(oldScreen!=null && oldScreen!=targetScreen)
        {
            oldScreen.onClose();
            Minecraft.getInstance().setScreen(targetScreen);
        }
    }


    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float pPartialTick) {
        super.render(guiGraphics, mouseX, mouseY, pPartialTick);
        {
            if(targetScreen.getTitle()!=Minecraft.getInstance().screen.getTitle())
            {
                super.active=true;
            }
            else
            {
                super.active=false;
            }
        }

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    public Component isTooltipText() {
        return tooltipText;
    }

    public void setTooltipText(Component tooltipText)
    {
        this.setTooltip(Tooltip.create(tooltipText));
    }


    public boolean getViewing() {
        return viewing;
    }

    public void setViewing(boolean viewing) {
        this.viewing = viewing;
    }

}

