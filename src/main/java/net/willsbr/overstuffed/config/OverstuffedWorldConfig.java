package net.willsbr.overstuffed.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class OverstuffedWorldConfig {
    public static final ForgeConfigSpec GENERAL_SPEC;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }


    public static ForgeConfigSpec.ConfigValue<Integer> amountStuffedLost;
    public static ForgeConfigSpec.ConfigValue<Double> stuffedLossedMultiplier;

    public static ForgeConfigSpec.ConfigValue<Integer> foodFill;

    public static ForgeConfigSpec.ConfigValue<Integer> maxHearts;
    public static ForgeConfigSpec.ConfigValue<Double> maxSpeedDecrease;

    public static ForgeConfigSpec.ConfigValue<Integer> multiplierForWGDelay;
    public static ForgeConfigSpec.ConfigValue<Integer> maxWGTickDelay;


    public static ForgeConfigSpec.ConfigValue<Integer> thresholdLoseWeight;
    public static ForgeConfigSpec.ConfigValue<Integer> goldenDietTickDelay;
    public static ForgeConfigSpec.ConfigValue<Integer> maxWeightLossTime;






    private static void setupConfig(ForgeConfigSpec.Builder builder)
    {
//        builder.comment(" This category holds configs for CPM.");
//        builder.push("CPM Config Options");
//        weightLayerConfigEntry = builder
//                .comment(" Name of CPM Value Layer for Weight.")
//                .define("weight_layer_config_entry", "weight");
        amountStuffedLost = builder
                .comment("Represents the baseline for how many points of stuffed you lose with every lost event")
                .define("stuffed_loss_multiplier", 1);

        stuffedLossedMultiplier = builder
                .comment("Multiplier for how quickly players stuffed bar will deplete when hunger isn't full.")
                .define("amount_stuffed_lost", 1.0);

        foodFill = builder
                .comment("How much of your hunger bar do you get back per x stuffed point you lose")
                .define("food_fill", 1);


        maxHearts=builder
                .comment("Max increase in hearts for all players at max weight")
                .define("max_hearts", 8);

        maxSpeedDecrease=builder
                .comment("Max Speed Decrease for all players at max weight")
                .define("max_speed_decrease", 0.3);

        multiplierForWGDelay=builder
                .comment("Multiplier of ticks to create the delay between eating food and gaining weight. Lower value will make players gain weight faster")
                .define("wg_delay", 10);
        maxWGTickDelay=builder
                .comment("Number of ticks that define the maximum delay a food item can have, to prevent modded foods from taking 15 years to gain weight from ")
                .define("max_wg_delay", 1000);

        thresholdLoseWeight=builder
                .comment("Amount of hunger a player needs to drop below in order to begin losing weight")
                .define("lose_threshold", 18);
        goldenDietTickDelay=builder
                .comment("Number of ticks that define how quickly a player loses weight from the golden diet effect as a baseline")
                .define("golden_diet_delay", 20);
        maxWeightLossTime=builder
                .comment("This effects all weight loss(except golden diet) as the time a player takes to lose weight is subtracted from this number based off their exhaustion level and a multipler. Effectively, higher number means all players lose weight slower")
                .define("max_weight_loss_time", 200)

        ;

    }

    public static void saveConfig()
    {

    }

}
