package net.willsbr.overstuffed.Menu.Buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.types.Func;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.willsbr.overstuffed.OverStuffed;

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
