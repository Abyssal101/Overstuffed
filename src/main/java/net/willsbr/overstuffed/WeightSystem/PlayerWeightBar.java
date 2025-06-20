package net.willsbr.overstuffed.WeightSystem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;
import net.willsbr.overstuffed.Command.ActiveCommands.setHitbox;
import net.willsbr.overstuffed.ServerPlayerSettings.PlayerServerSettingsProvider;
import net.willsbr.overstuffed.config.OverstuffedClientConfig;
import net.willsbr.overstuffed.config.OverstuffedWorldConfig;
import net.willsbr.overstuffed.networking.ModMessages;
import net.willsbr.overstuffed.networking.packet.WeightPackets.WeightMaxMinPollS2C;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerWeightBar {
    //

    private int minWeight= OverstuffedClientConfig.minWeight.get();
    private int currentWeight;

    //this is what options will determine for display
    private int curMaxWeight= OverstuffedClientConfig.getMaxWeight();

    //this is what should be used to set curMaxWeight, infinite options is hard to balance
    //food queue situation so you slowly gain weight
    private int queuedWeight;

    private ArrayList<Integer> weightChanges= new ArrayList<>();

    private boolean readyToUpdateWeight=true;
    private long savedTickForWeight;

    private int weightUpdateDelay=40;
    private double weightUpdateDelayModifier=1;


    private int weightLossDelay;
    private long savedTickforWeightLoss;

    //this is the boolean controlling if it's gradual or if it rapidly increases in stages
    private int lastWeightStage;

    private int amountThroughStage;
    private AttributeModifier WEIGHT_HEALTH_MODIFIER =
            new AttributeModifier(UUID.fromString("65d64bf1-2703-458d-a799-3d06b1e3a36c"), "health increase per percentage", 0, AttributeModifier.Operation.MULTIPLY_BASE);
    private AttributeModifier WEIGHT_SPEED_MODIFIER =
            new AttributeModifier(UUID.fromString("65d64bf1-2704-458d-a799-3d06b1e3a36c"), "speed decrease per percentage", 0, AttributeModifier.Operation.MULTIPLY_BASE);

    private float currentHitboxIncrease=0;



    //TODO make this save to NBT when your done
    private int totalStages = 5;


    public int getTotalStages() {
        return totalStages;
    }

    public void setTotalStages(int totalStages) {
        this.totalStages = totalStages;
    }


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
        for (Integer weightChange : weightChanges) {
            sum += weightChange;
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
        for (int j : savingArray) {
            this.weightChanges.add(j);
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
        return calculatedPercentage/(100/this.getTotalStages());
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


    public void setNewModifiers()
    {
        double percentageEffect= (double)this.currentWeight/(this.getCurMaxWeight()-this.getMinWeight());
        this.WEIGHT_HEALTH_MODIFIER=new AttributeModifier(UUID.fromString("65d64bf1-2703-458d-a799-3d06b1e3a36c"),
                "health increase from OS",
                (int)(percentageEffect*OverstuffedWorldConfig.maxHearts.get()), AttributeModifier.Operation.ADDITION);

        this.WEIGHT_SPEED_MODIFIER=new AttributeModifier(UUID.fromString("65d64bf1-2704-458d-a799-3d06b1e3a36c"),
                "speed increase from OS",
                -(percentageEffect*OverstuffedWorldConfig.maxSpeedDecrease.get()), AttributeModifier.Operation.MULTIPLY_BASE);
    }
    public void setNewModifiers(int currentStage)
    {
        double percentageEffect= (double)currentStage/this.getTotalStages();
        this.WEIGHT_HEALTH_MODIFIER=new AttributeModifier(UUID.fromString("65d64bf1-2703-458d-a799-3d06b1e3a36c"),
                "health increase from OS",
                (int)(percentageEffect*OverstuffedWorldConfig.maxHearts.get()), AttributeModifier.Operation.ADDITION);

        this.WEIGHT_SPEED_MODIFIER=new AttributeModifier(UUID.fromString("65d64bf1-2704-458d-a799-3d06b1e3a36c"),
                "speed increase from OS",
                -(percentageEffect*OverstuffedWorldConfig.maxSpeedDecrease.get()), AttributeModifier.Operation.MULTIPLY_BASE);
    }


    public static void clearModifiers(Player player, PlayerWeightBar weightBar)
    {
            if(player.getAttribute(Attributes.MAX_HEALTH).hasModifier(weightBar.WEIGHT_HEALTH_MODIFIER))
            {
                player.getAttribute(Attributes.MAX_HEALTH).removeModifier(weightBar.WEIGHT_HEALTH_MODIFIER);
            }
            if(player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(weightBar.WEIGHT_SPEED_MODIFIER))
            {
                player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(weightBar.WEIGHT_SPEED_MODIFIER);
            }
    }


    public static void addCorrectModifier(ServerPlayer player)
    {
        player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {

        PlayerWeightBar.clearModifiers(player, weightBar);
        ModMessages.sendToPlayer(new WeightMaxMinPollS2C(),player);
        int newWeightStage=weightBar.calculateCurrentWeightStage();

            //This clears the weight modifiers and sets it correctly when you join
        if(newWeightStage!=0)
        {
            //TODO add setting if it's granualar versus staged
            weightBar.setNewModifiers(newWeightStage);
            player.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(weightBar.WEIGHT_HEALTH_MODIFIER);
            player.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(weightBar.WEIGHT_SPEED_MODIFIER);
        }
        if(player.getHealth()>player.getMaxHealth())
        {
            player.setHealth(player.getMaxHealth());
        }
        });
    }
    public static void addCorrectScaling(ServerPlayer player)
    {
        player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar ->
        {
            if(ModList.get().isLoaded("pehkui"))
            {
                ScaleData hitboxWidthData = ScaleTypes.HITBOX_WIDTH.getScaleData(player);
                //removes anything added by Overstuffed
                hitboxWidthData.setScale(hitboxWidthData.getScale()- weightBar.getCurrentHitboxIncrease());
                player.getCapability(PlayerServerSettingsProvider.PLAYER_SERVER_SETTINGS).ifPresent(serverSettings -> {
                    //recalculates what your supposed to have
                    float percentage=(float)weightBar.getCurrentWeight()/(weightBar.getCurMaxWeight()-weightBar.getMinWeight());
                    percentage=(float)(percentage*serverSettings.getMaxHitboxWidth());
                    weightBar.setCurrentHitboxIncrease(percentage);
                    hitboxWidthData.setScale(hitboxWidthData.getScale()+(float)weightBar.getCurrentHitboxIncrease());
                });

            }
        });
    }


    public float getCurrentHitboxIncrease() {
        return currentHitboxIncrease;
    }

    public void setCurrentHitboxIncrease(float currentHitboxIncrease) {
        this.currentHitboxIncrease = currentHitboxIncrease;
    }
}