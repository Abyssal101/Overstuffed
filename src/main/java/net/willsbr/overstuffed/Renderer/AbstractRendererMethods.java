package net.willsbr.overstuffed.Renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;

import java.awt.*;

public class AbstractRendererMethods {

    public static void drawInBatch(Font font, String text, float x, float y, Color color, PoseStack poseStack, MultiBufferSource pBufferSource, int packedLight)
    {

        //1.19.2
        font.drawInBatch(text,
                x,
                y,
                color.hashCode(),
                false,
                poseStack.last().pose(),
                pBufferSource,
                false,
                0,
                packedLight);

        //1.20.1
//        font.drawInBatch(
//                text,
//                x,
//                y,
//                color.hashCode(),
//                false,
//                poseStack.last().pose(),
//                pBufferSource,
//                Font.DisplayMode.NORMAL,
//                0,
//                packedLight);
    }


    //In 1.19.2 this must return Quaternions, which is done through Vector3f.XN.rotationDegrees(45) for exaple
    //in 1.20.1 idk what it must return, but the method must be done through Axis.XN.rotationDegrees(-45)
    public static Quaternion abstractRotation(int amount, String axis)
    {
        if(axis.contentEquals("X"))
        {
            return Vector3f.XN.rotationDegrees(amount);
        }
        else if(axis.contentEquals("Y")) {
            return Vector3f.YN.rotationDegrees(amount);

        }
        else if(axis.contentEquals("Z")) {
            return Vector3f.ZN.rotationDegrees(amount);
        }
        else{
            //System.out.println("");
            return Vector3f.XN.rotationDegrees(0);

        }



    }




}
