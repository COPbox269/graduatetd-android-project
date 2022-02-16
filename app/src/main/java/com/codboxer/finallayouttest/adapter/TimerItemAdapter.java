 package com.codboxer.finallayouttest.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.codboxer.finallayouttest.R;
import com.codboxer.finallayouttest.menum.EWeekday;
import com.codboxer.finallayouttest.model.Relay;
import com.codboxer.finallayouttest.model.TimerSchedule;
import com.codboxer.finallayouttest.ui.myinterface.ItemClickListener;

import java.util.List;

public class TimerItemAdapter extends RecyclerView.Adapter<TimerItemAdapter.TimerItemViewHolder> {
    private static final String TAG = TimerItemAdapter.class.getSimpleName();

    private List<TimerSchedule> scheduleList;
    private ItemClickListener itemClickListener;

    public TimerItemAdapter(List<TimerSchedule> scheduleList, ItemClickListener itemClickListener) {
        this.scheduleList = scheduleList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public TimerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_timer_schedule, parent, false);

        return new TimerItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimerItemViewHolder holder, int position) {
        TimerSchedule timerSchedule = scheduleList.get(position);
        if(timerSchedule == null)
            return;

        holder.bind(timerSchedule);

        // Event listener
        holder.relativeLayoutTimerItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClickListener(timerSchedule, position, false);
            }
        });

        holder.relativeLayoutTimerItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemClickListener.onItemLongClickListener(timerSchedule, position, true);
                // Set true if the callback consumed the long click, false otherwise.
                return true;
            }
        });

        holder.switchCompatTimerState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                itemClickListener.onSwitchClickListener(timerSchedule, position, isChecked);
            }
        });
        // End event listener
    }

    @Override
    public int getItemCount() {
        if(scheduleList == null || scheduleList.isEmpty()) {
            return  0;
        }
        else {
            return scheduleList.size();
        }
    }

    public static class TimerItemViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout relativeLayoutTimerItem;
        private TextView textViewTime;
        private TextView textViewRepeat;
        private TextView textViewRelay;
        private TextView textViewControlMode;
        private SwitchCompat switchCompatTimerState;

        public TimerItemViewHolder(@NonNull View itemView) {
            super(itemView);
            // Mapping
            relativeLayoutTimerItem = (RelativeLayout) itemView.findViewById(R.id.relative_layout_timer_item);
            textViewRelay = (TextView) itemView.findViewById(R.id.text_view_relay);
            textViewTime = (TextView) itemView.findViewById(R.id.text_view_display_time);
            textViewRepeat = (TextView) itemView.findViewById(R.id.text_view_display_repeat);
            textViewRelay = (TextView) itemView.findViewById(R.id.text_view_display_relay);
            textViewControlMode = (TextView) itemView.findViewById(R.id.text_view_display_controlmode);
            switchCompatTimerState = (SwitchCompat) itemView.findViewById(R.id.switch_timer);
            // End mapping
        }

        void bind(TimerSchedule schedule) {
            // Display each schedule info on item recyclerview
            textViewTime.setText(schedule.getTime().toString());

            textViewRelay.setText(schedule.nameOfRelaysToString(TAG));

            textViewRepeat.setText(schedule.getRepeat().toString());

            textViewControlMode.setText(schedule.getControlMode().getName());

            switchCompatTimerState.setChecked(schedule.isOn());
            // End displaying
        }
    }
}
