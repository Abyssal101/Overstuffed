package net.willsbr.overstuffed.ServerPlayerSettings;

import net.minecraft.nbt.CompoundTag;

public class PlayerServerSettings {

    private boolean weightEffects=true;
    private boolean stageBased=false;

    private int burpFrequency=0;
    private int gurgleFrequency=0;
    public void copyFrom(PlayerServerSettings source)
    {
        this.weightEffects =source.weightEffects;
        this.stageBased=source.stageBased;
        this.burpFrequency=source.burpFrequency;
        this.gurgleFrequency=source.gurgleFrequency;
    }

    public void saveNBTData(CompoundTag nbt)
    {
        nbt.putBoolean("weighteffects", weightEffects);
        nbt.putBoolean("stagebased", stageBased);
        nbt.putInt("burpFrequency", burpFrequency);
        nbt.putInt("gurgleFrequency", gurgleFrequency);


    }
    public void loadNBTData(CompoundTag nbt)
    {
        weightEffects = nbt.getBoolean("weighteffects");
        stageBased = nbt.getBoolean("stagebased");
        burpFrequency = nbt.getInt("burpFrequency");
        gurgleFrequency = nbt.getInt("gurgleFrequency");
    }

    public CompoundTag updateNBTData()
    {
        CompoundTag nbt= new CompoundTag();

        nbt.putBoolean("weighteffects", weightEffects);
        nbt.putBoolean("stagebased", stageBased);
        nbt.putInt("burpFrequency", burpFrequency);
        nbt.putInt("gurgleFrequency", gurgleFrequency);

        return nbt;
    }

    public void setWeightEffects(boolean input)
    {
        this.weightEffects = input;
    }
    public boolean weightEffects()
    {
        return weightEffects;
    }
    public void setStageGain(boolean input)
    {
        this.stageBased = input;
    }
    public boolean stageBasedGain()
    {
        return stageBased;
    }


    public int getBurpFrequency() {
        return burpFrequency;
    }

    public void setBurpFrequency(int burpFrequency) {
        this.burpFrequency = burpFrequency;
    }

    public int getGurgleFrequency() {
        return gurgleFrequency;
    }

    public void setGurgleFrequency(int gurgleFrequency) {
        this.gurgleFrequency = gurgleFrequency;
    }
}
