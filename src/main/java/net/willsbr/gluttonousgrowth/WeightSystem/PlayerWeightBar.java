package net.willsbr.gluttonousgrowth.WeightSystem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;
import net.willsbr.gluttonousgrowth.ServerPlayerSettings.PlayerServerSettingsProvider;
import net.willsbr.gluttonousgrowth.config.GluttonousClientConfig;
import net.willsbr.gluttonousgrowth.config.GluttonousWorldConfig;
import net.willsbr.gluttonousgrowth.networking.ModMessages;
import net.willsbr.gluttonousgrowth.networking.packet.WeightPackets.WeightMaxMinPollS2C;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerWeightBar {
    //

    private int minWeight= 100;
    private int currentWeight;

    //this is what options will determine for display
    private int curMaxWeight= 300;

    //this is what should be used to set curMaxWeight, infinite options is hard to balance
    //food queue situation so you slowly gain weight
    private int queuedWeight;

    private ArrayList<Integer> weightChanges= new ArrayList<>();

    private boolean readyToUpdateWeight=true;
    private long savedTickForWeight;

    private int weightUpdateDelay=40;
    private double weightUpdateDelayModifier=1;


    private int weightLossDelay;
    private long savedTickforWeightLoss = -1; // -1 means timer not running

    //this is the boolean controlling if it's gradual or if it rapidly increases in stages
    private int lastWeightStage;

    private int amountThroughStage;
    private boolean effectsReady=false;

    private AttributeModifier WEIGHT_HEALTH_MODIFIER =
            new AttributeModifier(UUID.fromString("65d64bf1-2703-458d-a799-3d06b1e3a36c"), "health increase per percentage", 0, AttributeModifier.Operation.ADDITION);
    private AttributeModifier WEIGHT_SPEED_MODIFIER =
            new AttributeModifier(UUID.fromString("65d64bf1-2704-458d-a799-3d06b1e3a36c"), "speed decrease per percentage", 0, AttributeModifier.Operation.MULTIPLY_BASE);
    private AttributeModifier SCALING_HEALTH_MODIFIER =
            new AttributeModifier(UUID.fromString("65d64bf1-2703-458d-a799-3d06b1e3a26c"), "health increase from hitbox increase", 0, AttributeModifier.Operation.ADDITION);
    private float currentHitboxIncrease=0;
    private float lastHitboxIncrease=0;
    private float lastBaseScale=0;



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
       // savedTickForWeight is an absolute tickCount reference.
       // After death the new entity's tickCount starts fresh, so carrying the old value
       // would make (tickCount - savedTickForWeight) negative and freeze weight updates.
       // Reset to 0 so the delay check fires immediately on the next tick.
       this.savedTickForWeight = 0;
       // Similarly, reset weight loss timer so it doesn't fire immediately or freeze.
       this.savedTickforWeightLoss = -1;
       this.weightUpdateDelay=source.weightUpdateDelay;
       this.lastWeightStage=source.lastWeightStage;
       this.weightUpdateDelayModifier=source.weightUpdateDelayModifier;
       this.totalStages=source.totalStages;
       this.currentHitboxIncrease=source.getCurrentHitboxIncrease();
       this.lastHitboxIncrease=source.getLastHitboxIncrease();
       // Copy effectsReady so stage-based effects apply correctly after death.
       this.effectsReady=source.effectsReady;
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
        nbt.putInt("totalweightstages",this.totalStages);

        // Save the current and last hitbox increase values
        nbt.putFloat("currenthitboxincrease", this.currentHitboxIncrease);
        nbt.putFloat("lasthitboxincrease", this.lastHitboxIncrease);
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
        this.totalStages=nbt.getInt("totalweightstages");

        // Load the current and last hitbox increase values if they exist
        if (nbt.contains("currenthitboxincrease")) {
            this.currentHitboxIncrease = nbt.getFloat("currenthitboxincrease");
        }
        if (nbt.contains("lasthitboxincrease")) {
            this.lastHitboxIncrease = nbt.getFloat("lasthitboxincrease");
        }
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
    public double calculateCurrentWeightPercentage()
    {
        return (((double)(this.getCurrentWeight()-this.getMinWeight()))/(this.getCurMaxWeight()- this.getMinWeight()));
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
        double percentageEffect= (double)(this.currentWeight-this.getMinWeight())/(this.getCurMaxWeight()-this.getMinWeight());
       setNewModifierLogic(percentageEffect);
    }
    //assumes that the currentWeight includes the minWeight
    public void setNewModifiers(int currentWeight)
    {

        double percentageEffect= (double)(currentWeight-this.getMinWeight())/(this.getCurMaxWeight()-this.getMinWeight());
        setNewModifierLogic(percentageEffect);
    }

    private void setNewModifierLogic(double percentageEffect)
    {
        this.WEIGHT_HEALTH_MODIFIER=new AttributeModifier(UUID.fromString("65d64bf1-2703-458d-a799-3d06b1e3a36c"),
                "health increase from OS",
                (int)(percentageEffect* GluttonousWorldConfig.maxHearts.get()), AttributeModifier.Operation.ADDITION);

        this.WEIGHT_SPEED_MODIFIER=new AttributeModifier(UUID.fromString("65d64bf1-2704-458d-a799-3d06b1e3a36c"),
                "speed increase from OS",
                -(percentageEffect* GluttonousWorldConfig.maxSpeedDecrease.get()), AttributeModifier.Operation.MULTIPLY_BASE);

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
            if(player.getAttribute(Attributes.MAX_HEALTH).hasModifier(weightBar.SCALING_HEALTH_MODIFIER))
            {
                player.getAttribute(Attributes.MAX_HEALTH).removeModifier(weightBar.SCALING_HEALTH_MODIFIER);
            }
    }

    public static void addCorrectModifier(ServerPlayer player)
    {

        player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {

        PlayerWeightBar.clearModifiers(player, weightBar);
        ModMessages.sendToPlayer(new WeightMaxMinPollS2C(),player);

        //This clears the weight modifiers and sets it correctly when you join
            player.getAttribute(Attributes.MAX_HEALTH).addTransientModifier(weightBar.WEIGHT_HEALTH_MODIFIER);
            player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(weightBar.WEIGHT_SPEED_MODIFIER);

        });
    }

    public static void clearScaling(Player player,PlayerWeightBar weightBar)
    {
            if (ModList.get().isLoaded("pehkui")) {
                if(weightBar.getCurrentHitboxIncrease()!=0)
                {
                    ScaleData hitboxWidthData = ScaleTypes.HITBOX_WIDTH.getScaleData(player);
                    //removes anything added by Overstuffed
                    hitboxWidthData.setScale(hitboxWidthData.getScale() - weightBar.getCurrentHitboxIncrease());
                    weightBar.setCurrentHitboxIncrease(0);
                    weightBar.setLastHitboxIncrease(0);
                }
            }
    }

    public static void addCorrectScaling(ServerPlayer player)
    {
        player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar ->
        {
            if(ModList.get().isLoaded("pehkui"))
            {
                player.getCapability(PlayerServerSettingsProvider.PLAYER_SERVER_SETTINGS).ifPresent(serverSettings -> {
                    //recalculates what your supposed to have
                    int curWeight;
                    //Could I make a copy of this method and pass the this calculation, yes but it would
                    //make my code so much more ugly

                    if(serverSettings.stageBasedGain())
                    {
                        curWeight=(int)((double)weightBar.calculateCurrentWeightStage()
                                /weightBar.getTotalStages()*(weightBar.getCurMaxWeight()-weightBar.getMinWeight()));
                    }
                    else
                    {
                        curWeight = weightBar.getCurrentWeight()-weightBar.getMinWeight();
                    }

                    float percentage=(float)(curWeight)/(weightBar.getCurMaxWeight()-weightBar.getMinWeight());
                    percentage=(float)(percentage*serverSettings.getMaxHitboxWidth());

                    // Only apply changes if the new scale is different from the last scale
                    if (percentage != weightBar.getLastHitboxIncrease())
                    {

                        ScaleData hitboxWidthData = ScaleTypes.HITBOX_WIDTH.getScaleData(player);
                        //removes anything added by Overstuffed
                        //this can be above 1 by default, so if "remove" my hitbox increase but the player has a higher
                        //default scale, than it'll still multiply for example, if they are at 1.5 scale
                        //and it tries to reset it, it'll set to "1.5" and that'll multiply with the current 1.5

                        float baseScale=ScaleTypes.BASE.getScaleData(player).getBaseScale();



                        hitboxWidthData.setScale(hitboxWidthData.getScale()/baseScale-weightBar.getCurrentHitboxIncrease());

                        // Update the current and last hitbox increase values
                        weightBar.setLastHitboxIncrease(percentage);
                        weightBar.setCurrentHitboxIncrease(percentage);
                        weightBar.setLastBaseScale(baseScale);

                        // Apply the new scale
                        hitboxWidthData.setScale(hitboxWidthData.getScale()/baseScale+(float)weightBar.getCurrentHitboxIncrease());

                        weightBar.SCALING_HEALTH_MODIFIER=new AttributeModifier(UUID.fromString("65d64bf1-2703-458d-a799-3d06b1e3a26c"),
                                "hitbox health increase from OS",
                                (int)(weightBar.currentHitboxIncrease/GluttonousWorldConfig.blocksPerHeart.get()), AttributeModifier.Operation.ADDITION);
                        // Update health modifier
                        player.getAttribute(Attributes.MAX_HEALTH).addTransientModifier(weightBar.SCALING_HEALTH_MODIFIER);
                    }
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

    public float getLastHitboxIncrease() {
        return lastHitboxIncrease;
    }

    public void setLastHitboxIncrease(float lastHitboxIncrease) {
        this.lastHitboxIncrease = lastHitboxIncrease;
    }
    public int getWeightHealth()
    {
        return (int)WEIGHT_HEALTH_MODIFIER.getAmount();
    }
    public double getWeightSpeed()
    {
        return WEIGHT_SPEED_MODIFIER.getAmount();
    }
    public int getScalingHealth()
    {
        return (int)SCALING_HEALTH_MODIFIER.getAmount();
    }


    public boolean isEffectsReady() {
        return effectsReady;
    }

    public void setEffectsReady(boolean effectsReady) {
        this.effectsReady = effectsReady;
    }

    public float getLastBaseScale() {
        return lastBaseScale;
    }

    public void setLastBaseScale(float lastBaseScale) {
        this.lastBaseScale = lastBaseScale;
    }
}
