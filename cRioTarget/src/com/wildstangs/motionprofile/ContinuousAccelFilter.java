/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wildstangs.motionprofile;


public class ContinuousAccelFilter {
    protected double currPos; 
    protected double currVel; 
    protected double currAcc; 
    //Usage: 
    //m_straightFilter = new ContinuousAccelFilter();
    //m_straightFilter->CalcSystem(m_robot->straightDistanceGoal - m_currentX, m_currentV, m_robot->straightDistanceGoalVelocity, m_robot->straightDistanceMaxAcceleration, m_robot->straightDistanceMaxVelocity, m_robot->dt);
    //	m_currentA=m_straightFilter->GetCurrAcc();
    // 	m_currentV=m_straightFilter->GetCurrVel();
    // 	m_currentX=m_straightFilter->GetCurrPos();

    public double getCurrPos() {
        return currPos;
    }

    public double getCurrVel() {
        return currVel;
    }

    public double getCurrAcc() {
        return currAcc;
    }
    
    
    public ContinuousAccelFilter(double currPos , double currVel , double currAcc) {
        this.currPos = currPos; 
        this.currVel = currVel; 
        this.currAcc = currAcc; 
    }
    
    public void updateValues(double acc, double dt) {
        currPos+=currVel*dt+acc*.5*dt*dt;
        currVel+=acc*dt;
        currAcc=acc;
    }

    private double dt2,a,const_time,dtf,af;
    public void calculateSystem(double distance_to_target, double v, double goal_v, double max_a, double max_v, double dt)
    {
        //Reset values before recalculating
        dt2 = 0 ; 
        a = 0;
        const_time = 0 ;
        dtf= 0 ;
        af= 0 ;
        maxAccelTime(distance_to_target,v,goal_v,max_a,max_v);
        double time_left=dt;
        if(dt2>time_left)
            updateValues(a,time_left);
        else {
            updateValues(a,dt2);
            time_left-=dt2;
            if(const_time>time_left)
                updateValues(0,time_left);
            else {
                updateValues(0,const_time);
                time_left-=const_time;
                if(dtf>time_left)
                    updateValues(af,time_left);
                else {
                    updateValues(af,dtf);
                    time_left-=dtf;
                    updateValues(0,time_left);
                }
            }
        }
    }
    
    public void maxAccelTime(double distance_left, double curr_vel, double goal_vel, double max_a, double max_v)
    {
        //TODO: clean up unnecessary return statements
        double local_const_time=0;
        double start_a=0;
        if(distance_left > 0)
            start_a=max_a;
        else if(distance_left==0) {
            //TODO: Deal with velocity not right.
            dt2=0;
            a=0;
            const_time=0;
            dtf=0;
            af=0;
            return;
        }
        else {
            maxAccelTime(-distance_left, -curr_vel, -goal_vel, max_a, max_v);
            a*=-1;
            af*=-1;
            return;
        }
        double max_accel_velocity = distance_left * 2 * Math.abs(start_a) + curr_vel * curr_vel;
        if(max_accel_velocity > 0)
            max_accel_velocity=Math.sqrt(max_accel_velocity);
        else
            max_accel_velocity=-Math.sqrt(-max_accel_velocity);

        //Since we know what we'd have to do if we kept after it to decelerate, we know the sign of the acceleration.
        double final_a;
        if(max_accel_velocity>goal_vel)
            final_a=-max_a;
        else
            final_a=max_a;

        //We now know the top velocity we can get to
        double top_v = Math.sqrt((distance_left + (curr_vel * curr_vel) / (2.0 * start_a) + (goal_vel * goal_vel) / (2.0 * final_a)) / (-1.0 / (2.0 * final_a) + 1.0 / (2.0 * start_a)));

        //If it is too fast, we now know how long we get to accelerate for and how long to go at constant velocity
        double accel_time=0;
        if(top_v > max_v) {
            accel_time = (max_v - curr_vel) / max_a;
            local_const_time = (distance_left + (goal_vel * goal_vel - max_v * max_v) / (2.0 * max_a)) / max_v;
        }
        else
            accel_time = (top_v - curr_vel) / start_a;

        dt2=accel_time;
        a=start_a;
        const_time=local_const_time;
        dtf=(goal_vel-top_v)/final_a;
        af=final_a;
    }
    
}
