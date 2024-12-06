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


    public static ForgeConfigSpec.ConfigValue<String> toggleList;

    public static ForgeConfigSpec.ConfigValue<Integer> burpFrequency;
    public static ForgeConfigSpec.ConfigValue<Integer> gurgleFrequency;

    public static ForgeConfigSpec.ConfigValue<Integer> maxWeight;

    public static ForgeConfigSpec.ConfigValue<Integer> minWeight;

    public static ForgeConfigSpec.ConfigValue<Integer> weightDisplayX;
    public static ForgeConfigSpec.ConfigValue<Integer> weightDisplayY;

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

        //Default Config Values, order mirrors the list found on Github
        //0 1 1
        toggleList=builder.comment("List of the boolean values for the settings")
                .define("config_toggle_values","0 1 1");

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

        weightDisplayX=builder.comment("The weight display's current X position")
                .define("weightdisplayx,",100);

        weightDisplayY=builder.comment("The weight display's current Y position")
                .define("weightdisplayy,",10);

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
        toggleList.save();
        burpFrequency.save();
        gurgleFrequency.save();

        maxWeight.save();
        minWeight.save();

        weightDisplayX.save();
        weightDisplayY.save();

        stuffedHudYOffset.save();
        stuffedHudXOffset.save();

        debugView.save();
    }

    public static boolean returnSetting(int index)
    {
        //Defaults to return false on index not being possible in array, so too high or negative
        String[] tempString=toggleList.get().split(" ");
        if(index>=0 && index<tempString.length)
        {
            int result=Integer.parseInt(tempString[index]);

            if(result==1)
            {
                return true;
            }
            else if(result==0)
            {
                return false;
            }
            else {
                System.out.println("Error in Return Setting");
                return false;
            }

        }
        return false;
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


    public static void setSetting(int index, boolean input)
    {
        String[] tempString=toggleList.get().split(" ");
        if(index>=0 && index<tempString.length)
        {

            if(input)
            {
                tempString[index]="1";
            }
            else
            {
                tempString[index]="0";
            }
            String combine="";
            for(int i=0;i<tempString.length;i++)
            {
                combine=combine+tempString[i]+" ";
            }
            combine.trim();
            toggleList.set(combine);

            toggleList.save();
        }
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
