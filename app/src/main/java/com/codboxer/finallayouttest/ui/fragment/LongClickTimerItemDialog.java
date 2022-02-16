package com.codboxer.finallayouttest.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.codboxer.finallayouttest.R;
import com.codboxer.finallayouttest.viewmodel.AppViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * @author Tran Ngoc Man
 * 14/04/2021
 */

public class  LongClickTimerItemDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    public static final String TAG = LongClickTimerItemDialog.class.getSimpleName();
    public static final String SCHEDULE_DELETE_DIALOG_TAG = "ScheduleDeleteDialog";
    public static final String SPEECH_COMMAND_DELETE_DIALOG_TAG = "SpeechCommandDeleteDialog";

    private TextView textViewDelete;

    private AppViewModel appViewModel;
    private int id; // id of schedule (= position of schedule in schedules)

    public LongClickTimerItemDialog(int id) {
        this.id = id;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set dialog style
        setStyle(STYLE_NORMAL, R.style.BottomDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bottom_sheet_dialog_delete, container, false);

        /* Mapping*/
        textViewDelete = (TextView) rootView.findViewById(R.id.text_view_delete_schedule);
        /* End mapping*/

        /* Click listener*/
        textViewDelete.setOnClickListener(this);
        /* End listener*/

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(getTag() == SCHEDULE_DELETE_DIALOG_TAG) {    // dialog use for deleting schedule
            appViewModel.deleteSchedule(id);
        }
        else if(getTag() == SPEECH_COMMAND_DELETE_DIALOG_TAG) { // dialog use for deleting speech command
            appViewModel.deleteSpeechCommand(id);
        }
        dismiss();
    }
}
