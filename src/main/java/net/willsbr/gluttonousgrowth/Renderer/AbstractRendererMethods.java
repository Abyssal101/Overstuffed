package net.willsbr.gluttonousgrowth.Renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Quaternionf;


import java.awt.*;

public class AbstractRendererMethods {

    public static void drawInBatch(Font font, String text, float x, float y, Color color, PoseStack poseStack, MultiBufferSource pBufferSource, int packedLight)
    {

        //1.19.2
//        font.drawInBatch(text,
//                x,
//                y,
//                color.hashCode(),
//                false,
//                poseStack.last().pose(),
//                pBufferSource,
//                false,
//                0,
//                packedLight);

        //1.20.1
        font.drawInBatch(
                text,
                x,
                y,
                color.hashCode(),
                false,
                poseStack.last().pose(),
                pBufferSource,
                Font.DisplayMode.NORMAL,
                0,
                packedLight);
    }


    //In 1.19.2 this must return Quaternions, which is done through Vector3f.XN.rotationDegrees(45) for exaple
    //in 1.20.1 idk what it must return, but the method must be done through Axis.XN.rotationDegrees(-45)
    public static Quaternionf abstractRotation(int amount, String axis)
    {
        if(axis.contentEquals("X"))
        {

            return Axis.XN.rotationDegrees(amount);
        }
        else if(axis.contentEquals("Y")) {
            return Axis.YN.rotationDegrees(amount);

        }
        else if(axis.contentEquals("Z")) {
            return Axis.ZN.rotationDegrees(amount);
        }
        else{
            //System.out.println("");
            return Axis.XN.rotationDegrees(0);

        }



    }




}
