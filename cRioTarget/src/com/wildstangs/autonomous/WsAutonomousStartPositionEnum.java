/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wildstangs.autonomous;

/**
 *
 * @author coder65535
 */
public class WsAutonomousStartPositionEnum 
{
    private int index;
    private String description, configName;
    public static final int POSITION_COUNT = 3;//Remember to change when defining new positions.
    
    public WsAutonomousStartPositionEnum(int index, String configName, String description)
    {
        this.configName = configName;
        this.index = index;
        this.description = description;
    }
    
    public static final WsAutonomousStartPositionEnum UNKNOWN = new WsAutonomousStartPositionEnum(0, "unknown", "Unknown Position");
    public static final WsAutonomousStartPositionEnum BACK_LEFT_PYRAMID_OUTSIDE = new WsAutonomousStartPositionEnum(1, "outsidePyramidBackLeft", "Outside pyramid, by back left corner");
    public static final WsAutonomousStartPositionEnum BACK_LEFT_PYRAMID_INSIDE = new WsAutonomousStartPositionEnum(2, "insidePyramidBackLeft", "Inside pyramid, by back left corner");
    public static final WsAutonomousStartPositionEnum POSITION3 = new WsAutonomousStartPositionEnum(3, "unknown", "Unknown Position");
    public static final WsAutonomousStartPositionEnum POSITION4 = new WsAutonomousStartPositionEnum(4, "unknown", "Unknown Position");
    public static final WsAutonomousStartPositionEnum POSITION5 = new WsAutonomousStartPositionEnum(5, "unknown", "Unknown Position");
    public static final WsAutonomousStartPositionEnum POSITION6 = new WsAutonomousStartPositionEnum(6, "unknown", "Unknown Position");
    public static final WsAutonomousStartPositionEnum POSITION7 = new WsAutonomousStartPositionEnum(7, "unknown", "Unknown Position");
    public static final WsAutonomousStartPositionEnum POSITION8 = new WsAutonomousStartPositionEnum(8, "unknown", "Unknown Position");
    public static final WsAutonomousStartPositionEnum POSITION9 = new WsAutonomousStartPositionEnum(9, "unknown", "Unknown Position");    /**
 * Converts the enum type to a String. 
 * 
 * @return A string representing the enum.
 */
    public String toString() {
        return description;
    }
    
    public String toConfigString()
    {
        return configName;
    }
    
/** 
 * Converts the enum type to a numeric value.
 * 
 * @return An integer representing the enum.
 */
    public int toValue() {
        return index;
    }
    
    public static WsAutonomousStartPositionEnum getEnumFromValue(int i)
    {
        switch (i) {
            case 0:
                return WsAutonomousStartPositionEnum.UNKNOWN;
            case 1:
                return WsAutonomousStartPositionEnum.BACK_LEFT_PYRAMID_OUTSIDE;
            case 2:
                return WsAutonomousStartPositionEnum.BACK_LEFT_PYRAMID_INSIDE;
            case 3:
                return WsAutonomousStartPositionEnum.POSITION3;
            case 4:
                return WsAutonomousStartPositionEnum.POSITION4;
            case 5:
                return WsAutonomousStartPositionEnum.POSITION5;
            case 6:
                return WsAutonomousStartPositionEnum.POSITION6;
            case 7:
                return WsAutonomousStartPositionEnum.POSITION7;
            case 8:
                return WsAutonomousStartPositionEnum.POSITION8;
            case 9:
                return WsAutonomousStartPositionEnum.POSITION9;
            default:
                return null;
        }
    }

}
