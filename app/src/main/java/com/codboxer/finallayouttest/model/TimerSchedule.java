package com.codboxer.finallayouttest.model;

import com.codboxer.finallayouttest.adapter.TimerItemAdapter;
import com.codboxer.finallayouttest.ui.activity.TimerScheduleActivity;

import java.util.List;

/**
 * @author Tran Ngoc Man
 * Edit 26/03/2021
 */

public class TimerSchedule implements Comparable<TimerSchedule>, Cloneable{
    private int id;
    private boolean isOn;
    private int requestCode;
    private ControlMode controlMode;
    private Time time;
    private List<Relay> relays;
    private Repeat repeat;

    public TimerSchedule() {
    }

    public TimerSchedule(int id, boolean isOn, int requestCode, Time time, ControlMode controlMode, List<Relay> relays, Repeat repeat) {
        this.id = id;
        this.isOn = isOn;
        this.requestCode = requestCode;
        this.controlMode = controlMode;
        this.time = time;
        this.relays = relays;
        this.repeat = repeat;
    }

    public int getId() {
        return id;
    }

    public boolean isOn() {
        return isOn;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public ControlMode getControlMode() {
        return controlMode;
    }

    public Time getTime() {
        return time;
    }

    public List<Relay> getRelays() {
        return relays;
    }

    public Repeat getRepeat() {
        return repeat;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public void setControlMode(ControlMode controlMode) {
        this.controlMode = controlMode;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public void setRelays(List<Relay> relays) {
        this.relays = relays;
    }

    public void setRepeat(Repeat repeat) {
        this.repeat = repeat;
    }

    /**
     *  This method use for providing a content to display name of relays
     * @param TAG- Tag of class where put this method and it will be related to each specific display case in each class
     * @return a name sequence of relays(list of relay)
     */
    public String nameOfRelaysToString(String TAG) {
        StringBuilder content = new StringBuilder("");

        if(relays != null) {
            for(Relay relay : relays){  // type of relay : Relay
                // Depend on how it is displayed in each class
                if(TAG.equals(TimerItemAdapter.class.getSimpleName())) {
                    content.append(", ");
                }
                else if(TAG.equals(TimerScheduleActivity.class.getSimpleName())) {
                    content.append("\n");
                }

                content.append(relay.getName());    // getName() of each relay
            }
            content.delete(0, 1);    // delete ", "  or "\n"
        }

        return content.toString().trim();
    }

    @Override
    public int compareTo(TimerSchedule compareSchedule) {
        int compareId = compareSchedule.getId();
        // ascending order
        return this.id - compareId;

        // descending order
        //return compareId - this.id;
    }

    public TimerSchedule clone() throws CloneNotSupportedException {
        return (TimerSchedule) super.clone();
    }
}
