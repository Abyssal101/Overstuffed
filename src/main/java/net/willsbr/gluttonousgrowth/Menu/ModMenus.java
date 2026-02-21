package net.willsbr.gluttonousgrowth.Menu;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.willsbr.gluttonousgrowth.Menu.Buttons.SwapScreenButton;

import java.awt.*;
import java.util.ArrayList;

public class ModMenus {

//    public static final RegistryObject<MenuType<ConfigMenu>> CONFIG_MENU = REGISTER.register("config_menu",
//            () -> new MenuType(ConfigMenu::new, FeatureFlags.DEFAULT_FLAGS));
    private static SwapScreenButton main;
    public static Component mainTitle=Component.translatable("menu.overstuffed.main").withStyle(ChatFormatting.BLUE);



    private static SwapScreenButton advanced;
    public static Component advancedTitle=Component.translatable("menu.overstuffed.advanced").withStyle(ChatFormatting.BLUE);

    private static SwapScreenButton client;
    public static Component clientTitle=Component.translatable("menu.overstuffed.client").withStyle(ChatFormatting.BLUE);

    private static int buttonGap=4;

    public static ArrayList<SwapScreenButton> returnScreenButtons(int x, int y)
    {
        main= new SwapScreenButton(x,y,Component.translatable("menu.overstuffed.main"),new ConfigScreen()
                ,Component.translatable("menu.overstuffed.maintooltip"));

        advanced= new SwapScreenButton(x,y,Component.translatable("menu.overstuffed.advanced")
                ,new AdvancedConfigScreen(),Component.translatable("menu.overstuffed.advancedtooltip"));

        client= new SwapScreenButton(x,y,Component.translatable("menu.overstuffed.client")
                ,new ClientConfigScreen(),Component.translatable("menu.overstuffed.clienttooltip"));
        ArrayList<SwapScreenButton> buttons= new ArrayList<>();
        buttons.add(main);
        buttons.add(advanced);
        buttons.add(client);

        int totalLength=buttonGap*buttons.size()+buttons.size()*SwapScreenButton.WIDTH;
        x=x-totalLength/2;
        for(int i=0;i<buttons.size();i++)
        {
            if(i!=0)
            {
                buttons.get(i).setX(x+buttonGap*i+i*SwapScreenButton.WIDTH);
            }
            else
            {
                buttons.get(i).setX(x);
            }
        }

        return buttons;
    }
    public static void drawIssues(GuiGraphics guiGraphics,Font font, int x, int y, ArrayList<Component> errors, ArrayList<Component> warnings)
    {
        int outlineWidth=2;
        int width=200;
        int spacingbetween=11;

        int totalErrorLength=(errors.size()+warnings.size())*spacingbetween+10;

        int baseY=y;
        int warningX=x;
        int errorX=x;
        Component warningText=Component.translatable("menu.overstuffed.warningstitle");

        if((y+totalErrorLength)>(Minecraft.getInstance().screen.height-ConfigScreen.DONE_BUTTON_TOP_OFFSET))
        {
            warningX=x+Minecraft.getInstance().screen.width/2+Minecraft.getInstance().screen.width/12;
            errorX=x-Minecraft.getInstance().screen.width/4;
            y=y-(y+totalErrorLength-Minecraft.getInstance().screen.height-ConfigScreen.DONE_BUTTON_TOP_OFFSET+20);
            baseY=y;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(0.75f,0.75f,1);
            warningText=Component.translatable("menu.gluttonousgrowth.shortwarning");
        }


        if(!errors.isEmpty())
        {
            guiGraphics.drawCenteredString(font,Component.translatable("menu.overstuffed.errorstitle"),errorX,y, Color.RED.hashCode());
            y=y+spacingbetween;
            guiGraphics.fill(errorX-width/2-outlineWidth,y-outlineWidth,errorX+width/2+outlineWidth,y+errors.size()*spacingbetween+outlineWidth,Color.GRAY.hashCode());
            guiGraphics.fill(errorX-width/2,y,errorX+width/2,y+errors.size()*spacingbetween,Color.BLACK.hashCode());
            for(int i=0;i<errors.size();i++)
            {
                guiGraphics.drawCenteredString(font,errors.get(i),errorX,y+i*spacingbetween+outlineWidth,Color.RED.hashCode());
            }
            y=y+errors.size()*spacingbetween+5;
            if(warningX!=errorX)
            {
                y=baseY;
            }
        }

        if(!warnings.isEmpty())
        {
            guiGraphics.drawCenteredString(font,warningText,warningX,y,Color.YELLOW.hashCode());
            y=y+12;
            guiGraphics.fill(warningX-width/2-outlineWidth,y-outlineWidth,warningX+width/2+outlineWidth,y+warnings.size()*spacingbetween+outlineWidth,Color.GRAY.hashCode());
            guiGraphics.fill(warningX-width/2,y,warningX+width/2,y+warnings.size()*spacingbetween,Color.BLACK.hashCode());
            for(int i=0;i<warnings.size();i++)
            {
                guiGraphics.drawCenteredString(font,warnings.get(i),warningX,y+i*spacingbetween+outlineWidth,Color.YELLOW.hashCode());
            }
        }
        if(warningX!=errorX)
        {
            guiGraphics.pose().popPose();
        }

        errors.clear();
        warnings.clear();
    }

}
