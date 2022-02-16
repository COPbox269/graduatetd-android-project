package com.codboxer.finallayouttest.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codboxer.finallayouttest.model.Control;
import com.codboxer.finallayouttest.model.ControlMode;
import com.codboxer.finallayouttest.model.ItemData;
import com.codboxer.finallayouttest.model.Repeat;
import com.codboxer.finallayouttest.model.Sensor;
import com.codboxer.finallayouttest.model.SpeechCommand;
import com.codboxer.finallayouttest.model.Time;
import com.codboxer.finallayouttest.model.TimerSchedule;
import com.codboxer.finallayouttest.repository.AppRepositoryImpl;
import com.codboxer.finallayouttest.repository.ControlChangeListener;
import com.codboxer.finallayouttest.repository.ItemChangeListener;
import com.codboxer.finallayouttest.repository.RepeatChangeListener;
import com.codboxer.finallayouttest.repository.SpeechCommandLoadListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tran Ngoc Man
 * Created 28/03/2021
 */

public class AppViewModel extends ViewModel {
    private static final String TAG = AppViewModel.class.getSimpleName();

    private MutableLiveData<List<TimerSchedule>> schedules;
    private MutableLiveData<TimerSchedule> schedule;

    private MutableLiveData<Control> control;

    private MutableLiveData<List<SpeechCommand>> speechCommands;
    private MutableLiveData<SpeechCommand> speechCommand;

    private MutableLiveData<Sensor> sensor;

    public void setSpeechCommand(int id, SpeechCommand speechCommand) {
        AppRepositoryImpl.getInstance().updateSpeechCommand(id, speechCommand);
    }


    /*--- Get info ---*/
    /**
     * Get all schedules from child "timer" of database and save as MutableLiveData
     */
    public void initTimer() {
        if(schedules != null) {
            Log.e(TAG, "initTimer(): no null");
            return;
        }
        Log.e(TAG, "initTimer(): null");
        schedules = (MutableLiveData<List<TimerSchedule>>) AppRepositoryImpl.getInstance().fetchAllSchedules();
    }

    public LiveData<List<TimerSchedule>> getAllSchedules() {
        return schedules;
    }

    public LiveData<TimerSchedule> getSchedule(int id, boolean isCreation) {
        // schedule always null
        if(isCreation) {
            schedule = (MutableLiveData<TimerSchedule>) AppRepositoryImpl.getInstance().fetchScheduleByDefault(id);
        }
        else {
            try {
                schedule = (MutableLiveData<TimerSchedule>) AppRepositoryImpl.getInstance().fetchScheduleById(id);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        return schedule;
    }

    public LiveData<Control> getControl(ControlChangeListener listener) {
        control = (MutableLiveData<Control>) AppRepositoryImpl.getInstance().fetchControl(listener);

        return control;
    }

    public List<ItemData> getItems(String TAG, ItemChangeListener listener) {
        if(TAG == "RelayCheckboxes") {
            return AppRepositoryImpl.getInstance().fetchRelayItem(listener);
        }
        else if(TAG == "WeekdayCheckboxes") {
            return AppRepositoryImpl.getInstance().fetchWeekdayItem(listener);
        }
        return new ArrayList<>();
    }

    public Repeat getRepeat(RepeatChangeListener listener) {
        return AppRepositoryImpl.getInstance().fetchRepeat(listener);
    }

    public void initSpeechCommands() {
        if(speechCommands != null) {
            Log.e(TAG, "initSpeechCommand(): no null");
            return;
        }
        Log.e(TAG, "initSpeechCommand(): null");
        speechCommands = (MutableLiveData<List<SpeechCommand>>) AppRepositoryImpl.getInstance().fetchAllSpeechCommands();
    }

    public LiveData<List<SpeechCommand>> getAllSpeechCommands() {
        return speechCommands;
    }
    /*--- End of getter ---*/

    /*--- Set ---*/
    /**
     * Add or update schedule (click "done" button) follow the id. This set the schedule clone to database
     * @param id
     */
    public void setSchedule(int id, boolean isCreation) {
        AppRepositoryImpl.getInstance().setSchedule(id , isCreation);
    }
    /*--- End of setter ---*/


    /*--- Update info ---*/
    public void updateScheduleClone(String TAG, List<ItemData> items) {
        if(TAG == "RelayCheckboxes") {
            AppRepositoryImpl.getInstance().updateRelays(items);
        }
        else if(TAG == "WeekdayCheckboxes") {
            AppRepositoryImpl.getInstance().updateWeekdays(items);
        }

    }

    public void updateScheduleClone(int selectedRepeatId) {
        AppRepositoryImpl.getInstance().updateRepeat(selectedRepeatId);
    }

    public void updateScheduleClone(ControlMode controlMode) {
        AppRepositoryImpl.getInstance().updateControlMode(controlMode);
    }

    public void updateScheduleClone(Time time) {
        AppRepositoryImpl.getInstance().updateTime(time);
    }

    public void setActiveSchedule(int id, boolean isOn) {
        AppRepositoryImpl.getInstance().updateActiveSchedule(id, isOn);
    }

    /**
     * Update name of relay by id, this also update mutableLiveDataControl
     */
    public void updateRelaysInfoFromControl(int id, String name) {
        AppRepositoryImpl.getInstance().updateRelayInfoFromControl(id, name);
    }

    public void updateStatesInfo(int id, boolean state) {
        AppRepositoryImpl.getInstance().updateStatesInfo(id, state);
    }
    /*--- End of updating ---*/

    public void deleteSchedule(int id) {
        AppRepositoryImpl.getInstance().deleteSchedule(id);
    }

    public List<String> getRelayNameList() {
        return AppRepositoryImpl.getInstance().fetchRelayNameList();
    }

    public void getSpeechCommand(boolean isCreation, int id, SpeechCommandLoadListener loadListener) {
        AppRepositoryImpl.getInstance().fetchCommandById(id, loadListener);
    }

    public void deleteSpeechCommand(int id) {
        AppRepositoryImpl.getInstance().deleteSpeechCommand(id);
    }

    public void updateSpeechCommandInfo(int id, boolean isOn) {
        AppRepositoryImpl.getInstance().updateSpeechCommandInfo(id, isOn);
    }

    public void speechCommandMatches(String speechInputResult) {
        AppRepositoryImpl.getInstance().speechCommandMatches(speechInputResult);
    }


/**********__________ Parameter Block __________**********/
    public void initSensorData() {
        if(sensor != null) {
            Log.e(TAG, "initSensorData(): no null");
            return;
        }
        Log.e(TAG, "initSensorData: null");
        sensor = (MutableLiveData<Sensor>) AppRepositoryImpl.getInstance().fetchSensorData();
    }

    public LiveData<Sensor> getSensorData() {
        return sensor;
    }
/*****-----*****-----*****_____*****-----*****-----*****/
}
