package com.codboxer.finallayouttest.ui.myinterface;

import com.codboxer.finallayouttest.model.SpeechCommand;
import com.codboxer.finallayouttest.model.TimerSchedule;

public interface CommandItemClickListener {
    void onItemClickListener(SpeechCommand speechCommand, int position, boolean isLongClick);
    void onItemLongClickListener(SpeechCommand speechCommand, int position, boolean isLongClick);
    void onSwitchClickListener(SpeechCommand speechCommand, int position, boolean isChecked);
}
