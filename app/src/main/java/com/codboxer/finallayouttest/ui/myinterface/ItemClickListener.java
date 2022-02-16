package com.codboxer.finallayouttest.ui.myinterface;

import com.codboxer.finallayouttest.model.TimerSchedule;

/**
 * @author: Tran Ngoc Man
 * @usage: Click event listener for timer schedule RecycleView
 */

public interface ItemClickListener {
    void onItemClickListener(TimerSchedule timerSchedule, int position, boolean isLongClick);
    void onItemLongClickListener(TimerSchedule timerSchedule, int position, boolean isLongClick);
    void onSwitchClickListener(TimerSchedule timerSchedule, int position, boolean isChecked);
}
