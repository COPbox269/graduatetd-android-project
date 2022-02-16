package com.codboxer.finallayouttest.ui.fragment;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.codboxer.finallayouttest.R;
import com.codboxer.finallayouttest.model.Relay;
import com.codboxer.finallayouttest.repository.ControlChangeListener;
import com.codboxer.finallayouttest.ui.activity.ControlSpeechActivity;
import com.codboxer.finallayouttest.util.ListenerEditText;
import com.codboxer.finallayouttest.viewmodel.AppViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tran Ngoc Man
 */

public class  ControlFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnFocusChangeListener, ListenerEditText.KeyImeChange {
    private static final String TAG = ControlFragment.class.getSimpleName();
    // request code speech input
    private final int REQ_CODE_SPEECH = 100;

    private ListenerEditText editTextRelay1, editTextRelay2, editTextRelay3, editTextRelay4;

    private SwitchCompat switchRelay1, switchRelay2, switchRelay3, switchRelay4;
    // This flag use for allowing update state when check of switch is changed
    // To make sure that checkChangeListener() run after getControl() whenever onCreateView() occur
    // true: can update state to firebase, false: can not
    private boolean FLAG_UPDATE_STATE = false;

    private ImageButton imageButtonMic;

    private AppViewModel appViewModel;

    private EditText focusedEditText;
    private String unmodifiedContent;

    public ControlFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate()");
    }

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_control, container, false);

        editTextRelay1 = (ListenerEditText) rootView.findViewById(R.id.edit_text_name_1_relay_1);
        editTextRelay2 = (ListenerEditText) rootView.findViewById(R.id.edit_text_name_2_relay_2);
        editTextRelay3 = (ListenerEditText) rootView.findViewById(R.id.edit_text_name_3_relay_3);
        editTextRelay4 = (ListenerEditText) rootView.findViewById(R.id.edit_text_name_4_relay_4);

        switchRelay1 = (SwitchCompat) rootView.findViewById(R.id.switch_1_control_relay_1);
        switchRelay2 = (SwitchCompat) rootView.findViewById(R.id.switch_2_control_relay_2);
        switchRelay3 = (SwitchCompat) rootView.findViewById(R.id.switch_3_control_relay_3);
        switchRelay4 = (SwitchCompat) rootView.findViewById(R.id.switch_4_control_relay_4);

        imageButtonMic = (ImageButton) rootView.findViewById(R.id.image_button_mic);

        Log.e(TAG, "onCreateView()");

        // Initialize View Model
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        appViewModel.getControl(listener);    // sync UI below

//        editTextRelay1.setOnKeyListener(this);
//        editTextRelay2.setOnKeyListener(this);
//        editTextRelay3.setOnKeyListener(this);
//        editTextRelay4.setOnKeyListener(this);

        editTextRelay1.setOnFocusChangeListener(this);
        editTextRelay2.setOnFocusChangeListener(this);
        editTextRelay3.setOnFocusChangeListener(this);
        editTextRelay4.setOnFocusChangeListener(this);

        // magic usage for soft keyboard
        editTextRelay1.setOnKeyImeChangeListener(this);
        editTextRelay2.setOnKeyImeChangeListener(this);
        editTextRelay3.setOnKeyImeChangeListener(this);
        editTextRelay4.setOnKeyImeChangeListener(this);


        switchRelay1.setOnCheckedChangeListener(this);
        switchRelay2.setOnCheckedChangeListener(this);
        switchRelay3.setOnCheckedChangeListener(this);
        switchRelay4.setOnCheckedChangeListener(this);

        imageButtonMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askSpeechInput();
            }
        });

        imageButtonMic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openControlSpeechInputSetting();

                return true;
            }
        });

        return rootView;
    }

    private void askSpeechInput() {
        // check the phone support speech recognition
        if(!SpeechRecognizer.isRecognitionAvailable(getActivity())) {
            Toast.makeText(getActivity(), "Speech recognition is not available", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
            intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 500);
            intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 500);
            intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 1500);
            try {
                startActivityForResult(intent, REQ_CODE_SPEECH);
            }
            catch(ActivityNotFoundException a) {
                Toast.makeText(getActivity(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openControlSpeechInputSetting() {
        Intent intent = new Intent(getActivity(), ControlSpeechActivity.class);
        startActivity(intent);
    }

    /**
     * Receive speech input
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case REQ_CODE_SPEECH:
                if(resultCode == -1  && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.e("TAG", result.get(0));

                    doOnSpeechInputResult(result.get(0));
                }
                break;
        }
    }

    private void doOnSpeechInputResult(String speechInputResult) {
        appViewModel.speechCommandMatches(speechInputResult);
    }


    private ControlChangeListener listener = new ControlChangeListener() {
        @Override
        public void onStatesChange(List<Boolean> states) {
            Log.e(TAG, "update state");
            switchRelay1.setChecked(states.get(0));
            switchRelay2.setChecked(states.get(1));
            switchRelay3.setChecked(states.get(2));
            switchRelay4.setChecked(states.get(3));
        }

        @Override
        public void onRelaysChange(List<Relay> relays) {
            Log.e(TAG, "sync relay");
            editTextRelay1.setText(relays.get(0).getName());
            editTextRelay2.setText(relays.get(1).getName());
            editTextRelay3.setText(relays.get(2).getName());
            editTextRelay4.setText(relays.get(3).getName());
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume()");
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if(buttonView.isPressed()) {    // make sure user click listener, block setCheck()
            Log.e(TAG, "user check switch");

            doOnRelayControl(id, isChecked);

            Log.e(TAG, "doOn");
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void doOnRelayControl(int id, boolean isChecked) {
        switch(id) {
            case R.id.switch_1_control_relay_1:
                appViewModel.updateStatesInfo(0, isChecked);
                break;
            case R.id.switch_2_control_relay_2:
                appViewModel.updateStatesInfo(1, isChecked);
                break;
            case R.id.switch_3_control_relay_3:
                appViewModel.updateStatesInfo(2, isChecked);
                break;
            case R.id.switch_4_control_relay_4:
                appViewModel.updateStatesInfo(3, isChecked);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + id);
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if(!hasFocus) {     // view is not focused
            closeKeyboard(view);
            doOnEditTextSetAction(focusedEditText);

            switchRelay1.setVisibility(View.VISIBLE);
            switchRelay2.setVisibility(View.VISIBLE);
            switchRelay3.setVisibility(View.VISIBLE);
            switchRelay4.setVisibility(View.VISIBLE);
        }
        else {  // view is focusing
            switchRelay1.setVisibility(View.INVISIBLE);
            switchRelay2.setVisibility(View.INVISIBLE);
            switchRelay3.setVisibility(View.INVISIBLE);
            switchRelay4.setVisibility(View.INVISIBLE);

            // save current focused edit text, when it is not focusable, its text handle
            focusedEditText = (EditText) getActivity().getCurrentFocus();
            // save current content
            unmodifiedContent = focusedEditText.getText().toString().trim();
        }
    }

    @Override
    public boolean onKeyIme(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            /* TODO handle when soft back key is clicked */
            clearFocus();
            return true;
        }
        return false;
    }

    @Override
    public boolean onEditorAction(int actionCode) {
        if(actionCode == EditorInfo.IME_ACTION_DONE) {
            /* TODO handle when enter key is clicked */
            clearFocus();

            return true;
        }

        return false;
    }

    private EditText clearFocus() {
//      close soft keyboard
//      this will give us the view
//      which is currently focus
//      in this layout
        View v = getActivity().getCurrentFocus();
        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if(v != null) {
            v.clearFocus();
        }
        return (EditText) v;
    }

    private void closeKeyboard(View view)
    {
        InputMethodManager manager = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void doOnEditTextSetAction(EditText editText) {
        String name = editText.getText().toString().trim();

        if(name.equals("")) {  // content of edit text is empty
            //don't save current content of focusedEditText
            // set content before focusing
            editText.setText(unmodifiedContent);
        }
        else {
            switch (editText.getId()) {
                case R.id.edit_text_name_1_relay_1:
                    appViewModel.updateRelaysInfoFromControl(0, name);
                    break;

                case R.id.edit_text_name_2_relay_2:
                    appViewModel.updateRelaysInfoFromControl(1, name);
                    break;

                case R.id.edit_text_name_3_relay_3:
                    appViewModel.updateRelaysInfoFromControl(2, name);
                    break;

                case R.id.edit_text_name_4_relay_4:
                    appViewModel.updateRelaysInfoFromControl(3, name);
                    break;
            }
        }
    }
}