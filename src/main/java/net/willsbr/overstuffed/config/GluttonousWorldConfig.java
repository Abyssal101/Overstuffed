package net.willsbr.overstuffed.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.lang.reflect.Field;

public class GluttonousWorldConfig {
    public static final ForgeConfigSpec GENERAL_SPEC;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }


    public static ForgeConfigSpec.ConfigValue<Integer> amountStuffedLost;
    public static ForgeConfigSpec.ConfigValue<Double> stuffedLostMultiplier;

    public static ForgeConfigSpec.ConfigValue<Integer> calToHungerRate;

    public static ForgeConfigSpec.ConfigValue<Integer> maxHearts;
    public static ForgeConfigSpec.ConfigValue<Double> maxSpeedDecrease;

    public static ForgeConfigSpec.ConfigValue<Integer> multiplierForWGDelay;
    public static ForgeConfigSpec.ConfigValue<Integer> maxWGTickDelay;

    public static ForgeConfigSpec.ConfigValue<Integer> thresholdLoseWeight;
    public static ForgeConfigSpec.ConfigValue<Integer> goldenDietTickDelay;
    public static ForgeConfigSpec.ConfigValue<Integer> maxWeightLossTime;

    public static ForgeConfigSpec.ConfigValue<Integer> baseCalCap;
    public static ForgeConfigSpec.ConfigValue<Integer> absCalCap;
    public static ForgeConfigSpec.ConfigValue<Integer> calCapIncrement;

    public static ForgeConfigSpec.ConfigValue<Double> calConvertedPercentage;
    public static ForgeConfigSpec.ConfigValue<Double> calorieGainMultipler;
    public static ForgeConfigSpec.ConfigValue<Integer> calToWeightRate;



    public static ForgeConfigSpec.ConfigValue<Double> modMetabolismThres;
    public static ForgeConfigSpec.ConfigValue<Double> slowMetabolismThres;

    public static ForgeConfigSpec.ConfigValue<Double> modMetabolismMultiplier;
    public static ForgeConfigSpec.ConfigValue<Double> slowMetabolismMultiplier;


    public static ForgeConfigSpec.ConfigValue<Integer> minCalClearDelay;
    public static ForgeConfigSpec.ConfigValue<Integer> maxCalClearDelay;


    public static ForgeConfigSpec.ConfigValue<Integer> capacityIncreaseInterval;
    public static ForgeConfigSpec.ConfigValue<Integer> intervalIncrease;


    public static ForgeConfigSpec.ConfigValue<Double> blocksPerHeart;
    public static ForgeConfigSpec.ConfigValue<Double> absMaxHitboxIncrease;




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

        stuffedLostMultiplier = builder
                .comment("Multiplier for how quickly players stuffed bar will deplete when hunger isn't full.")
                .define("amount_stuffed_lost", 1.0);

        calToHungerRate = builder
                .comment("How many calories converts into a hunger point")
                .define("food_fill", 6);


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
                .define("max_weight_loss_time", 600);
        baseCalCap=builder
                .comment("The calorie capacity a player will spawn with")
                .define("base_cal_cap", 40);
        absCalCap=builder
                .comment("The absolute maximum calorie capacity a player can obtain through growing their capacity")
                .define("abs_cal_cap", 300);
        calCapIncrement=builder
                .comment("The amount a players cal cap increments when they reach their next calorie lost interval")
                .define("cal_cap_increment", 10);
        calConvertedPercentage=builder
                .comment("The percentage of a players calories that convert to weight every conversion event")
                .define("cal_convert_percent", 0.25);

       calorieGainMultipler=builder
               .comment("The base calorie multiplier a player spawns with")
               .define("base_cal_gain_multiplier", 1.0);

        calToWeightRate=builder
                .comment("The amount of calories that equates to one weight")
                .define("cal_weight_rate", 3);

        modMetabolismThres=builder
                .comment("The percentage threshold that determines when moderate metabolism when a player spawns in")
                .define("moderate_metabolism_threshold", 0.3);
        slowMetabolismThres=builder
                .comment("The percentage threshold that determines when slow metabolism when a player spawns in")
                .define("slow_metabolism_threshold", 0.6);
        modMetabolismMultiplier=builder
                .comment("The percentage of the calories a player has that is added ontop of their current calories for being " +
                        "past this threshold")
                .define("moderate_metabolism_multipler", 1.2);
        slowMetabolismMultiplier=builder
                .comment("The percentage of the calories a player has that is added ontop of their current calories for being " +
                        "past this threshold")
                .define("slow_metabolism_multipler", 1.5);


        minCalClearDelay=builder
                .comment("The min delay in ticks for how long it takes for the players calories to clear")
                .define("min_cal_clear_delay", 20*5);
        maxCalClearDelay=builder
                .comment("The max delay in ticks for how long it takes for the players calories to clear. Adjusting this effects the time gained from all food items")
                .define("max_cal_clear_delay", 20*30);

        capacityIncreaseInterval=builder
                .comment("The base interval for losing calories between increases to a players capacity")
                .define("cap_increases_interval", 30);
        intervalIncrease=builder
                .comment("The amount the threshold of increasing a players calorie capacity is increased by")
                .define("interval_increase", 30);

        blocksPerHeart=builder
                .comment("The size of a hitbox increase to add half a heart to a player")
                .define("blocks_per_heart", 0.5);
        absMaxHitboxIncrease=builder
                .comment("Absolute max increase of hitbox size that players can set to occur")
                .define("abs_max_hitbox_increase", 3.0);

    }

    public static void saveConfig()
    {
        for(Field f : GluttonousWorldConfig.class.getDeclaredFields()){
            try {
                // Check if the field is a ConfigValue
                if (ForgeConfigSpec.ConfigValue.class.isAssignableFrom(f.getType())) {
                    // Make the field accessible if it's not already
                    boolean wasAccessible = f.isAccessible();
                    if (!wasAccessible) {
                        f.setAccessible(true);
                    }

                    // Get the field value
                    Object value = f.get(null); // null because these are static fields

                    // Call save() on the ConfigValue
                    if (value instanceof ForgeConfigSpec.ConfigValue) {
                        ((ForgeConfigSpec.ConfigValue<?>) value).save();
                    }

                    // Restore accessibility
                    if (!wasAccessible) {
                        f.setAccessible(false);
                    }
                }
            } catch (IllegalAccessException e) {
                // Handle exception - you might want to log this
                System.err.println("Error saving config value for field: " + f.getName());
                e.printStackTrace();
            }


        }



    }

}
