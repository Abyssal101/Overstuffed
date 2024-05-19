package net.willsbr.overstuffed.Menu;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.willsbr.overstuffed.config.OverstuffedConfig;

import java.awt.Color;
import javax.annotation.Nonnull;

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
    private OptionsList optionsList;

    //OPTION INSTANCES ARE BUTTONS
    private OptionInstance stageBasedWeight= OptionInstance.createBoolean("Stage Based Weight", false);

    // ## CPM Value Layers
    // - Stuffed: `____`
    // - Weight: `____`
    // ## Toggles
    // - `[ ]` Stage Based Weight
    // - `[ ]` `<icon>` Weight Based Momentum
    // - `[ ]` `<icon>` Weight Effects

    private EditBox weightLayerEditBox;

    private int centerW;
    private int centerH;
    private int screenW;
    private int screenH;
    private int leftBackX;
    private int leftBackY;

    private Window curWindow;
    private Font font;

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

        screenW = curWindow.getGuiScaledWidth();
        screenH = curWindow.getGuiScaledHeight();
        centerW = screenW / 2;
        centerH = screenH / 2;
        leftBackX = (int) (this.width / 4 * (1 / curWindow.getGuiScale()));
        leftBackY = (int) (this.height / 7 * (1 / curWindow.getGuiScale()));

        this.weightLayerEditBox = new EditBox(
                font,
                leftBackX + (width / 60),
                leftBackY + (height / 40),
                130,
                30,
                Component.literal("Weight Layer"));
        this.weightLayerEditBox.setValue(OverstuffedConfig.weightLayerConfigEntry.get());



        // Create the options list
        // It must be created in this method instead of in the constructor,
        // or it will not be displayed properly
        this.optionsList = new OptionsList(
                Minecraft.getInstance(), this.width, this.height,
                OPTIONS_LIST_TOP_HEIGHT,
                this.height - OPTIONS_LIST_BOTTOM_OFFSET,
                OPTIONS_LIST_ITEM_HEIGHT
        );
        optionsList.addSmall(OptionInstance.createBoolean("test",false),OptionInstance.createBoolean("test2",false));



        // FIXME: Add OptionInstance<String> instead of EditBox?

        // Add the options list as this screen's child
        // If this is not done, users cannot click on items in the list
        this.addRenderableWidget(this.optionsList);

        this.addRenderableWidget(this.weightLayerEditBox);


        // Add the "Done" button
        this.addRenderableWidget(new Button(
                (this.width - BUTTON_WIDTH) / 2,
                this.height - DONE_BUTTON_TOP_OFFSET,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                Component.literal("Done"),
                button -> this.onClose()
        ));
    }

    @Override
    public void tick() {
        super.tick();

        // Add ticking logic for EditBox in editBox
        this.weightLayerEditBox.tick();
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (weightLayerEditBox.isFocused()) {
            // FIXME: caps lock toggle not working?
            System.out.println("keyPressed " + pKeyCode);
            if (!weightLayerEditBox.keyPressed(pKeyCode, pScanCode, pModifiers)) {
                if ((pKeyCode >= 'A' && pKeyCode <= 'Z') || (pKeyCode >= 'a' && pKeyCode <= 'z')) {
                    return weightLayerEditBox.charTyped((char)pKeyCode, pModifiers);
                }
            }
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    // mouseX and mouseY indicate the scaled coordinates of where the cursor is in
    // on the screen
    @Override
    public void render(@Nonnull PoseStack pose, int mouseX, int mouseY, float partialTick) {
        // Background is typically rendered first
        this.renderBackground(pose);
        // Then the widgets if this is a direct child of the Screen
        super.render(pose, mouseX, mouseY, partialTick);
        // Draw the title
        drawCenteredString(pose, font, this.getTitle().getString(),
                this.width / 2, TITLE_HEIGHT, Color.WHITE.hashCode());



        // pose.popPose();
        // Render things after widgets (tooltips)
    }

    public void save() {
        this.onClose();
    }

    @Override
    public void onClose() {
        // Save mod configuration
        OverstuffedConfig.weightLayerConfigEntry.set(this.weightLayerEditBox.getValue());
        OverstuffedConfig.saveConfig();

        // Call last in case it interferes with the override
        super.onClose();
    }

    @Override
    public void removed() {
        // Reset initial states here

        // Call last in case it interferes with the override
        super.removed();
    }
}
