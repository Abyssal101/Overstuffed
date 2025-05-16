package net.willsbr.overstuffed.WeightSystem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.RegistryObject;
import net.willsbr.overstuffed.config.OverstuffedConfig;
import net.willsbr.overstuffed.networking.ModMessages;
import net.willsbr.overstuffed.networking.packet.WeightPackets.WeightMaxMinPollS2C;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerWeightBar {
    //

    private int minWeight= OverstuffedConfig.minWeight.get();
    private int currentWeight;

    //this is what options will determine for display
    private int curMaxWeight=OverstuffedConfig.getMaxWeight();

    //this is what should be used to set curMaxWeight, infinite options is hard to balance
    //food queue situation so you slowly gain weight
    private int queuedWeight;

    private ArrayList<Integer> weightChanges= new ArrayList<Integer>();

    private boolean readyToUpdateWeight=true;
    private long savedTickForWeight;

    private int weightUpdateDelay=40;
    private double weightUpdateDelayModifier=1;


    private int weightLossDelay;
    private long savedTickforWeightLoss;

    //this is the boolean controlling if it's gradual or if it rapidly increases in stages
    private int lastWeightStage;

    private int amountThroughStage;
    private static final AttributeModifier WEIGHT_HEALTH_MODIFIER_1 = new AttributeModifier(UUID.fromString("31580520-a812-447f-89d6-8bd82cf790ed"), "health from stage 1 weight", 2, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier WEIGHT_HEALTH_MODIFIER_2 = new AttributeModifier(UUID.fromString("28571767-3553-459f-82bd-4ac9dc94378a"), "health from stage 2 weight", 4, AttributeModifier.Operation.ADDITION);

    private static final AttributeModifier WEIGHT_HEALTH_MODIFIER_3 = new AttributeModifier(UUID.fromString("fc0e7b27-2e4f-4add-a0a8-e87b4efcfbbf"), "health from stage 3 weight", 6, AttributeModifier.Operation.ADDITION);

    private static final AttributeModifier WEIGHT_HEALTH_MODIFIER_4 = new AttributeModifier(UUID.fromString("3c6abd60-0939-4fac-9d84-c280edc5c409"), "health from stage 4 weight", 10, AttributeModifier.Operation.ADDITION);

    public static AttributeModifier[] WEIGHT_HEALTH_MODIFIERS= {WEIGHT_HEALTH_MODIFIER_1,WEIGHT_HEALTH_MODIFIER_2,WEIGHT_HEALTH_MODIFIER_3,WEIGHT_HEALTH_MODIFIER_4};
    private static final AttributeModifier WEIGHT_SPEED_MODIFIER_1 = new AttributeModifier(UUID.fromString("65d64bf1-2703-458d-a799-3d06b1e3a36c"), "speed decrease from stage 1 weight", -0.05, AttributeModifier.Operation.MULTIPLY_BASE);
    private static final AttributeModifier WEIGHT_SPEED_MODIFIER_2 = new AttributeModifier(UUID.fromString("9d8bf167-b018-4600-ad9e-4f66fcfdb8b2"), "speed decrease from stage 2 weight", -0.15, AttributeModifier.Operation.MULTIPLY_BASE);
    private static final AttributeModifier WEIGHT_SPEED_MODIFIER_3 = new AttributeModifier(UUID.fromString("8372c521-ee15-4ae3-af15-32ba797272d1"), "speed decrease from stage 3 weight", -0.20, AttributeModifier.Operation.MULTIPLY_BASE);
    private static final AttributeModifier WEIGHT_SPEED_MODIFIER_4 = new AttributeModifier(UUID.fromString("0f7a44e8-340b-4285-b25b-25493524eff2"), "speed decrease from stage 4 weight", -0.30, AttributeModifier.Operation.MULTIPLY_BASE);

    public static AttributeModifier[] WEIGHT_SPEED_MODIFIERS= {WEIGHT_SPEED_MODIFIER_1,WEIGHT_SPEED_MODIFIER_2,WEIGHT_SPEED_MODIFIER_3,WEIGHT_SPEED_MODIFIER_4};

    private static int doorwayWedgeCheck=0;

    public int getCurrentWeight()
    {
        return this.currentWeight;
    }
    public void setCurrentWeight(int i)
    {this.currentWeight=i;}


    public void addWeight()
    {
        if(currentWeight+1<=this.curMaxWeight && queuedWeight>0)
        {
            currentWeight++;
            queuedWeight--;
        }
        else if(queuedWeight>0)
        {
            queuedWeight--;
        }
    }
    public void loseWeight()
    {
        if(currentWeight-1>this.minWeight)
        {
            currentWeight--;
        }
    }



    public int getCurMaxWeight()
    {
        return this.curMaxWeight;
    }
    public void setMaxWeight(int i)
    {
        this.curMaxWeight=i;
    }
    public void addWeightChanges(int input)
    {
        weightChanges.add(input);
    }

    public int getWeightChanges()
    {
        int change;
        if(weightChanges.isEmpty())
        {
            change=0;
        }
        else {
            change = weightChanges.remove(0);
        }
        return change;
    }
    public int getTotalWeightChanges()
    {
        int sum=0;
        for(int i=0; i<weightChanges.size(); i++)
        {
            sum+=weightChanges.get(i);
        }
        return sum;
    }



    public boolean weightUpdateStatus()
    {
        return readyToUpdateWeight;
    }
    public void setWeightUpdateStatus(boolean input)
    {
        readyToUpdateWeight=input;
    }

    public void setWeightTick(long tick)
    {
       savedTickForWeight=tick;
    }
    public long getWeightTick()
    {
        return savedTickForWeight;
    }

    public void addChangetoQueue(int weightChange)
    {
        queuedWeight+=weightChange;
    }

    public int getQueuedWeight()
    {
        return queuedWeight;
    }

    public void setQueuedWeight(int newW)
    {
        this.queuedWeight=newW;
    }


    public void copyFrom(PlayerWeightBar source)
    {
       this.currentWeight=source.getCurrentWeight();
       this.curMaxWeight=source.getCurMaxWeight();
       this.minWeight=source.getMinWeight();
       this.queuedWeight=source.queuedWeight;
       this.weightChanges=source.weightChanges;
       this.readyToUpdateWeight=source.readyToUpdateWeight;
       this.savedTickForWeight=source.savedTickForWeight;
       this.weightUpdateDelay=source.weightUpdateDelay;
       this.lastWeightStage=source.lastWeightStage;
       this.weightUpdateDelayModifier=source.weightUpdateDelayModifier;
    }

    public void saveNBTData(CompoundTag nbt)
    {
        nbt.putInt("currentweight", currentWeight);
        nbt.putInt("maxweight", curMaxWeight);
        nbt.putInt("minweight",minWeight);
        //call it stack weight because queing is atrocious to spell ever time
        nbt.putInt("stackweight", queuedWeight);
        int[] savingArray= new int[weightChanges.size()];
        for(int i=0;i<savingArray.length;i++)
        {
            savingArray[i]=this.weightChanges.get(i);
        }
        nbt.putIntArray("changestack", savingArray);

        nbt.putInt("weightstage", this.lastWeightStage);

        nbt.putDouble("weightupdatedelaymodifier", this.weightUpdateDelayModifier);
        //Probably not important because it'll just reset, max a few extra seconds for someone to change.
        //nbt.putBoolean("updateweight", this.readyToUpdateWeight);


    }
    public void loadNBTData(CompoundTag nbt)
    {
        this.currentWeight =nbt.getInt("currentweight");
        this.curMaxWeight =nbt.getInt("maxweight");
        this.minWeight=nbt.getInt("minweight");
        this.queuedWeight =nbt.getInt("stackweight");
        int[] savingArray= nbt.getIntArray("changestack");
        for(int i=0;i<savingArray.length;i++)
        {
            this.weightChanges.add(savingArray[i]);
        }

        this.lastWeightStage=nbt.getInt("weightstage");
        this.weightUpdateDelayModifier=nbt.getDouble("weightupdatedelaymodifier");
    }

    public int getWeightUpdateDelay() {
        return weightUpdateDelay;
    }

    public void setWeightUpdateDelay(int weightUpdateDelay) {
        this.weightUpdateDelay = weightUpdateDelay;
    }

    public int getWeightLossDelay() {
        return weightLossDelay;
    }

    public void setWeightLossDelay(int weightLossDelay) {
        this.weightLossDelay = weightLossDelay;
    }

    public long getSavedTickforWeightLoss() {
        return savedTickforWeightLoss;
    }

    public void setSavedTickforWeightLoss(int savedTickforWeightLoss) {
        this.savedTickforWeightLoss = savedTickforWeightLoss;
    }

    public void setLastWeightStage(int i)
    {
        this.lastWeightStage=i;
    }
    public int getLastWeightStage()
    {
        return this.lastWeightStage;
    }

    public int calculateCurrentWeightStage()
    {
        int calculatedPercentage=(int)((((double)(this.getCurrentWeight()-this.getMinWeight()))/(this.getCurMaxWeight()- this.getMinWeight()))*100);
        //TODO make it so it's max is clamped when you set the total levels of weight
        return calculatedPercentage/20;
    }


    public int getAmountThroughStage() {
        return amountThroughStage;
    }

    public void setAmountThroughStage(int amountThroughStage) {
        this.amountThroughStage = amountThroughStage;
    }

    public int getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(int minWeight) {
        this.minWeight = minWeight;
    }

    public double getWeightUpdateDelayModifier() {
        return weightUpdateDelayModifier;
    }
    public void setWeightUpdateDelayModifier(double weightUpdateDelayModifier) {
        this.weightUpdateDelayModifier = weightUpdateDelayModifier;
    }

    public int incrementWedgeCheck()
    {
        ++doorwayWedgeCheck;
        if(doorwayWedgeCheck>=4)
        {
            doorwayWedgeCheck=0;
            return 4;
        }
        return doorwayWedgeCheck;
    }

    public static void clearModifiers(Player player)
    {
        for(int i=0;i<PlayerWeightBar.WEIGHT_HEALTH_MODIFIERS.length;i++)
        {
            if(player.getAttribute(Attributes.MAX_HEALTH).hasModifier(PlayerWeightBar.WEIGHT_HEALTH_MODIFIERS[i]))
            {
                player.getAttribute(Attributes.MAX_HEALTH).removeModifier(PlayerWeightBar.WEIGHT_HEALTH_MODIFIERS[i]);
            }
            if(player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(PlayerWeightBar.WEIGHT_SPEED_MODIFIERS[i]))
            {
                player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(PlayerWeightBar.WEIGHT_SPEED_MODIFIERS[i]);
            }
        }
    }

    public static void addCorrectModifier(ServerPlayer player)
    {
        player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {

            PlayerWeightBar.clearModifiers(player);
        int newWeightStage=weightBar.calculateCurrentWeightStage();
        ModMessages.sendToPlayer(new WeightMaxMinPollS2C(),player);

        //This clears the weight modifiers and sets it correctly when you join

        if(newWeightStage!=0)
        {
            if(newWeightStage==5)
            {
                player.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(PlayerWeightBar.WEIGHT_HEALTH_MODIFIERS[3]);
                player.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(PlayerWeightBar.WEIGHT_SPEED_MODIFIERS[3]);

            }
            else
            {
                player.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(PlayerWeightBar.WEIGHT_HEALTH_MODIFIERS[newWeightStage-1]);
                player.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(PlayerWeightBar.WEIGHT_SPEED_MODIFIERS[newWeightStage-1]);

            }
        }
            if(player.getHealth()>player.getMaxHealth())
            {
                player.setHealth(player.getMaxHealth());
            }
        });
    }

}
