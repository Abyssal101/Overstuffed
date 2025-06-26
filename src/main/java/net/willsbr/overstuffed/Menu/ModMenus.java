package net.willsbr.overstuffed.Menu;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.willsbr.overstuffed.Menu.Buttons.SwapScreenButton;

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
        if(!errors.isEmpty())
        {
            guiGraphics.drawCenteredString(font,Component.translatable("menu.overstuffed.errorstitle"),x,y, Color.RED.hashCode());
            y=y+spacingbetween;
            guiGraphics.fill(x-width/2-outlineWidth,y-outlineWidth,x+width/2+outlineWidth,y+errors.size()*spacingbetween+outlineWidth,Color.GRAY.hashCode());
            guiGraphics.fill(x-width/2,y,x+width/2,y+errors.size()*spacingbetween,Color.BLACK.hashCode());
            for(int i=0;i<errors.size();i++)
            {
                guiGraphics.drawCenteredString(font,errors.get(i),x,y+i*spacingbetween+outlineWidth,Color.RED.hashCode());
            }
            y=y+errors.size()*spacingbetween+5;
        }
        if(!warnings.isEmpty())
        {
            guiGraphics.drawCenteredString(font,Component.translatable("menu.overstuffed.warningstitle"),x,y,Color.YELLOW.hashCode());
            y=y+12;
            guiGraphics.fill(x-width/2-outlineWidth,y-outlineWidth,x+width/2+outlineWidth,y+warnings.size()*spacingbetween+outlineWidth,Color.GRAY.hashCode());
            guiGraphics.fill(x-width/2,y,x+width/2,y+warnings.size()*spacingbetween,Color.BLACK.hashCode());
            for(int i=0;i<warnings.size();i++)
            {
                guiGraphics.drawCenteredString(font,warnings.get(i),x,y+i*spacingbetween+outlineWidth,Color.YELLOW.hashCode());
            }
        }
        errors.clear();
        warnings.clear();
    }

}
