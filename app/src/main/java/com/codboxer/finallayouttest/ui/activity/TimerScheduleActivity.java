package com.codboxer.finallayouttest.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.codboxer.finallayouttest.R;
import com.codboxer.finallayouttest.model.ControlMode;
import com.codboxer.finallayouttest.model.Time;
import com.codboxer.finallayouttest.model.TimerSchedule;
import com.codboxer.finallayouttest.ui.fragment.CheckBoxItemBottomSheetDialog;
import com.codboxer.finallayouttest.ui.fragment.RepeatBottomSheetDialogFragment;
import com.codboxer.finallayouttest.viewmodel.AppViewModel;

public class TimerScheduleActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, TimePicker.OnTimeChangedListener {
    private static final String TAG = TimerScheduleActivity.class.getSimpleName();

    private ImageButton imageButtonCancel;
    private ImageButton imageButtonDone;

    private TimePicker timePicker;

    private TextView textViewRelay;
    private TextView textViewRepeat;
    private TextView textViewChosenRelay;
    private TextView textViewChosenRepeat;

    private RadioGroup radioGroupControl;
    private RadioButton radioButtonTurnON;
    private RadioButton radioButtonTurnOFF;

    private AppViewModel appViewModel;

    private ControlMode controlMode;
    private Time time;

    // Get bundle of TimerFragment Intent
    private boolean isCreation;
    private int id;

    /**
     * @author Tran Ngoc Man
      */
    public TimerScheduleActivity() {
        controlMode = new ControlMode();
        time = new Time();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_schedule);

        /* Mapping */
        imageButtonDone = (ImageButton) findViewById(R.id.image_button_done_activity);
        imageButtonCancel = (ImageButton) findViewById(R.id.image_button_cancel_activity);

        timePicker = (TimePicker) findViewById(R.id.time_picker);
        // Set mode 24h
        timePicker.setIs24HourView(true);

        textViewRelay = (TextView) findViewById(R.id.text_view_relay);
        textViewChosenRelay = (TextView) findViewById(R.id.text_view_relay_chosen);
        textViewRepeat = (TextView) findViewById(R.id.text_view_repeat);
        textViewChosenRepeat = (TextView) findViewById(R.id.text_view_repeat_chosen);

        radioGroupControl = (RadioGroup) findViewById(R.id.radio_group_control);
        radioButtonTurnON = (RadioButton) findViewById(R.id.radio_button_turn_on);
        radioButtonTurnOFF = (RadioButton) findViewById(R.id.radio_button_turn_off);
        /* End mapping */

        // Get extras from TimerFragment intent (isCreation and clicked id)
        getBundleOfTimerFragmentIntent();

        // Sync or Update UI
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
            appViewModel.getSchedule(id, isCreation).observe(this, new Observer<TimerSchedule>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onChanged(TimerSchedule schedule) {     // Display all info of clicked schedule
                    controlMode = schedule.getControlMode();
                    if(controlMode.isTurnOn())
                        radioGroupControl.check(R.id.radio_button_turn_on);
                    else
                        radioGroupControl.check(R.id.radio_button_turn_off);

                    textViewChosenRelay.setText(schedule.nameOfRelaysToString(TAG));

                    textViewChosenRepeat.setText(schedule.getRepeat().toString());

                    displayTime(schedule.getTime().getHour(), schedule.getTime().getMinute());

                    Log.e(TAG, "get schedule");
                }
            });

        /* Event Listener */
        imageButtonDone.setOnClickListener(this);
        imageButtonCancel.setOnClickListener(this);
        textViewRelay.setOnClickListener(this);
        textViewRepeat.setOnClickListener(this);

        radioGroupControl.setOnCheckedChangeListener(this);

        timePicker.setOnTimeChangedListener(this);
        /* End Event Listener */
    }


    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop()");
    }

    @Override
    public void onClick(View v) {
        int  clickedButtonId = v.getId();
        switch(clickedButtonId) {
            case R.id.image_button_cancel_activity: finish();
                                                    break;
            case R.id.image_button_done_activity:   // add or update schedule at id position
                                                    appViewModel.updateScheduleClone(controlMode);
                                                    appViewModel.setSchedule(id, isCreation);
                                                    finish();
                                                    break;
            case R.id.text_view_relay:  openRelayCheckboxDialog();
                                        break;
            case R.id.text_view_repeat: openRepeatOptionDialog();
                                        break;
            default:
                throw new IllegalStateException("Unexpected value: " + clickedButtonId);
        }
    }

    private void openRelayCheckboxDialog() {
        CheckBoxItemBottomSheetDialog checkBoxItemBottomSheetDialog = new CheckBoxItemBottomSheetDialog("RelayCheckboxes");

        // (Dialog) Display dialog
        checkBoxItemBottomSheetDialog.show(getSupportFragmentManager(), TAG);
    }

    private void openRepeatOptionDialog() {
        RepeatBottomSheetDialogFragment repeatBottomSheetDialogFragment = new RepeatBottomSheetDialogFragment(id, isCreation);

        repeatBottomSheetDialogFragment.show(getSupportFragmentManager(), RepeatBottomSheetDialogFragment.class.getSimpleName());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(checkedId == R.id.radio_button_turn_on) {
            controlMode.setTurnOn(true);
            controlMode.setName(radioButtonTurnON.getText().toString());
        }
        else if(checkedId == R.id.radio_button_turn_off) {
            controlMode.setTurnOn(false);
            controlMode.setName(radioButtonTurnOFF.getText().toString());
        }
    }

    // Initialize intent
    private void getBundleOfTimerFragmentIntent() {
        Intent intent  = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            isCreation = bundle.getBoolean("key_isCreation", false);

            id = bundle.getInt("key_id", 0);
        }
    }

    private void displayTime(int hour, int minute) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(hour);
            timePicker.setMinute(minute);
        } else {
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);
        }
    }

//    private Time getTimeOfTimePicker() {
//        int hour, minute;
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            hour = timePicker.getHour();
//            minute = timePicker.getMinute();
//        } else {
//            hour = timePicker.getCurrentHour();
//            minute = timePicker.getCurrentMinute();
//        }
//
//        return new Time(hour, minute);
//    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        time.setHour(hourOfDay);
        time.setMinute(minute);
        appViewModel.updateScheduleClone(time);
    }
}