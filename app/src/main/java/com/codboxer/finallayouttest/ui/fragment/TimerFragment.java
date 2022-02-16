package com.codboxer.finallayouttest.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codboxer.finallayouttest.R;
import com.codboxer.finallayouttest.adapter.TimerItemAdapter;
import com.codboxer.finallayouttest.ui.activity.TimerScheduleActivity;
import com.codboxer.finallayouttest.model.TimerSchedule;
import com.codboxer.finallayouttest.ui.myinterface.ItemClickListener;
import com.codboxer.finallayouttest.alarm.ScheduleInfoProvider;
import com.codboxer.finallayouttest.viewmodel.AppViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TimerFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = TimerFragment.class.getSimpleName();

    private RecyclerView recyclerViewTimer;
    private FloatingActionButton fabAddTimer;

    private AppViewModel appViewModel;

    private TimerItemAdapter timerItemAdapter;

    public TimerFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_timer, container, false);

        // Mapping
        recyclerViewTimer = (RecyclerView) rootView.findViewById(R.id.recycle_view_list_timer);
        fabAddTimer = (FloatingActionButton)rootView.findViewById(R.id.fab_add_timer);
        // End mapping

        // Event listener
        fabAddTimer.setOnClickListener(this);

        // Set up recyclerview layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewTimer.setLayoutManager(linearLayoutManager);

        // Get all schedules from firebase and fill it to adapter
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        appViewModel.initTimer();
        appViewModel.getAllSchedules().observe(requireActivity(), new Observer<List<TimerSchedule>>() {
            @Override
            public void onChanged(List<TimerSchedule> schedules) {
                timerItemAdapter = new TimerItemAdapter(schedules, itemClickListener);
                recyclerViewTimer.setAdapter(timerItemAdapter);
                Log.e(TAG, "get schedules");

                // Alarm handle
                // sync allocatedRequestCodeList
                List<Integer> allocatedRequestCodes = new ArrayList<>();
                for(TimerSchedule schedule : schedules) {
                    allocatedRequestCodes.add(schedule.getRequestCode());
                }
                ScheduleInfoProvider.getInstance().setAllAllocatedRequestCodes(allocatedRequestCodes);

            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");
    }

    // Item in RecycleView click listener
    private ItemClickListener itemClickListener = new ItemClickListener() {
        @Override
        public void onItemClickListener(TimerSchedule timerSchedule, int position, boolean isLongClick) {
            openTimerScheduleActivity(false, position);
        }

        @Override
        public void onItemLongClickListener(TimerSchedule timerSchedule, int position, boolean isLongClick) {
            /* TODO: delete schedule*/
            openLongClickItemDialog(position);
        }

        @Override
        public void onSwitchClickListener(TimerSchedule timerSchedule, int position, boolean isChecked) {
            /* TODO: enable or disable valid of schedule*/
            Log.e(TAG, "checked switch change");
            appViewModel.setActiveSchedule(position,  isChecked);
        }
    };

    // Floating action button is clicked
    @Override
    public void onClick(View v) {
        if(timerItemAdapter == null) {
            openTimerScheduleActivity(true, 0);   // in case schedules is null at getAllSchedules()
        }
        else {
            openTimerScheduleActivity(true, timerItemAdapter.getItemCount());   // getItemCount return size of current schedules
        }
    }

    private void openTimerScheduleActivity(boolean isCreation, int id) {
        Intent intent = new Intent(getActivity(), TimerScheduleActivity.class);
        // Send boolean to TimerScheduleActivity to check the action is new creation or edition
        Bundle bundle = new Bundle();
        bundle.putBoolean("key_isCreation", isCreation);
        // set clicked id of timer schedule item
        bundle.putInt("key_id", id);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void openLongClickItemDialog(int id) {
        LongClickTimerItemDialog dialog = new LongClickTimerItemDialog(id);

        dialog.show(getChildFragmentManager(), LongClickTimerItemDialog.SCHEDULE_DELETE_DIALOG_TAG);
    }
}