/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj;

/**
 *
 * @author Nathan Walters
 */
public class Encoder {
    int count;
    public Encoder(int i, int j, int k) {
        //Do nothing
    }
    public int get() {
        return count;
    }
    public void set(int count) {
        this.count = count;
        System.out.println("Encoder count: " + count);
    }
    public void reset() {
        count = 0;
    }
    
    public void start() {
        //Do nothing here
    }
    
}
