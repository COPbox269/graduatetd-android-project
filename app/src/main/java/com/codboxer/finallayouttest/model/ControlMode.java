package com.codboxer.finallayouttest.model;

/**
 * @author Tran Ngoc Man
 * Created 01/04/2021
 */

public class ControlMode implements Cloneable {
    public static final boolean TURN_ON = true;
    public static final boolean TURN_OFF = false;

    private boolean isTurnOn;
    private String name;

    public ControlMode() {}

    public ControlMode(boolean isTurnOn, String name) {
        this.isTurnOn = isTurnOn;
        this.name = name;
    }

    public boolean isTurnOn() {
        return isTurnOn;
    }

    public String getName() {
        return name;
    }

    public void setTurnOn(boolean turnOn) {
        isTurnOn = turnOn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ControlMode clone() throws CloneNotSupportedException {
        return (ControlMode) super.clone();
    }
}
