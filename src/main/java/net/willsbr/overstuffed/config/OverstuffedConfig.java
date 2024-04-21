package net.willsbr.overstuffed.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class OverstuffedConfig {
    public static final ForgeConfigSpec GENERAL_SPEC;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    public static ForgeConfigSpec.ConfigValue<String> weightLayerConfigEntry;
    public static ForgeConfigSpec.ConfigValue<String> stuffedLayerConfigEntry;

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        builder.comment(" This category holds configs for CPM.");
        builder.push("CPM Config Options");
        weightLayerConfigEntry = builder
                .comment(" Name of CPM Value Layer for Weight.")
                .define("weight_layer_config_entry", "weight");
        stuffedLayerConfigEntry = builder
                .comment(" Name of CPM Value Layer for Stuffed.")
                .define("stuffed_layer_config_entry", "stuffed");
        builder.pop();
    }

    public static void saveConfig() {
        weightLayerConfigEntry.save();
        stuffedLayerConfigEntry.save();
    }
}
