package com.codboxer.finallayouttest.model;

public class Time implements Cloneable{
    private int hour;
    private int minute;

    public Time() {
    }

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public Time clone() throws CloneNotSupportedException {
        return (Time) super.clone();
    }

    @Override
    public String toString() {
        StringBuilder content = new StringBuilder();

        if(hour < 10) {
            content.append(0);
        }
        content.append(hour);

        content.append(":");

        if(minute < 10) {
            content.append(0);
        }
        content.append(minute);

        return content.toString().trim();
    }
}
