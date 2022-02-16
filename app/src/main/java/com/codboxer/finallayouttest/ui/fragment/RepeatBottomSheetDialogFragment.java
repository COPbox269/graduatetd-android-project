package com.codboxer.finallayouttest.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.codboxer.finallayouttest.R;
import com.codboxer.finallayouttest.model.Repeat;
import com.codboxer.finallayouttest.model.TimerSchedule;
import com.codboxer.finallayouttest.repository.RepeatChangeListener;
import com.codboxer.finallayouttest.ui.myinterface.DismissDialogListener;
import com.codboxer.finallayouttest.viewmodel.AppViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class RepeatBottomSheetDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private static final String TAG = RepeatBottomSheetDialogFragment.class.getSimpleName();

    private RadioButton radioButtonOnce;
    private RadioButton radioButtonDaily;
    private RadioButton radioButtonWeekdayOption;

    private TextView textViewChosenWeekday;

    // Determine add or update action
    private int id;
    private boolean isCreation;

    private AppViewModel appViewModel;

    private int selectedRepeatId;

    public RepeatBottomSheetDialogFragment(int id, boolean isCreation) {
        this.id = id;
        this.isCreation = isCreation;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set dialog shape
        setStyle(STYLE_NORMAL, R.style.BottomDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dialog_bottomsheet_repeat, container, false);

        /* Mapping */
        radioButtonOnce = (RadioButton) rootView.findViewById(R.id.radio_button_once);
        radioButtonDaily = (RadioButton) rootView.findViewById(R.id.radio_button_daily);
        radioButtonWeekdayOption = (RadioButton) rootView.findViewById(R.id.radio_button_weekday_option);

        textViewChosenWeekday = (TextView) rootView.findViewById(R.id.text_view_chosen_weekday);
        textViewChosenWeekday.setGravity(Gravity.CENTER|Gravity.RIGHT);
        /* End Mapping */

        // Sync or Update UI
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        appViewModel.getRepeat(new RepeatChangeListener() {
            @Override
            public void onLoaded(Repeat repeat) {
                Log.e(TAG, "getSchedule");

                // sync
                selectedRepeatId = repeat.getId();

                int repeatId = repeat.getId();
                switch (repeatId) {
                    case 0: radioButtonOnce.setChecked(true);
                        radioButtonDaily.setChecked(false);
                        radioButtonWeekdayOption.setChecked(false);
                        textViewChosenWeekday.setText("");
                        break;
                    case 1: radioButtonDaily.setChecked(true);
                        radioButtonOnce.setChecked(false);
                        radioButtonWeekdayOption.setChecked(false);
                        textViewChosenWeekday.setText("");
                        break;
                    case 2: radioButtonWeekdayOption.setChecked(true);
//                        // if all weekdays is chosen, uncheck radioButtonWeekdayOption and check radioButtonDaily
                        String weekdayContent = repeat.toString();
//                        if(weekdayContent == "DAILY") {
//                            radioButtonDaily.setChecked(true);
//                            radioButtonWeekdayOption.setChecked(false);
//                            radioButtonOnce.setChecked(false);
//                        }
//                        else {
                            textViewChosenWeekday.setText(weekdayContent);
//                        }
                        break;
                }
            }
        });

        /* Event Listener */
        radioButtonOnce.setOnClickListener(this);
        radioButtonDaily.setOnClickListener(this);
        radioButtonWeekdayOption.setOnClickListener(this);
        /* End event Listener */

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /* Handling code when dialog close*/
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.e(TAG, "onDismiss()");
        //send to schedule
        if(selectedRepeatId != Repeat.WEEKDAYS) // in case full weekdays handle (see in fetchWeekdays() method in AppRepositoryImpl.class)
            appViewModel.updateScheduleClone(selectedRepeatId);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop");
    }

    // RadioButton is clicked
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch(id) {
            case R.id.radio_button_once:
                selectedRepeatId = 0;
                radioButtonDaily.setChecked(false);
                radioButtonWeekdayOption.setChecked(false);
                break;

            case R.id.radio_button_daily:
                selectedRepeatId = 1;
                radioButtonOnce.setChecked(false);
                radioButtonWeekdayOption.setChecked(false);
                break;

            case R.id.radio_button_weekday_option:
                selectedRepeatId = 2;
                radioButtonOnce.setChecked(false);
                radioButtonDaily.setChecked(false);
                openWeekdayCheckboxesDialog();
                break;
        }
    }

    private void openWeekdayCheckboxesDialog() {
        CheckBoxItemBottomSheetDialog checkBoxItemBottomSheetDialog = new CheckBoxItemBottomSheetDialog("WeekdayCheckboxes", listener);
        checkBoxItemBottomSheetDialog.show(getChildFragmentManager(), TAG);
    }

    DismissDialogListener listener = new DismissDialogListener() {
        @Override
        public void onHide() {
                dismiss();
        }
    };
}