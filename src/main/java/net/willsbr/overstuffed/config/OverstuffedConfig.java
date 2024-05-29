package net.willsbr.overstuffed.config;

import com.sun.jdi.connect.Connector;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Collection;

public class OverstuffedConfig {
    public static final ForgeConfigSpec GENERAL_SPEC;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    public static ForgeConfigSpec.ConfigValue<String> weightLayerConfigEntry;
    public static ForgeConfigSpec.ConfigValue<String> stuffedLayerConfigEntry;

    public static ForgeConfigSpec.ConfigValue<String> toggleList;


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

        builder.pop();




    }

    public static void saveConfig() {
        weightLayerConfigEntry.save();
        stuffedLayerConfigEntry.save();
        toggleList.save();
    }

    public static boolean returnSetting(int index)
    {
        //Defaults to return false on index not being possible in array, so too high or negative
        String[] tempString=toggleList.get().split(" ");
        if(index>=0 && index<tempString.length)
        {
            System.out.println(tempString[index]+"FUcken");
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
            System.out.println("TEmpINDEX"+tempString[index]);
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



}
