package com.codboxer.finallayouttest.repository;

import com.codboxer.finallayouttest.model.Control;
import com.codboxer.finallayouttest.model.TimerSchedule;

import java.util.List;

/**
 * @author Tran Ngoc Man
 * Created 27/03/2021
 * @usage Writing an interface DataChangeListener.java to update our Activity    when the apps are fetched
 */

public interface DataChangeListener {
    void onScheduleLoaded(List<TimerSchedule> schedules);
    void onControlLoaded(List<Control> controls);
}
