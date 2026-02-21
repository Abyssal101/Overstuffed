package net.willsbr.gluttonousgrowth.Menu.Buttons;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.MutableComponent;

import javax.annotation.Nonnull;

public class PortProofButton extends AbstractButton {

    Runnable onPressFunction;

    public PortProofButton(int pX, int pY, int pWidth, int pHeight, MutableComponent text,Runnable func) {
        super(pX, pY, pWidth, pHeight, text );
        onPressFunction=func;

    }


    public void onPress() {
        onPressFunction.run();
    }


    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float pPartialTick) {

        super.render(guiGraphics, mouseX, mouseY, pPartialTick);
    }


    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
