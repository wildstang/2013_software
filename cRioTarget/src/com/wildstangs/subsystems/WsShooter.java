package com.wildstangs.subsystems;

import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.subsystems.base.WsSubsystem;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;

public class WsShooter extends WsSubsystem {

    
    private Encoder encoder1, encoder2;
    private double wheel1Speed = 0;
    private double wheel2Speed = 0;
    public WsShooter (String name) 
    {
        super(name);
        encoder1 = new Encoder(1, 1, 2, 2, false);
        encoder2 = new Encoder(3, 3, 4, 4, false);
    }

    public void init()
    {
        
    }
    
    public void update() 
    {
        double speed1 = encoder1.getRate();
        double speed2 = encoder2.getRate();
        
        Victor victor1 = (Victor) WsOutputFacade.getInstance().getOutput(WsOutputFacade.SHOOTER_VICTOR_1);
        Victor victor2 = (Victor) WsOutputFacade.getInstance().getOutput(WsOutputFacade.SHOOTER_VICTOR_2);
        
        if(speed1 < wheel1Speed)
        {
            victor1.set(1.0f); 
        }
        else
        {
            victor1.set(0.0f);
        }
        
        if(speed2 < wheel2Speed)
        {
            victor2.set(1.0f);
        }
        else
        {
            victor2.set(0.0f);
        }
    }

    public void notifyConfigChange() 
    {
        
    }
}