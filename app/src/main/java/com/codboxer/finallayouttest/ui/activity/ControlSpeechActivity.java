package com.codboxer.finallayouttest.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codboxer.finallayouttest.R;
import com.codboxer.finallayouttest.adapter.SpeechCommandAdapter;
import com.codboxer.finallayouttest.model.SpeechCommand;
import com.codboxer.finallayouttest.ui.fragment.CommandSettingDialogFragment;
import com.codboxer.finallayouttest.ui.fragment.LongClickTimerItemDialog;
import com.codboxer.finallayouttest.ui.fragment.TimerFragment;
import com.codboxer.finallayouttest.ui.myinterface.CommandItemClickListener;
import com.codboxer.finallayouttest.viewmodel.AppViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tran Ngoc Man
 * 06/05/2021
 */
public class ControlSpeechActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = ControlSpeechActivity.class.getSimpleName();

    private RecyclerView recyclerViewSpeedCommand;
    private FloatingActionButton fab;

    private AppViewModel appViewModel;

    private SpeechCommandAdapter speechCommandAdapter;

    // List use for Text View display action (combine with action state toString)
    // see on speech command adapter
    List<String> relayNames;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_speech);

        // Mapping
        recyclerViewSpeedCommand = (RecyclerView) findViewById(R.id.recycle_view_list_control_speech);
        fab = (FloatingActionButton) findViewById(R.id.fab_add_control_speech);

        // Click listener
        fab.setOnClickListener(this);

        // Set up recyclerview layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewSpeedCommand.setLayoutManager(linearLayoutManager);

        // Get all speechCommands from firebase and fill it to adapter
        appViewModel = new ViewModelProvider(ControlSpeechActivity.this).get(AppViewModel.class);
        appViewModel.initSpeechCommands();
        relayNames = new ArrayList<>();
        relayNames.addAll(appViewModel.getRelayNameList());
        appViewModel.getAllSpeechCommands().observe(this, new Observer<List<SpeechCommand>>() {
            @Override
            public void onChanged(List<SpeechCommand> speechCommands) {
                speechCommandAdapter = new SpeechCommandAdapter(speechCommands, relayNames, itemClickListener);
                recyclerViewSpeedCommand.setAdapter(speechCommandAdapter);
            }
        });
    }

    /**
     * Floating Action Button Click Listener
     * @param v
     */
    @Override
    public void onClick(View v) {
        showSettingDialog(true, speechCommandAdapter.getItemCount());
    }

    /**
     * Item Click Listener
     */
    private CommandItemClickListener itemClickListener = new CommandItemClickListener() {
        @Override
        public void onItemClickListener(SpeechCommand speechCommand, int position, boolean isLongClick) {
            showSettingDialog(false, position);
        }

        @Override
        public void onItemLongClickListener(SpeechCommand speechCommand, int position, boolean isLongClick) {
            // TODO delete SpeechCommand
            openLongClickItemDialog(position);
        }

        @Override
        public void onSwitchClickListener(SpeechCommand speechCommand, int position, boolean isChecked) {
            // TODO update isON status of clicked speech command
            appViewModel.updateSpeechCommandInfo(position, isChecked);
        }
    };

    /**
     * Click Listener Event at id speech command
     * @param isCreation
     * @param id
     */
    private void showSettingDialog(boolean isCreation, int id) {
        // Show and pass value to Dialog
        FragmentManager fragmentManager = getSupportFragmentManager();
        CommandSettingDialogFragment dialog = CommandSettingDialogFragment.newInstance(isCreation, id);
        dialog.show(fragmentManager, CommandSettingDialogFragment.TAG);
    }

    /**
     * Long Click Listener Event at id speech command
     * @param id
     */
    private void openLongClickItemDialog(int id) {
        LongClickTimerItemDialog dialog = new LongClickTimerItemDialog(id);
        dialog.show(getSupportFragmentManager(), LongClickTimerItemDialog.SPEECH_COMMAND_DELETE_DIALOG_TAG);
    }
}
