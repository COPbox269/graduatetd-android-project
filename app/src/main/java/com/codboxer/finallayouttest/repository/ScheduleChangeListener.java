package com.codboxer.finallayouttest.repository;

import com.codboxer.finallayouttest.model.ControlMode;
import com.codboxer.finallayouttest.model.Relay;
import com.codboxer.finallayouttest.model.Repeat;

import java.util.List;

public interface ScheduleChangeListener {
    public void onIdOfScheduleLoaded(int id);

    public void onTimeLoaded(int hour, int minute);

    public void onControlModeLoaded(ControlMode controlMode);

    public void onIsOnLoaded(boolean isOn);

    public void onRelaysOfScheduleLoaded(List<Relay> relays);

    public void onRepeatOfScheduleLoaded(Repeat repeat);

}
