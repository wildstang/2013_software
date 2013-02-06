package com.wildstangs.config.timer;

import com.wildstangs.logger.Logger;
import edu.wpi.first.wpilibj.Timer;

public class WsTimer 
{
    double startingTime;
    double endingTime;
    double totalTime;
    double minTime;
    double maxTime;
    int runs;
    int iterations;
    
    String name;
    Timer time;
    
    public WsTimer(String name, int iterations)
    {
        reset();
        this.iterations = iterations;
        this.name = name;
        time = new Timer();
    }
    
    public void startTimingSection()
    {
        runs++;
        time.start();
        startingTime = time.getFPGATimestamp();
    }
    public double endTimingSection()
    {
        double spentTime = 0;
        time.stop();
        endingTime = time.getFPGATimestamp();
        spentTime = endingTime - startingTime;
        
        totalTime += spentTime;
        
        if(spentTime > maxTime)
        {
            maxTime = spentTime;
        }
        
        if(spentTime < minTime || (minTime == 0))
        {
            minTime = spentTime;
        }
                    
        Logger.getLogger().debug(name, "Cycle Time", Double.toString(spentTime));
        if(runs >= iterations)
        {
            Logger.getLogger().debug(name, "Avg Time", Double.toString(totalTime / (double)runs));
            Logger.getLogger().debug(name, "Max Time", Double.toString(maxTime));
            Logger.getLogger().debug(name, "Min Time", Double.toString(minTime));
            reset();
        }
       return spentTime;
    }
    
    private void reset()
    {
        startingTime = 0;
        endingTime = 0;
        totalTime = 0;
        minTime = 0;
        maxTime = 0;
        runs = 0;
    }
}