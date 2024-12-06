package net.willsbr.overstuffed.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;

import static net.minecraft.client.gui.GuiComponent.blit;

public class AbstractClientMethods {
    public static void AbstractDraw(ForgeGui gui, PoseStack poseStack, ResourceLocation texture, int x, int y, int textureWidth, int textureHeight){
        RenderSystem.setShaderTexture(0, texture);
        blit(poseStack,x, y,0,0,0,textureWidth,textureHeight,textureHeight,textureWidth);
    }

}
