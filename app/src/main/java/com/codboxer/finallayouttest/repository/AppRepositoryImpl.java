package com.codboxer.finallayouttest.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.codboxer.finallayouttest.model.Control;
import com.codboxer.finallayouttest.model.ControlMode;
import com.codboxer.finallayouttest.model.ItemData;
import com.codboxer.finallayouttest.model.Relay;
import com.codboxer.finallayouttest.model.Repeat;
import com.codboxer.finallayouttest.model.Sensor;
import com.codboxer.finallayouttest.model.SpeechCommand;
import com.codboxer.finallayouttest.model.Time;
import com.codboxer.finallayouttest.model.TimerSchedule;
import com.codboxer.finallayouttest.model.Weekday;
import com.codboxer.finallayouttest.alarm.ScheduleInfoProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author Tran Ngoc Man
 * Created 27/03/2021
 */

public class AppRepositoryImpl {
    private static final String TAG = AppRepositoryImpl.class.getSimpleName();

    private static AppRepositoryImpl instance;

    private List<TimerSchedule> schedules;
    private TimerSchedule schedule;
    private List<ItemData> checkedItems;    // contain chosen relays and chosen weekdays
    private Control control;
    private List<Relay> relays;
    private Repeat repeat;
    private List<Boolean> states;
    private SpeechCommand speechCommand;
    private List<SpeechCommand> speechCommands;
    private Sensor sensor;

    private MutableLiveData<List<TimerSchedule>> mutableLiveDataSchedules;
    private MutableLiveData<TimerSchedule> mutableLiveDataSchedule;

    private MutableLiveData<Control> mutableLiveDataControl;
    private MutableLiveData<List<Relay>> mutableLiveDataRelays;

    private MutableLiveData<SpeechCommand> mutableLiveDataSpeechCommand;
    private MutableLiveData<List<SpeechCommand>> mutableLiveDataSpeechCommands;

    private MutableLiveData<Sensor> mutableLiveDataSensor;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabaseRef;

    // block read state from firebase at ControlFragment interface
    private boolean flag_readStates = false;

    private AppRepositoryImpl() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = firebaseDatabase.getReference();

        schedules = new ArrayList<>();
        schedule = new TimerSchedule();
        checkedItems = new ArrayList<>();

        control = new Control();
        relays = new ArrayList<>();
        states = new ArrayList<>();

        speechCommand = new SpeechCommand();
        speechCommands = new ArrayList<>();

        sensor = new Sensor();

        mutableLiveDataSchedules = new MutableLiveData<>();
        mutableLiveDataSchedule = new MutableLiveData<>();

        mutableLiveDataControl = new MutableLiveData<>();
        mutableLiveDataRelays = new MutableLiveData<>();

        mutableLiveDataSpeechCommand = new MutableLiveData<>();
        mutableLiveDataSpeechCommands = new MutableLiveData<>();

        mutableLiveDataSensor = new MutableLiveData<>();
    }

    // Singleton design pattern
    public static AppRepositoryImpl getInstance() {
        if(instance == null) {
            instance = new AppRepositoryImpl();
        }
        return instance;
    }

/*************-------------------- Timer Schedule Block --------------------************/
/**********************************************************************************/
    public LiveData<List<TimerSchedule>> fetchAllSchedules() {
    final Query fetchScheduleInfoQuery = mDatabaseRef.child("timer").orderByKey();
    fetchScheduleInfoQuery.addValueEventListener(new ValueEventListener() {     // this method will repeat if child "timer" change data (in here is name of relay)
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()) {
                GenericTypeIndicator<List<TimerSchedule>> genericTypeIndicator = new GenericTypeIndicator<List<TimerSchedule>> () {};
                schedules = snapshot.getValue(genericTypeIndicator);

                Log.e(TAG, "fetchAllSchedules()");

                mutableLiveDataSchedules.setValue(schedules);
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    });

    return mutableLiveDataSchedules;
}

    public LiveData<TimerSchedule> fetchScheduleById(int id) throws CloneNotSupportedException {
        // fetch clicked schedule from firebase by id
        // NOTE: Must'nt use "=" operator because it impact on root schedules
        schedule = schedules.get(id).clone();     // mutableLiveDataSchedules (= schedules) is called before in TimerFragment, by the MainActivity is'nt be killed, the value is still live

        mutableLiveDataSchedule.setValue(schedule);

        return mutableLiveDataSchedule;
    }

    public LiveData<TimerSchedule> fetchScheduleByDefault(int id) {
        // fetch default timer
        List<Weekday> defaultWeekdays = new ArrayList<>();
        List<Relay> defaultRelays = new ArrayList<>();

        // relays have value by assigning in fetchingControl() below
        if(relays != null) {
            defaultRelays.add(relays.get(0));
        }

        // set time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        schedule = new TimerSchedule(id, true, 0, new Time(hour, minute), new ControlMode(true, "Turn on"), defaultRelays, new Repeat(0, defaultWeekdays));
        // Before clicking "done", we may manipulate with temporary schedule in this class

        mutableLiveDataSchedule.setValue(schedule);

        return mutableLiveDataSchedule;
    }

    public void setSchedule(int id, boolean isCreation) {
//        if(schedule != null) {      // check fetched schedule not null
        /* TODO: catch all info of schedule  if its necessary (in case use SavedStateViewModel)*/

        // after doing with schedule, clicked "done" button always makes schedule active (get root not clone)
        schedule.setOn(true);   // this mean switchTimer.setCheck(true)

        // initialize code for alarm usage
        int requestCode = 0;

        if(!isCreation) {   // schedule already exists
            // Set to schedules live data to TimerFragment observer to update UI
            // when schedule is not change after fetching, the addListenerForSingleValueEvent() of Firebase is at called
            // actually child "timer" does not change
            schedules.set(id, schedule);    // only for available schedule

            mutableLiveDataSchedules.setValue(schedules);

            requestCode = schedule.getRequestCode();
        }
        else {
            // set id for new schedule
            schedule.setId(id);     // only for new schedule

            requestCode = ScheduleInfoProvider.getInstance().generateRequestCode();
        }

        // set requestCode for alarm usage
        schedule.setRequestCode(requestCode);

        // always call addAlarm()

//        ScheduleAlarmManager.getInstance().addAlarm(schedule);

        // Set to Firebase
        mDatabaseRef.child("timer").child(String.valueOf(id)).setValue(schedule, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if(error == null) { // success
                }
            }
        });
    }

    public List<ItemData> fetchRelayItem(ItemChangeListener itemChangeListener) {
        List<ItemData> items = new ArrayList<>();
        checkedItems = new ArrayList<>();

        items.add(relays.get(0));
        items.add(relays.get(1));
        items.add(relays.get(2));
        items.add(relays.get(3));

        for(Relay relayLoop: schedule.getRelays()) {
            checkedItems.add(relayLoop);
        }

        itemChangeListener.onItemLoaded(items, checkedItems);

        return items;
    }

    public List<ItemData> fetchWeekdayItem(ItemChangeListener itemChangeListener) {
        List<ItemData> items = new ArrayList<>();
        checkedItems = new ArrayList<>();

        items.add(new Weekday(0, "Monday"));
        items.add(new Weekday(1, "Tuesday"));
        items.add(new Weekday(2, "Wednesday"));
        items.add(new Weekday(3, "Thursday"));
        items.add(new Weekday(4, "Friday"));
        items.add(new Weekday(5, "Saturday"));
        items.add(new Weekday(6, "Sunday"));

        List<Weekday> weekdaysLoop = schedule.getRepeat().getWeekdays();
        if(weekdaysLoop != null) {  // check if fetched repeat id is not 2 (not "weekday option)
            for(Weekday weekdayLoop : weekdaysLoop)
                checkedItems.add(weekdayLoop);
        }

        itemChangeListener.onItemLoaded(items, checkedItems);

        return items;
    }

    public Repeat fetchRepeat(RepeatChangeListener listener) {
        repeat = schedule.getRepeat();
        listener.onLoaded(repeat);

        return repeat;
    }

    public void updateRelayInfoFromControl(int id, String name) {
        // update to "control" database
        mDatabaseRef.child("control/relays").child(String.valueOf(id))
                .child("name").setValue(name, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if(error == null) { // success
                    // TODO handle success event
                    Control control = mutableLiveDataControl.getValue();    // local variable
                    control.getRelays().get(id).setName(name);

                    mutableLiveDataControl.setValue(control);

//                    flag_readStates = true;
                }
            }
        });
        // Update schedule "timer" mark to changed relay
        // Check child exist
//        schedules = mutableLiveDataSchedules.getValue();
        if(schedules != null) {
            // scan schedules if it has changed relay
            for(TimerSchedule scheduleLoop : schedules) {
                for(Relay relayLoop : scheduleLoop.getRelays()) {
                    if(id == relayLoop.getId()) {
                        relayLoop.setName(name);
                    }
                }
            }

            Log.e(TAG, "in updateRelayInfo(): schedules no null");
            mDatabaseRef.child("timer").setValue(schedules, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if(error == null) {
                        // TODO handle when successful
                        Log.e(TAG, "in updateRelayInfo(): setValue to Firebase successfully");
                    }
                }
            });
        }
    }

    public void updateRelays(List<ItemData> items) {
        List<Relay> tempRelays = new ArrayList<>();
        if(items == null || items.isEmpty()) {       // when relays is null (not have checked Relay)
            /* TODO: Make default checked relay item*/
            tempRelays.add(relays.get(0));
        }
        else {
            for(ItemData itemLoop : items) {
                tempRelays.add((Relay) itemLoop);
            }
        }

        schedule.setRelays(tempRelays);

        mutableLiveDataSchedule.setValue(schedule);
    }

    public void updateWeekdays(List<ItemData> items) {
        List<Weekday> tempWeekdays = new ArrayList<>();
        if(items == null || items.isEmpty()) {       // when weekdays is null (not have checked Relay)
            /* TODO: Make default checked weekday item*/
            tempWeekdays.add(new Weekday(0, "Monday"));
        }
        else {
            for(ItemData itemLoop : items) {
                tempWeekdays.add((Weekday) itemLoop);
            }
        }

        // clone repeat again, if not, weekday setter can makes original changes
        try {
            repeat = schedule.getRepeat().clone();

            if(tempWeekdays.size() == 7) { // full weekdays
                repeat.setId(Repeat.DAILY);
                repeat.setWeekdays(null);
            }
            else {
                repeat.setWeekdays(tempWeekdays);
                repeat.setId(Repeat.WEEKDAYS);    // this method is called when CheckBoxBottomSheetDialog dismisses hence the repeat id must be 2
            }
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        schedule.setRepeat(repeat);

        mutableLiveDataSchedule.setValue(schedule);
    }

    public void updateRepeat(int selectedRepeatId) {
        try {
            repeat = schedule.getRepeat().clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        repeat.setId(selectedRepeatId);
        if(selectedRepeatId != 2) { // not weekdays option
            repeat.setWeekdays(null);   // set null/empty weekdays when "Once" or "Daily" is selected
        }

        schedule.setRepeat(repeat);

        mutableLiveDataSchedule.setValue(schedule);
    }

    public void updateActiveSchedule(int id, boolean isOn) {
        schedules.get(id).setOn(isOn);

        Log.e(TAG, "updateActiveSchedule()");

//        if(!isOn) {
//            // TODO cancel Alarm
//            ScheduleAlarmManager.getInstance().cancelAlarm(schedules.get(id));
//        }
//        else {
//            // TODO update Alarm
//
//            ScheduleAlarmManager.getInstance().addAlarm(schedules.get(id));
//        }

        mDatabaseRef.child("timer").setValue(schedules);
    }

    public void updateTime(Time time) {
        schedule.setTime(time);
    }

    public void deleteSchedule(int id) {
        // Alarm handle
//        // retrieve requestCode of this schedule and remove it
        ScheduleInfoProvider.getInstance().retrieveRequestCode(schedules.get(id).getRequestCode());
        // cancel
//        ScheduleAlarmManager.getInstance().cancelAlarm(schedules.get(id));

        if(schedules.removeIf(scheduleToRemove -> id == scheduleToRemove.getId())) {
            // update id of each schedule in schedules
            for(TimerSchedule schedule: schedules) {
                schedule.setId(schedules.indexOf(schedule));
            }

            mDatabaseRef.child("timer").setValue(schedules, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if(error == null) {     // success
                    }
                }});

            // Set for live data to observer because when the child "timer" is deleted, the addValueChangeListener()
            // method can be called
            if(schedules == null || schedules.isEmpty()) {
                mutableLiveDataSchedules.setValue(schedules);
            }
        }
    }

/***************************************************************************************/
/*****************--------------- End Schedule Block ---------------*****************/


/*--------------------------------- Control Block ---------------------------------*/
/***************************************************************************************/
    public LiveData<Control> fetchControl(ControlChangeListener listener) {
        mDatabaseRef.child("control").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    GenericTypeIndicator<Control> genericTypeIndicator = new GenericTypeIndicator<Control> () {};
                    control = snapshot.getValue(genericTypeIndicator);
                    mutableLiveDataControl.setValue(control);

                    Log.e("ControlFragment", "controlEvent");


                    // relays after fetching as instance variable
                    relays = control.getRelays();
                    listener.onRelaysChange(relays);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // Every time this child change, the value states are updated, the switch state sync to it
        mDatabaseRef.child("control/states").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    GenericTypeIndicator<List<Boolean>> genericTypeIndicator = new GenericTypeIndicator<List<Boolean>> () {};
                    states = snapshot.getValue(genericTypeIndicator);

                    Log.e(TAG, String.valueOf(flag_readStates));

                    Log.e("ControlFragment", "statesEvent");

                    if(!flag_readStates){
                        listener.onStatesChange(states);
                        Log.e("ControlFragment", "interfaceStateEvent");

                    }

                    flag_readStates = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        return mutableLiveDataControl;
    }

    public void updateControlMode(ControlMode controlMode) {
        schedule.setControlMode(controlMode);
    }

    public void updateStatesInfo(int id, boolean state) {
        flag_readStates = true;

        mDatabaseRef.child("control/states").child(String.valueOf(id)).setValue(state);

        Log.e("ControlFragment", "updateState from user click");

        /* TODO: update control/mutableLiveDataControl if necessary*/
    }
/**************************************************************************************/
/*************-------------------- End Control Block --------------------*************/


/*------------------------------- Speech Command Block -------------------------------*/
/***************************************************************************************/
    public LiveData<List<SpeechCommand>> fetchAllSpeechCommands() {
        final Query fetchSpeechCommandInfoQuery = mDatabaseRef.child("speechCommand").orderByKey();
        fetchSpeechCommandInfoQuery.addValueEventListener(new ValueEventListener() {     // this method will repeat if child "timer" change data (in here is name of relay)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    GenericTypeIndicator<List<SpeechCommand>> genericTypeIndicator = new GenericTypeIndicator<List<SpeechCommand>> () {};
                    speechCommands = snapshot.getValue(genericTypeIndicator);

                    mutableLiveDataSpeechCommands.setValue(speechCommands);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return mutableLiveDataSpeechCommands;
    }

    public void fetchCommandById(int id, SpeechCommandLoadListener loadListener) {
        speechCommands = mutableLiveDataSpeechCommands.getValue();
        loadListener.onLoaded(speechCommands.get(id));
        Log.e("TAG", speechCommands.get(id).getActions().toString());
        Log.e("TAG", speechCommands.get(id).getCommands().toString());
    }

    public void updateSpeechCommand(int id, SpeechCommand speechCommand) {
        mDatabaseRef.child("speechCommand").child(String.valueOf(id)).setValue(speechCommand, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

            }
        });
    }

    public void deleteSpeechCommand(int id) {
        if(speechCommands.removeIf(speechCommandToRemove -> id == speechCommandToRemove.getId())) {
            // update id of each speechCommand in speechCommands
            for(SpeechCommand speechCommandLoop : speechCommands) {
                speechCommandLoop.setId(speechCommands.indexOf(speechCommandLoop));
            }

            // Update to database
            mDatabaseRef.child("speechCommand").setValue(speechCommands, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if(error == null) {     // success
                    }
                }});

            // Set for live data to observer because when the child "speechCommand" is deleted
            //  the addValueChangeListener() method can be called
            if(speechCommands == null || speechCommands.isEmpty()) {
                mutableLiveDataSpeechCommands.setValue(speechCommands);
            }
        }
    }

    public void updateSpeechCommandInfo(int id, boolean isOn) {
        mDatabaseRef.child("speechCommand").child(String.valueOf(id)).child("on").setValue(isOn);
    }

    public void speechCommandMatches(String speechInputResult) {
//        speechCommands = mutableLiveDataSpeechCommands.getValue();

        if(speechCommands == null || speechCommands.isEmpty()) {
            Log.e("TEST_NOW", "null");
            fetchAllSpeechCommands();
        }
        else {
            Log.e("TEST_NOW", "no null");
//            speechCommands = mutableLiveDataSpeechCommands.getValue();
        }

        // Compare and Set relay state if match true
        for(SpeechCommand speechCommandLoop : speechCommands) {
            if(speechCommandLoop.isOn()) {
                /* TODO do on active speech command*/
                for(String commandLoop : speechCommandLoop.getCommands()) {
                    if(commandLoop.trim().toLowerCase().equals(speechInputResult.trim().toLowerCase())) {
                        Log.e("TEST_NOW", "true match");

                        // update relay state
                        List<Boolean> relayStates = new ArrayList<>();
                        relayStates.addAll(control.getStates());
                        Log.e("TEST_NOW_RELAY_STATES", relayStates.toString());

                        List<Integer> actionsLoop = new ArrayList<>();
                        actionsLoop.addAll(speechCommandLoop.getActions());
                        Log.e("TEST_NOW_ACTIONS", actionsLoop.toString());

                        int sizeToLoop = actionsLoop.size();
                        for(int i = 0; i < sizeToLoop; i++) {
                            Log.e("TEST_NOW_ACTION", String.valueOf(i));
                            int actionLoop = actionsLoop.get(i);

                            boolean relayStateLoop = relayStates.get(i);
                            // convert relay state boolean type to relay state int type
                            int relayStateToInt;
                            if(relayStateLoop) {
                                relayStateToInt = 1;
                            }
                            else {
                                relayStateToInt = 2;
                            }

                            if (actionLoop != SpeechCommand.NONE_STATE_ACTION) {
                                /* TODO update relay state */

//                              if(actionLoop != relayStateToInt) {
                                    if(actionLoop == SpeechCommand.ON_STATE_ACTION) {
                                        Log.e("TEST_NOW", "state at " + String.valueOf(i) + " ON");
                                        mDatabaseRef.child("control/states")
                                                .child(String.valueOf(i)).setValue(true);
                                    }
                                    else if(actionLoop == SpeechCommand.OFF_STATE_ACTION) {
                                        Log.e("TEST_NOW", "state at " + String.valueOf(i) + " OFF");
                                        mDatabaseRef.child("control/states")
                                                .child(String.valueOf(i)).setValue(false);
                                    }
//                              }
                            }
                        }

                        break;
                    }
                }
            }
        }
    }

    public List<String> fetchRelayNameList() {
        List<String> relayNames = new ArrayList<>();

        for(Relay relay : control.getRelays()) {
            relayNames.add(relay.getName());
        }

        return relayNames;
    }

/*******************************************************************************/
/***********--------------- End Speech Command Block ---------------***********/


/*------------------------------------ Sensor Block ------------------------------------*/
/***************************************************************************************/
    public LiveData<Sensor> fetchSensorData() {

        final Query fetchSensorInfoQuery = mDatabaseRef.child("sensor");
        fetchSensorInfoQuery.addValueEventListener(new ValueEventListener() {     // this method will repeat if child "sensor" change data (in here is value of sensor)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    GenericTypeIndicator<Sensor> genericTypeIndicator = new GenericTypeIndicator<Sensor> () {};
                    sensor = snapshot.getValue(genericTypeIndicator);

                    Log.e(TAG, "fetchSensorData()");

                    mutableLiveDataSensor.setValue(sensor);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return mutableLiveDataSensor;
    }

/*******************************************************************************/
/**************---------------- End Sensor Block ----------------**************/

}
