package com.codboxer.finallayouttest.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.codboxer.finallayouttest.R;
import com.codboxer.finallayouttest.model.SpeechCommand;
import com.codboxer.finallayouttest.repository.SpeechCommandLoadListener;
import com.codboxer.finallayouttest.viewmodel.AppViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tran Ngoc Man
 * 07/05/2021
 */
public class CommandSettingDialogFragment extends DialogFragment implements View.OnClickListener {
    public static final String TAG = CommandSettingDialogFragment.class.getSimpleName();
    private final int COMPLETELY = -1;
    private final int MISS_COMMAND = 0;
    private final int MISS_ACTION = 1;


    EditText editTextCommand1;
    EditText editTextCommand2;
    EditText editTextCommand3;

    TextView textViewNameRelay1;
    TextView textViewNameRelay2;
    TextView textViewNameRelay3;
    TextView textViewNameRelay4;

    Spinner spinnerActionRelay1;
    Spinner spinnerActionRelay2;
    Spinner spinnerActionRelay3;
    Spinner spinnerActionRelay4;

    Button buttonCancel;
    Button buttonDone;

    AppViewModel appViewModel;

    boolean isCreation;
    int id;

    // Initialize dialog
    public static CommandSettingDialogFragment newInstance(boolean isCreation, int id) {
        CommandSettingDialogFragment dialog = new CommandSettingDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean("isCreation", isCreation);
        args.putInt("id", id);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        // Set dialog shape
        setStyle(STYLE_NORMAL, R.style.Theme_AppCompat_Light_Dialog_Alert);

        setCancelable(false);
    }


    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        // Corner radius
        getDialog().getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_white_16));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.dialog_control_speech_command_setting, container, false);
        // Get value from Bundle
        isCreation = getArguments().getBoolean("isCreation");
        id = getArguments().getInt("id");

        // Mapping
        editTextCommand1 = (EditText) rootView.findViewById(R.id.edit_text_comand_1);
        editTextCommand2 = (EditText) rootView.findViewById(R.id.edit_text_comand_2);
        editTextCommand3 = (EditText) rootView.findViewById(R.id.edit_text_comand_3);

        textViewNameRelay1 = (TextView) rootView.findViewById(R.id.text_view_relay_1_name);
        textViewNameRelay2 = (TextView) rootView.findViewById(R.id.text_view_relay_2_name);
        textViewNameRelay3 = (TextView) rootView.findViewById(R.id.text_view_relay_3_name);
        textViewNameRelay4 = (TextView) rootView.findViewById(R.id.text_view_relay_4_name);

        spinnerActionRelay1 = (Spinner) rootView.findViewById(R.id.spinner_relay_1_action);
        spinnerActionRelay2 = (Spinner) rootView.findViewById(R.id.spinner_relay_2_action);
        spinnerActionRelay3 = (Spinner) rootView.findViewById(R.id.spinner_relay_3_action);
        spinnerActionRelay4 = (Spinner) rootView.findViewById(R.id.spinner_relay_4_action);

        buttonCancel = (Button) rootView.findViewById(R.id.button_cancel_setting);
        buttonDone = (Button) rootView.findViewById(R.id.button_done_setting);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.actions_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerActionRelay1.setAdapter(adapter);
        spinnerActionRelay2.setAdapter(adapter);
        spinnerActionRelay3.setAdapter(adapter);
        spinnerActionRelay4.setAdapter(adapter);

        // Click Listener Event
        buttonCancel.setOnClickListener(this);
        buttonDone.setOnClickListener(this);

        // Initialize ViewModel
        appViewModel = new ViewModelProvider(getActivity()).get(AppViewModel.class);

        // Click id speech command
        if(!isCreation) {
            appViewModel.getSpeechCommand(isCreation, id, new SpeechCommandLoadListener() {
                @Override
                public void onLoaded(SpeechCommand speechCommand) {
                    int size = speechCommand.getCommands().size();
                    if(size > 0) {
                        editTextCommand1.setText(speechCommand.getCommands().get(0));
                    }
                    if(size > 1) {
                        editTextCommand2.setText(speechCommand.getCommands().get(1));
                    }
                    if(size > 2) {
                        editTextCommand3.setText(speechCommand.getCommands().get(2));
                    }

                    spinnerActionRelay1.setSelection(speechCommand.getActions().get(0));
                    spinnerActionRelay2.setSelection(speechCommand.getActions().get(1));
                    spinnerActionRelay3.setSelection(speechCommand.getActions().get(2));
                    spinnerActionRelay4.setSelection(speechCommand.getActions().get(3));
                }
            });
        }

        // Get all relay names
        List<String> relayNames = new ArrayList<>();
        relayNames.addAll(appViewModel.getRelayNameList());
        // Show on TextView
        textViewNameRelay1.setText(relayNames.get(0).toString().trim());
        textViewNameRelay2.setText(relayNames.get(1).toString().trim());
        textViewNameRelay3.setText(relayNames.get(2).toString().trim());
        textViewNameRelay4.setText(relayNames.get(3).toString().trim());

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Button Click Listener
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
            case R.id.button_cancel_setting: doOnCancel();
                                            break;
            case R.id.button_done_setting: doOnDone();
                                            break;
        }
    }

    /**
     * Called when Button Done is clicked
     */
    private void doOnDone() {
        Log.e("TAG", "spinner 1: " + String.valueOf(spinnerActionRelay1.getSelectedItemPosition()));
        Log.e("TAG", "spinner 2: " + String.valueOf(spinnerActionRelay2.getSelectedItemPosition()));
        Log.e("TAG", "spinner 3: " + String.valueOf(spinnerActionRelay3.getSelectedItemPosition()));
        Log.e("TAG", "spinner 4: " + String.valueOf(spinnerActionRelay4.getSelectedItemPosition()));

        switch(isCompleteInfo()) {
            case COMPLETELY:
                doOnCompleteInfo();
                // Close/Dismiss dialog
                dismiss();
                break;
            case MISS_COMMAND:
                Toast.makeText(getActivity(), "Please check command fields", Toast.LENGTH_SHORT).show();
                break;
            case MISS_ACTION:
                Toast.makeText(getActivity(), "Please choose control actions", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Called when info is full
     */
    private void doOnCompleteInfo() {
        List<String> commands = new ArrayList<>();
        List<Integer> actions = new ArrayList<>();

        // Add no null edit text
        if(!editTextCommand1.getText().toString().trim().isEmpty()) {
            commands.add(editTextCommand1.getText().toString().trim());
        }
        if(!editTextCommand2.getText().toString().trim().isEmpty()) {
            commands.add(editTextCommand2.getText().toString().trim());
        }
        if(!editTextCommand3.getText().toString().trim().isEmpty()) {
            commands.add(editTextCommand3.getText().toString().trim());
        }

        // Add action
        actions.add(spinnerActionRelay1.getSelectedItemPosition());
        actions.add(spinnerActionRelay2.getSelectedItemPosition());
        actions.add(spinnerActionRelay3.getSelectedItemPosition());
        actions.add(spinnerActionRelay4.getSelectedItemPosition());

        SpeechCommand speechCommand = new SpeechCommand(id, true, commands, actions);
        appViewModel.setSpeechCommand(id, speechCommand);
    }

    /**
     * Called when Cancel Button is clicked
     */
    private void doOnCancel() {
        // Close/Dismiss dialog
        dismiss();
    }

    /**
     *
     * @return code indicate complete infor
     */
    private int isCompleteInfo() {
        String command1 = editTextCommand1.getText().toString().trim();
        String command2 = editTextCommand2.getText().toString().trim();
        String command3 = editTextCommand3.getText().toString().trim();

        int relay1Action = spinnerActionRelay1.getSelectedItemPosition();
        int relay2Action = spinnerActionRelay2.getSelectedItemPosition();
        int relay3Action = spinnerActionRelay3.getSelectedItemPosition();
        int relay4Action = spinnerActionRelay4.getSelectedItemPosition();

        if(command1.isEmpty() && command2.isEmpty() && command3.isEmpty()) {
            return MISS_COMMAND;
        }
        else {
            if(relay1Action == SpeechCommand.NONE_STATE_ACTION
                    && relay2Action == SpeechCommand.NONE_STATE_ACTION
                    && relay3Action == SpeechCommand.NONE_STATE_ACTION
                    && relay4Action == SpeechCommand.NONE_STATE_ACTION) {
                return MISS_ACTION;
            }
            else {
                return COMPLETELY;
            }
        }
    }
}
