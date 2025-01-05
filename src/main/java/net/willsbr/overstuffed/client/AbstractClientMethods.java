package net.willsbr.overstuffed.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;

public class AbstractClientMethods {
    public static void AbstractDraw(ForgeGui gui, PoseStack poseStack, ResourceLocation texture, int x, int y, int textureWidth, int textureHeight){
        RenderSystem.setShaderTexture(0, texture);
        //gui.blit(poseStack,x, y,0,0,0,textureWidth,textureHeight,textureHeight,textureWidth);
    }
    public static void AbstractDraw(ForgeGui gui, GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int textureWidth, int textureHeight){
        guiGraphics.blit(texture,x, y,0,0,0,textureWidth,textureHeight,textureWidth,textureHeight);
    }
    public static void AbstractDraw(ForgeGui gui, GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int textureWidth
            , int textureHeight,int uvWidth, int uvHeight){
        guiGraphics.blit(texture,x, y,0,0,0,textureWidth,textureHeight,uvWidth,uvHeight);
    }

}
