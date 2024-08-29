package net.willsbr.overstuffed.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY_OVERSTUFFED = "key.category.overstuffed.overstuffed";
    public static final String KEY_STUFFED_CONFIG ="key.overstuffed.stuffed_config";

    public static final KeyMapping STUFFED_CONFIG=new KeyMapping(KEY_STUFFED_CONFIG, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_O, KEY_CATEGORY_OVERSTUFFED);

}
