package net.willsbr.gluttonousgrowth.StuffedBar;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.willsbr.gluttonousgrowth.ServerPlayerSettings.PlayerServerSettingsProvider;
import net.willsbr.gluttonousgrowth.WeightSystem.PlayerWeightBar;
import net.willsbr.gluttonousgrowth.WeightSystem.PlayerWeightBarProvider;
import net.willsbr.gluttonousgrowth.config.GluttonousWorldConfig;
import net.willsbr.gluttonousgrowth.networking.ModMessages;
import net.willsbr.gluttonousgrowth.networking.packet.StuffedPackets.CalorieMeterDelaySyncPacketS2C;
import net.willsbr.gluttonousgrowth.networking.packet.StuffedPackets.OverfullFoodDataSyncPacketS2C;
import net.willsbr.gluttonousgrowth.sound.ModSounds;

import java.util.concurrent.atomic.AtomicBoolean;

import static net.willsbr.gluttonousgrowth.config.GluttonousClientConfig.stageGain;

public class PlayerCalorieMeter {

    private int maxCalories= GluttonousWorldConfig.baseCalCap.get();
    private int currentCalories=0;

    private double calorieGainMultipler= GluttonousWorldConfig.calorieGainMultipler.get();

    private double modMetabolismThres= GluttonousWorldConfig.modMetabolismThres.get();
    private double slowMetabolismThres= GluttonousWorldConfig.slowMetabolismThres.get();


    private int calLost =0;
    private int addState=0;

    //what the amount of stuffed lost should be before it adds new
    private int interval= GluttonousWorldConfig.capacityIncreaseInterval.get();

    private int calClearDelay=-1;
    private long foodEatenTick =-1;

    // Temporary field used only during load: stores remaining ticks until calorie clear.
    // Set by loadNBTData, consumed by rearmFoodEatenTick(currentServerTick), then reset to -1.
    private int pendingRemainingTicks = -1;


    public void copyFrom(PlayerCalorieMeter source)
    {
        this.maxCalories=source.getMaxCalories();
        this.currentCalories=source.getCurrentCalories();
        this.modMetabolismThres=source.getModMetabolismThres();
        this.slowMetabolismThres=source.getSlowMetabolismThres();
        this.calLost =source.getCalLost();
        this.addState=source.addState;
        this.interval=source.interval;
        this.calClearDelay=source.calClearDelay;
        // foodEatenTick is an absolute tick reference and cannot be safely copied across death.
        // Set pendingRemainingTicks so rearmFoodEatenTick() in onPlayerJoinWorld re-anchors it.
        this.foodEatenTick = -1;
        this.pendingRemainingTicks = (source.currentCalories > 0) ? source.calClearDelay : -1;
    }

    public void saveNBTData(CompoundTag nbt)
    {
        nbt.putInt("curcalories", this.currentCalories);
        nbt.putInt("maxcalories", this.maxCalories);
        nbt.putDouble("modmetabolismthreshold", this.modMetabolismThres);
        nbt.putDouble("slowmetabolismthreshold", this.slowMetabolismThres);
        nbt.putInt("callost", this.calLost);
        nbt.putInt("addstate",this.addState);
        nbt.putInt("calcleardelay",this.calClearDelay);

        // Store remaining ticks instead of absolute tick so it survives restarts/dimension changes.
        // If foodEatenTick is -1 (timer not running), store -1.
        if (this.foodEatenTick == -1 || this.calClearDelay == -1) {
            nbt.putInt("remainingcalclearticks", -1);
        } else {
            // We don't have access to the current server tick here, so store the full calClearDelay
            // as a safe upper bound. rearmFoodEatenTick() will use this to re-anchor the timer.
            // This means after a save/load the timer resets to full delay, which is acceptable.
            nbt.putInt("remainingcalclearticks", this.calClearDelay);
        }
    }
    public void loadNBTData(CompoundTag nbt)
    {
        currentCalories=nbt.getInt("curcalories");
        maxCalories=nbt.getInt("maxcalories");
        modMetabolismThres= GluttonousWorldConfig.modMetabolismThres.get();
        slowMetabolismThres= GluttonousWorldConfig.slowMetabolismThres.get();
        calLost =nbt.getInt("callost");
        addState=nbt.getInt("addstate");
        calClearDelay=nbt.getInt("calcleardelay");

        // Do NOT set foodEatenTick here — we don't know the current server tick.
        // rearmFoodEatenTick() must be called from onPlayerJoinWorld with the current tick.
        foodEatenTick = -1;
        if (nbt.contains("remainingcalclearticks")) {
            // New format: remaining ticks stored directly
            pendingRemainingTicks = nbt.getInt("remainingcalclearticks");
        } else if (nbt.contains("lastfoodeatentick")) {
            // Legacy format migration: we can't recover the exact remaining ticks without knowing
            // the server tick at save time, so conservatively restore the full delay.
            // This means the timer resets to full on first load after upgrading, which is safe.
            pendingRemainingTicks = (currentCalories > 0 && calClearDelay > 0) ? calClearDelay : -1;
        } else {
            pendingRemainingTicks = -1;
        }
    }

    /**
     * Called from onPlayerJoinWorld (where server tick is available) to re-anchor
     * foodEatenTick to the current server tick, preserving the remaining delay.
     */
    public void rearmFoodEatenTick(long currentServerTick) {
        if (currentCalories <= 0 || calClearDelay <= 0) {
            // No calories or no delay configured — timer should not run.
            foodEatenTick = -1;
        } else if (pendingRemainingTicks > 0) {
            // Restore with remaining ticks preserved.
            // foodEatenTick is set so that checkClearCalories fires after pendingRemainingTicks more ticks.
            foodEatenTick = currentServerTick - (calClearDelay - pendingRemainingTicks);
        } else {
            // pendingRemainingTicks is 0 or -1 but calories exist — restart timer from full delay.
            // This covers dimension changes and fresh loads where remaining ticks couldn't be saved.
            foodEatenTick = currentServerTick;
        }
        pendingRemainingTicks = -1;
    }


    public int getCalLost() {
        return calLost;
    }
    public void setCalLost(int stuffedLost) {
        this.calLost = stuffedLost;
    }


    public void addCalLost(int calories)
    {
        this.calLost+=calories;
    }

    public boolean checkClearCalories(long tick)
    {
        return foodEatenTick != -1 && tick - foodEatenTick > calClearDelay;
    }

    /**
     * Returns ticks remaining until the next calorie clear, given the current server tick.
     * Returns -1 if the timer is not running.
     */
    public int getRemainingTicks(long currentServerTick) {
        if (foodEatenTick == -1 || calClearDelay == -1) return -1;
        return (int) Math.max(0, calClearDelay - (currentServerTick - foodEatenTick));
    }





    public int getInterval() {
        return interval;
    }
    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getAddState()
    {
        return addState;
    }


    public int getMaxCalories()
    {
        return maxCalories;
    }

    public void setMaxCalories(int maxCalories)
    {
        this.maxCalories = maxCalories;
    }

    public int getCurrentCalories()
    {
        return currentCalories;
    }

    public void setCurrentCalories(int currentCalories) {
        this.currentCalories = Math.max(0,currentCalories);
    }
    public void addCalories(int calories)
    {
        this.currentCalories+=calories;
        if(this.currentCalories>this.maxCalories)
        {
            this.currentCalories=this.maxCalories;
        }
    }

    static public int calRawCalories(int nutrition, double saturationModifier)
    {
        int calculatedCalories=nutrition;
        calculatedCalories=calculatedCalories+(int)(calculatedCalories*saturationModifier);
        return calculatedCalories;
    }

    public int calcPlayerCalories(int nutrition, double saturationModifier, PlayerWeightBar weightBar
    ,AtomicBoolean curStageGain)
    {

        int calculatedCalories=(int)(calRawCalories(nutrition,saturationModifier)*calorieGainMultipler);
        double calReductionFromWeight=0;

        if(curStageGain.get())
        {
            double currentStagePercentage=(double)weightBar.calculateCurrentWeightStage()/weightBar.getTotalStages();
            calReductionFromWeight=(1-currentStagePercentage*0.5);
        }
        else
        {
            calReductionFromWeight=(1-weightBar.calculateCurrentWeightPercentage()*0.5);
        }


        calculatedCalories=(int)(calculatedCalories*calReductionFromWeight);
        calculatedCalories=Math.max(1,calculatedCalories);

        return calculatedCalories;
    }

    private void handleCalorieTiming(int calculatedCalories,Player player)
    {
        if(this.getFoodEatenTick()==-1)
        {
            this.setFoodEatenTick(player.tickCount);
            this.setCalClearDelay(GluttonousWorldConfig.minCalClearDelay.get());
        }
        else
        {
            int timeToAdd=(int)(((double)calculatedCalories/this.getMaxCalories()
                    *(GluttonousWorldConfig.maxCalClearDelay.get()- GluttonousWorldConfig.minCalClearDelay.get())));
            timeToAdd+=GluttonousWorldConfig.minCalClearDelay.get();
            this.setCalClearDelay(this.getCalClearDelay()+timeToAdd);
        }
    }

    private void handleEatingSounds(ServerPlayer player)
    {
        ModSounds.playBurp(player);
        ModMessages.sendToPlayer(new OverfullFoodDataSyncPacketS2C(this.getCurrentCalories(),
                this.getMaxCalories(),this.getModMetabolismThres(),
                this.getSlowMetabolismThres()),player);
        ModMessages.sendToPlayer(new CalorieMeterDelaySyncPacketS2C(this.getCalClearDelay(), this.getRemainingTicks(player.tickCount)),player);
    }

    //(ON SERVER ONLY)Handles calculating the calories, adding them to the player server side, syncing with client, and activating souds
    public void handleCalorieAddition(int nutrition, double saturationModifier,
                                      ServerPlayer player)
    {
        player.getCapability(PlayerWeightBarProvider.PLAYER_WEIGHT_BAR).ifPresent(weightBar -> {
            AtomicBoolean curStageGain=new AtomicBoolean(false);

            //Grabs the if the stage setting is set
            player.getCapability(PlayerServerSettingsProvider.PLAYER_SERVER_SETTINGS).ifPresent(serverSettings -> {
                curStageGain.set(serverSettings.stageBasedGain());
            });

            int calculatedCalories=this.calcPlayerCalories(nutrition,saturationModifier,weightBar,curStageGain);
            this.addCalories(calculatedCalories);
            //only does it on the first time it's been cosnumed
            this.handleCalorieTiming(calculatedCalories,player);
            this.handleEatingSounds(player);
        });

    }




    public double getModMetabolismThres() {
        return modMetabolismThres;
    }

    public void setModMetabolismThres(double modMetabolismThres) {
        this.modMetabolismThres = modMetabolismThres;
    }

    public double getSlowMetabolismThres() {
        return slowMetabolismThres;
    }

    public void setSlowMetabolismThres(double slowMetabolismThres) {
        this.slowMetabolismThres = slowMetabolismThres;
    }

    public int getCalClearDelay() {
        return calClearDelay;
    }

    public void setCalClearDelay(int calClearDelay) {
        this.calClearDelay = calClearDelay;
    }

    public long getFoodEatenTick() {
        return foodEatenTick;
    }

    public void setFoodEatenTick(long foodEatenTick)
    {
        this.foodEatenTick = foodEatenTick;
    }

    public double getCalorieGainMultipler() {
        return calorieGainMultipler;
    }

    public void setCalorieGainMultipler(double calorieGainMultipler) {
        this.calorieGainMultipler = calorieGainMultipler;
    }



}
