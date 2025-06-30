package net.willsbr.overstuffed.Menu.Buttons;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.willsbr.overstuffed.GluttonousGrowth;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class ToggleButton extends AbstractButton {
    private String settingName;
    private boolean setting = true;
    private boolean locked = true;
    private boolean rightSide = false;

    private String tooltipText;

    private static final ResourceLocation lockedIcon = new ResourceLocation(GluttonousGrowth.MODID, "textures/config/lockedicon.png");

    public ToggleButton(int pX, int pY, int pWidth, int pHeight, String pMessage, boolean startVal) {
        super(pX, pY, pWidth, pHeight, Component.literal(pMessage + ":"));
        settingName = pMessage;
        setting = startVal;

        tooltipText ="";

        String statusUpdate = "";
        if (setting) {
            statusUpdate = "True";
        } else {
            statusUpdate = "False";
        }
        super.setMessage(Component.literal(settingName + ": " + statusUpdate));

    }

    public ToggleButton(int pX, int pY, int pWidth, int pHeight, String pMessage, boolean startVal, boolean isRight) {
        super(pX, pY, pWidth, pHeight, Component.literal(pMessage + ":"));
        settingName = pMessage;
        setting = startVal;

        tooltipText = "";


        String statusUpdate = "";
        if (setting) {
            statusUpdate = "True";
        } else {
            statusUpdate = "False";
        }
        super.setMessage(Component.literal(settingName + " " + statusUpdate));
        rightSide = isRight;
    }

    //Component Version of INits
    public ToggleButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, boolean startVal) {
        super(pX, pY, pWidth, pHeight, pMessage);
        settingName = pMessage.getString();

        tooltipText = "";

        setting = startVal;
        Component statusUpdate;
        if (setting) {
            statusUpdate = Component.translatable("debug.overstuffed.true");
        } else {
            statusUpdate = Component.translatable("debug.overstuffed.false");
        }

        super.setMessage(Component.literal(settingName + " " + statusUpdate.getString()));

    }

    public ToggleButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, boolean startVal, boolean isRight) {
        super(pX, pY, pWidth, pHeight, pMessage);
        settingName = pMessage.getString();
        setting = startVal;

        tooltipText = "";


        Component statusUpdate;
        if (setting) {
            statusUpdate = Component.translatable("debug.overstuffed.true");
        } else {
            statusUpdate = Component.translatable("debug.overstuffed.false");
        }
        rightSide = isRight;
        super.setMessage(Component.literal(settingName + " " + statusUpdate.getString()));

    }

    public void onPress() {

        if (locked == false) {

            setting = !setting;
            Component statusUpdate;
            if (setting) {
                statusUpdate = Component.translatable("debug.overstuffed.true");
            } else {
                statusUpdate = Component.translatable("debug.overstuffed.false");

            }

            super.setMessage(Component.literal(settingName + " " + statusUpdate.getString()));


        }

    }


    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float pPartialTick) {
        super.render(guiGraphics, mouseX, mouseY, pPartialTick);
        {
            if (locked) {
                //RenderSystem.setShaderTexture(0, lockedIcon);
                if (rightSide) {

                    guiGraphics.blit(lockedIcon, this.getX() + this.width + 5, this.getY(), 0, 0, 20, 20, 20, 20);

                } else {
                    guiGraphics.blit(lockedIcon, this.getX() - 25, this.getY(), 0, 0, 20, 20, 20, 20);
                }

            }
        }

    }

    public String isTooltipText() {
        return tooltipText;
    }

    public void setTooltipText(String tooltipText) {
        //1.19.2 version is this.tooltip
        //this.tooltipText = tooltipText;
        //Remove this in the
        this.setTooltip(Tooltip.create(Component.literal(tooltipText)));
    }
    public void setTooltipText(Component tooltipText) {
        //1.19.2 version is this.tooltip
        //this.tooltipText = tooltipText;
        //Remove this in the
        this.setTooltip(Tooltip.create(tooltipText));
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }


    //    public void renderButton(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
//        super.renderWidget(guiGraphics,pMouseX,pMouseY,pPartialTick);
//        //this will handle drawing the locked icon next to the button
//        if(locked)
//        {
//            if(rightSide)
//            {
//                guiGraphics.blit(lockedIcon,this.x+this.width+5,this.y,0,0,20 ,20,20,20);
//
//            }
//            else {
//                guiGraphics.blit(lockedIcon,this.getX()-20,this.y,0,0,20,20,20,20);
//
//            }
//        }
//    }
    public void setLocked(boolean input) {
        this.locked = input;
    }


    public boolean getSetting() {
        return setting;
    }

    public void setSetting(boolean setting) {
        this.setting = setting;
    }

}

