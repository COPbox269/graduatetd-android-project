package com.codboxer.finallayouttest.model;

/**
 * @author Tran Ngoc Man
 * 19/05/21
 */
public class Sensor {
    private float voltage;
    private float current;
    private float power;
    private float energy;
    private float frequency;
    private float powerFactor;

    public Sensor() {
    }

    public Sensor(float voltage, float current, float power, float energy, float frequency, float powerFactor) {
        this.voltage = voltage;
        this.current = current;
        this.power = power;
        this.energy = energy;
        this.frequency = frequency;
        this.powerFactor = powerFactor;
    }

    public float getVoltage() {
        return voltage;
    }

    public void setVoltage(float voltage) {
        this.voltage = voltage;
    }

    public float getCurrent() {
        return current;
    }

    public void setCurrent(float current) {
        this.current = current;
    }

    public float getPower() {
        return power;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }

    public float getFrequency() {
        return frequency;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    public float getPowerFactor() {
        return powerFactor;
    }

    public void setPowerFactor(float powerFactor) {
        this.powerFactor = powerFactor;
    }
}
