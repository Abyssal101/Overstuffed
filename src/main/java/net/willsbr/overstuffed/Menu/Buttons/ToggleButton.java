package net.willsbr.overstuffed.Menu.Buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.willsbr.overstuffed.OverStuffed;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class ToggleButton extends AbstractButton {
    private String settingName;
    private boolean setting = true;
    private boolean locked = true;
    private boolean rightSide = false;

    private String tooltipText;

    private static final ResourceLocation lockedIcon = new ResourceLocation(OverStuffed.MODID, "textures/config/lockedicon.png");

    public ToggleButton(int pX, int pY, int pWidth, int pHeight, String pMessage, boolean startVal) {
        super(pX, pY, pWidth, pHeight, Component.literal(pMessage + ":"));
        settingName = pMessage;
        setting = startVal;

        tooltipText = pMessage;

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

        tooltipText = pMessage;


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

        tooltipText = settingName;

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

        tooltipText = settingName;


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
    public void render(@Nonnull PoseStack pose, int mouseX, int mouseY, float pPartialTick) {
        super.render(pose, mouseX, mouseY, pPartialTick);
        {
            if (locked) {
                RenderSystem.setShaderTexture(0, lockedIcon);
                if (rightSide) {

                    GuiComponent.blit(pose, this.x + this.width + 5, this.y, 0, 0, 20, 20, 20, 20);

                } else {
                    GuiComponent.blit(pose, this.x - 25, this.y, 0, 0, 20, 20, 20, 20);
                }

            }
            boolean flag = this.isHovered && !tooltipText.contentEquals("");

            if (flag) {
                Minecraft.getInstance().screen.renderTooltip(pose, Component.literal(this.tooltipText), mouseX, mouseY);


            }
        }

    }

    public String isTooltipText() {
        return tooltipText;
    }

    public void setTooltipText(String tooltipText) {
        this.tooltipText = tooltipText;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {

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

