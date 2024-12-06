package net.willsbr.overstuffed.Menu;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.willsbr.overstuffed.CPMCompat.Capability.CPMData;
import net.willsbr.overstuffed.Menu.Buttons.OptionSlider;
import net.willsbr.overstuffed.Menu.Buttons.ToggleButton;
import net.willsbr.overstuffed.client.ClientCPMData;
import net.willsbr.overstuffed.client.ClientWeightBarData;
import net.willsbr.overstuffed.config.OverstuffedConfig;
import net.willsbr.overstuffed.networking.ModMessages;
import net.willsbr.overstuffed.networking.packet.SettingPackets.PlayerToggleUpdateBooleanC2S;
import net.willsbr.overstuffed.networking.packet.WeightPackets.setMaxWeightDataSyncPacketC2S;
import net.willsbr.overstuffed.networking.packet.WeightPackets.setMinWeightDataSyncPacketC2S;
import net.willsbr.overstuffed.networking.packet.WeightPackets.setWeightC2SPacket;

import java.awt.Color;

public class ConfigScreen extends Screen {
    /** Distance from top of the screen to this GUI's title */
    private static final int TITLE_HEIGHT = 8;

    /** Distance from top of the screen to the options list's top */
    private static final int OPTIONS_LIST_TOP_HEIGHT = 24;
    /** Distance from bottom of the screen to the options list's bottom */
    private static final int OPTIONS_LIST_BOTTOM_OFFSET = 32;
    /** Height of each item in the options list */
    private static final int OPTIONS_LIST_ITEM_HEIGHT = 25;

    /** Width of a button */
    private static final int BUTTON_WIDTH = 200;
    /** Height of a button */
    private static final int BUTTON_HEIGHT = 20;
    /** Distance from bottom of the screen to the "Done" button's top */
    private static final int DONE_BUTTON_TOP_OFFSET = 26;

    /** List of options shown on the screen */
    // Not a final field because this cannot be initialized in the constructor,
    // as explained below

    private ToggleButton stageBasedWeight;

    private ToggleButton weightEffect;

    private ToggleButton momentum;

    private OptionSlider burpFrequency;

    private OptionSlider gurgleFrequency;

    private EditBox maxWeight;
    private EditBox minWeight;





    // ## CPM Value Layers
    // - Stuffed: `____`
    // - Weight: `____`
    // ## Toggles
    // - `[ ]` Stage Based Weight
    // - `[ ]` `<icon>` Weight Based Momentum
    // - `[ ]` `<icon>` Weight Effects

    private EditBox weightLayerEditBox;
    private EditBox stuffedLayerEditBox;

    private Button toGraphicsConfig;
    private Button done;


    private int centerW;
    private int centerH;
    private int screenW;
    private int screenH;
    private int leftBackX;
    private int leftBackY;

    private Window curWindow;
    private Font font;

    private int lastGuiScale;

    public ConfigScreen() {
        super(Component
                .literal("Overstuffed Config Menu")
                .withStyle(ChatFormatting.BLUE));
    }

    // this should initialize most things, gets ercalled on screen change.
    @SuppressWarnings("resource")
    @Override
    protected void init() {
        super.init();

        curWindow = Minecraft.getInstance().getWindow();
        font = Minecraft.getInstance().font;
        lastGuiScale=(int)curWindow.getGuiScale();
        screenW = curWindow.getGuiScaledWidth();
        screenH = curWindow.getGuiScaledHeight();
        centerW = screenW / 2;
        centerH = screenH / 2;
        leftBackX = (int) (this.width / 4 * (1 / curWindow.getGuiScale()));
        leftBackY = (int) (this.height / 7 * (1 / curWindow.getGuiScale()));






        // Create the options list
        // It must be created in this method instead of in the constructor,
        // or it will not be displayed properly

        //buttons
        //TODO MAKE LOCKED BUTTONS ACTUAL TIE TO PLAYER UNLOCK
        this.stageBasedWeight= new ToggleButton(centerW-160,70,150,20,Component.translatable("menu.overstuffed.stagebasedweightbutton"),OverstuffedConfig.returnSetting(0));
        this.stageBasedWeight.setLocked(false);
        this.momentum= new ToggleButton(centerW+10,70,150,20,Component.translatable("menu.overstuffed.weightmomentumbutton"),OverstuffedConfig.returnSetting(1), true);
        this.weightEffect= new ToggleButton(centerW+-160,100,150,20,Component.translatable("menu.overstuffed.weighteffectsbutton"),OverstuffedConfig.returnSetting(2));

        //TODO MAKE the Sliders have translateable components

        this.burpFrequency = new OptionSlider(centerW+10,100,150,20,Component.literal("Burp Frequency"),OverstuffedConfig.burpFrequency.get()*0.1);
        this.gurgleFrequency = new OptionSlider(centerW+10,130,150,20,Component.literal("Gurgle Frequency"),OverstuffedConfig.gurgleFrequency.get()*0.1);
        this.momentum.setLocked(true);
        this.weightEffect.setLocked(true);
        //TODO REMAKE Tooltip from 1.20.1 by trying to copy the previous code
//        this.stageBasedWeight.setTooltip(Tooltip.create(Component.literal("False: Weight visually udates with every tick. \nTrue: Weight visually updates once you reach every 20% weight interval.")));
//        this.momentum.setTooltip(Tooltip.create(Component.literal("Locked: Planned Feature")));
//        this.weightEffect.setTooltip(Tooltip.create(Component.literal("Locked: Planned Feature")));

        //ALL editbox sizes are based off this first editbox.
        this.weightLayerEditBox = new EditBox(
                font,
                centerW - (width / 60)-150,
                160,
                130,
                25,
                Component.literal("Weight Layer"));
        this.weightLayerEditBox.setValue(OverstuffedConfig.weightLayerConfigEntry.get());

        this.stuffedLayerEditBox = new EditBox(
                font,
                weightLayerEditBox.x,
                weightLayerEditBox.y+weightLayerEditBox.getHeight()+15*2,
                weightLayerEditBox.getWidth(),
                weightLayerEditBox.getHeight(),
                Component.literal("Stuffed Layer"));
        this.stuffedLayerEditBox.setValue(OverstuffedConfig.stuffedLayerConfigEntry.get());

        this.maxWeight= new EditBox(
                font,
                this.centerW+105-50,
                stuffedLayerEditBox.y+65,
                50,
                weightLayerEditBox.getHeight(),
                Component.literal("Max Weight"));
        this.maxWeight.setValue(OverstuffedConfig.maxWeight.get()+"");
        //So you can't go above 9999 because of this
        this.maxWeight.setMaxLength(4);

        this.minWeight= new EditBox(
                font,
                this.centerW-105,
                stuffedLayerEditBox.y+65,
                50,
                weightLayerEditBox.getHeight(),
                Component.literal("Min Weight"));
        this.minWeight.setValue(OverstuffedConfig.minWeight.get()+"");
        //So you can't go above 9999 because of this
        this.minWeight.setMaxLength(4);


        //TODO FINISH PORTPROOFBUTTON (AND FIND AN EASIER NAME) AND APPLY IT TO SAVE AND TOGRAPHICS
        toGraphicsConfig=new Button(screenW-120,8,BUTTON_WIDTH/2,BUTTON_HEIGHT,
                Component.literal("Graphics Config")
                , button ->{
            this.swapScreen("graphics");
        });
//        PortProofButton test= new PortProofButton(0,600,
//                BUTTON_WIDTH-20,BUTTON_HEIGHT,Component.literal("Test"), this::onClose);


        // Add the options list as this screen's child
        // If this is not done, users cannot click on items in the list
       //this.addRenderableWidget(this.optionsList);
        this.addRenderableWidget(stageBasedWeight);
        this.addRenderableWidget(momentum);
        this.addRenderableWidget(weightEffect);
        this.addRenderableWidget(burpFrequency);
        this.addRenderableWidget(gurgleFrequency);
        this.addRenderableWidget(maxWeight);
        this.addRenderableWidget(minWeight);

        this.addRenderableWidget(this.weightLayerEditBox);
        this.addRenderableWidget(this.stuffedLayerEditBox);

        this.addRenderableWidget(this.toGraphicsConfig);
        //TODO ERROR FOR AN BAD ANIMATION DOESN'T WORK(Might be because you had no animation loaded)

        // Add the "Done" button
        this.done= new Button(
                (this.width - BUTTON_WIDTH) / 2,
                this.height - DONE_BUTTON_TOP_OFFSET,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                Component.literal("Save"),
                button -> this.onClose()
        );

        this.addRenderableWidget(done);

    }

    @Override
    public void tick() {
        super.tick();
        // Add ticking logic for EditBox in editBox
        this.weightLayerEditBox.tick();
        this.stuffedLayerEditBox.tick();
    }

    // mouseX and mouseY indicate the scaled coordinates of where the cursor is in
    // on the screen
    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        // Background is typically rendered first
        this.renderBackground(poseStack);
        // Then the widgets if this is a direct child of the Screen
        super.render(poseStack, mouseX, mouseY, partialTick);
        // Draw the title
        drawCenteredString(poseStack, font, this.getTitle().getString(), this.width / 2, TITLE_HEIGHT, Color.WHITE.hashCode());
        //drawing the edit box's title

        drawCenteredString(poseStack,font, "Weight Layer", this.width/ 2+25,weightLayerEditBox.y,Color.white.hashCode());
        drawCenteredString(poseStack,font, "Name of value layer for weight animations", this.width/2+100,weightLayerEditBox.y+10,Color.GRAY.hashCode());

        if(ClientCPMData.getPlayersAPI().getAnimationPlaying(this.weightLayerEditBox.getValue())==-1)
        {
            drawCenteredString(poseStack,font, "Error: Weight Layer inputted was not found", this.width/ 2+100,weightLayerEditBox.y+20,Color.RED.hashCode());

        }

        drawCenteredString(poseStack,font, "Stuffed Layer", this.width/ 2+25,stuffedLayerEditBox.y,Color.white.hashCode());
        drawCenteredString(poseStack,font, "Name of value layer for stuffed animations", this.width/ 2+100,stuffedLayerEditBox.y+10,Color.GRAY.hashCode());

        drawCenteredString(poseStack,font, "Max Weight", centerW+80,stuffedLayerEditBox.y+40,Color.WHITE.hashCode());
        drawCenteredString(poseStack,font, "Range:0-9999", centerW+80,stuffedLayerEditBox.y+50,Color.GRAY.hashCode());

        drawCenteredString(poseStack,font, "Min Weight", centerW-80,stuffedLayerEditBox.y+40,Color.WHITE.hashCode());
        drawCenteredString(poseStack,font, "Range:0-9999", centerW-80,stuffedLayerEditBox.y+50,Color.GRAY.hashCode());

        //TODO FIX ERROR CODES FOR THE RANGE OF WEIGHT BEING OPPOSITE,NO DIFFERENCE AND ETC
        if(stuffedLayerEditBox.getValue().contentEquals(weightLayerEditBox.getValue()))
        {
            drawCenteredString(poseStack,font, "Error: Stuffed and Weight Layer Same", this.width/ 2-40,stuffedLayerEditBox.y-20,Color.RED.hashCode());
        }

        // pose.popPose();
        // Render things after widgets (tooltips)
    }

    public void save() {
        this.onClose();
    }

    @Override
    public void onClose() {
        // Save mod configuration
        if(!OverstuffedConfig.weightLayerConfigEntry.get().contentEquals(this.weightLayerEditBox.getValue()))
        {
            OverstuffedConfig.setWeightLayer(this.weightLayerEditBox.getValue());
        }
        if(!OverstuffedConfig.stuffedLayerConfigEntry.get().contentEquals(this.stuffedLayerEditBox.getValue()))
        {
            OverstuffedConfig.setStuffedLayer((this.stuffedLayerEditBox.getValue()));
        }

        OverstuffedConfig.setSetting(0, stageBasedWeight.getSetting());
        ModMessages.sendToServer(new PlayerToggleUpdateBooleanC2S(0,stageBasedWeight.getSetting()));

        OverstuffedConfig.setSetting(1, momentum.getSetting());
        OverstuffedConfig.setSetting(2, weightEffect.getSetting());
        System.out.println(burpFrequency.getValue()+"Burp");
        OverstuffedConfig.burpFrequency.set(burpFrequency.getValue());
        OverstuffedConfig.gurgleFrequency.set(gurgleFrequency.getValue());

        int max;
        int min;
        try{
            max=Integer.parseInt(maxWeight.getValue());
            min=Integer.parseInt(minWeight.getValue());
            if(minWeight.getValue()!=maxWeight.getValue() && max>min)
            {
                if(max-min>100)
                {
                    //System.out.println("CLient Weight:"+ClientWeightBarData.getPlayerWeight());
                    //System.out.println("CLient Min Weight:"+min);

                    double weightRatio=((double)ClientWeightBarData.getPlayerWeight()-OverstuffedConfig.minWeight.get());
                    weightRatio=weightRatio/(OverstuffedConfig.maxWeight.get()-OverstuffedConfig.minWeight.get());
                    //System.out.println("Ratio"+weightRatio);
                    int newRange=max-min;

                    int relativeWeight=(int)Math.round(weightRatio*newRange)+min;
                    ClientWeightBarData.setCurrentWeight(relativeWeight);
                    //System.out.println(ClientWeightBarData.getPlayerWeight());
                    ModMessages.sendToServer(new setWeightC2SPacket(ClientWeightBarData.getPlayerWeight()));

                    OverstuffedConfig.maxWeight.set(max);
                    OverstuffedConfig.minWeight.set(min);
                    ModMessages.sendToServer(new setMinWeightDataSyncPacketC2S(min));
                    ModMessages.sendToServer(new setMaxWeightDataSyncPacketC2S(max));
                }
                else {
                    Minecraft.getInstance().player.sendSystemMessage(Component.literal("Error: The range between your max and min " +
                            "eight is too low. Must be at least 100"));
                }


            }
            else
            {
                Minecraft.getInstance().player.sendSystemMessage(Component.literal("Error: Values are the same or min is greater than max"));
            }
        }
        catch (Exception e)
        {
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Error: Non-Number Character contained in the weight box"));
        }

        OverstuffedConfig.saveConfig();
        if(CPMData.checkIfUpdateCPM("weight")==false)
        {
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Error: CPM is not loaded. No visual changes can occur"));
        }

        CPMData.checkIfUpdateCPM("stuffed");


        // Call last in case it interferes with the override
        super.onClose();
    }
    public void swapScreen(String screenName)
    {
        this.onClose();
        if(screenName.contentEquals("graphics"))
        {
            Minecraft.getInstance().setScreen(new GraphicsConfigScreen());
        }
    }


    @Override
    public void removed() {
        // Reset initial states here
        // Call last in case it interferes with the override
        super.removed();
    }
}
