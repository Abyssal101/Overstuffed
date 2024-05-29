package net.willsbr.overstuffed.client;

public class ClientTogglesData {

    static int[] settings= new int[10];


    static public void setToggle(int index, boolean input)
    {
        settings[index]=booleanToInt(input);
    }

    static public void setValueToggle(int index, int input)
    {
        settings[index]=input;
    }
    static public int getValueToggle(int index)
    {
        return settings[index];
    }
    static public boolean getToggle(int index)
    {
        return intToBoolean(settings[index]);
    }

    static public boolean intToBoolean(int inputInt)
    {
        if(inputInt==1)
        {
            return true;
        }
        else if(inputInt==0)
        {
            return false;
        }
        else {
            System.out.println("You fuckkkeeed the toggle somehow");
            return false;
        }
    }
    static public int booleanToInt(boolean input)
    {
        if(input)
        {
            return 1;
        }
        return 0;

    }


}
