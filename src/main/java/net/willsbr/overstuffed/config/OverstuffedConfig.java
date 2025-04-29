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
    public static String lastWeightLayer="";

    public static ForgeConfigSpec.ConfigValue<String> stuffedLayerConfigEntry;
    public static String lastStuffedLayer="";

    public static ForgeConfigSpec.ConfigValue<Boolean> stageGain;

    public static ForgeConfigSpec.ConfigValue<Boolean> weightEffects;

    public static ForgeConfigSpec.ConfigValue<Integer> burpFrequency;
    public static ForgeConfigSpec.ConfigValue<Integer> gurgleFrequency;

    public static ForgeConfigSpec.ConfigValue<Integer> maxWeight;

    public static ForgeConfigSpec.ConfigValue<Integer> minWeight;

    public static ForgeConfigSpec.ConfigValue<Integer> weightDisplayXOffset;
    public static ForgeConfigSpec.ConfigValue<Integer> weightDisplayYOffSet;

    public static ForgeConfigSpec.ConfigValue<Integer> stuffedHudXOffset;
    public static ForgeConfigSpec.ConfigValue<Integer> stuffedHudYOffset;
    public static ForgeConfigSpec.ConfigValue<Boolean> debugView;




    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        builder.comment(" This category holds configs for CPM.");
        builder.push("CPM Config Options");
        weightLayerConfigEntry = builder
                .comment(" Name of CPM Value Layer for Weight.")
                .define("weight_layer_config_entry", "weight");


        stuffedLayerConfigEntry = builder
                .comment(" Name of CPM Value Layer for Stuffed.")
                .define("stuffed_layer_config_entry", "stuffed");

        stageGain=builder.comment("Stage based gaining for those who want more a more sudden change")
                .define("config_stage_gain",false);
        weightEffects=builder.comment("Currently enables/disables all forms of weight effects")
                .define("config_weight_effects",true);

        burpFrequency=builder.comment("1-10, the frequency that burps occur")
                .define("config_burp_frequency,",5);
        gurgleFrequency=builder.comment("1-10, the frequency that gurgles occur")
                .define("config_gurgle_frequency,",3);

        maxWeight=builder.comment("The maximum displayable weight.")
                .define("max_weight,",300);
        minWeight=builder.comment("The minimum displayable weight.")
                .define("min_weight,",100);

        builder.pop();
        builder.comment("This section is for graphics related settings");
        builder.push("Overstuffed Graphics Options");

        weightDisplayXOffset =builder.comment("The weight display's current X position")
                .define("weightdisplayx,",0);

        weightDisplayYOffSet =builder.comment("The weight display's current Y position")
                .define("weightdisplayy,",0);

        stuffedHudXOffset =builder.comment("The stuffed hud's X offset from it's default position")
                .define("stuffedhudx,",0);

        stuffedHudYOffset =builder.comment("The stuffed hud's Y offset from it's default position")
                .define("stuffedhudy,",0);

        debugView =builder.comment("Boolean to determine if the debug view should be on ")
                .define("debugview,",false);

    }

    public static void saveConfig() {
        weightLayerConfigEntry.save();
        stuffedLayerConfigEntry.save();
        burpFrequency.save();
        gurgleFrequency.save();

        maxWeight.save();
        minWeight.save();

        weightDisplayXOffset.save();
        weightDisplayYOffSet.save();

        stuffedHudYOffset.save();
        stuffedHudXOffset.save();
        debugView.save();



    }


    public static void setWeightLayer(String newLayer)
    {
        if(!weightLayerConfigEntry.get().contentEquals("") && !weightLayerConfigEntry.get().contentEquals(newLayer))
        {
            lastWeightLayer=weightLayerConfigEntry.get();
        }
        weightLayerConfigEntry.set(newLayer);
    }
    public static void setStuffedLayer(String newLayer)
    {
        if(!stuffedLayerConfigEntry.get().contentEquals("") && !stuffedLayerConfigEntry.get().contentEquals(newLayer))
        {
            lastStuffedLayer=stuffedLayerConfigEntry.get();
        }
        stuffedLayerConfigEntry.set(newLayer);
    }



    //Created because the figura plugin can't recognize ForgeConfigSpec.ConfigValue<String>
    //At least in common. This is easier than creating a whole seperate section in the forge
    //area of the plugin
    public static int getMaxWeight()
    {
        return maxWeight.get();
    }
    public static int getMinWeight()
    {
        return minWeight.get();
    }


}
