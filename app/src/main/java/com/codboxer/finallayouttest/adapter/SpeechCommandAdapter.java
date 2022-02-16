package com.codboxer.finallayouttest.adapter;

import android.annotation.SuppressLint;
import android.speech.SpeechRecognizer;
import android.util.Log;
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
import com.codboxer.finallayouttest.model.SpeechCommand;
import com.codboxer.finallayouttest.repository.ItemChangeListener;
import com.codboxer.finallayouttest.ui.myinterface.CommandItemClickListener;
import com.codboxer.finallayouttest.ui.myinterface.ItemClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * @author Tran Ngoc Man
 * 06/05/2021
 */
public class SpeechCommandAdapter extends RecyclerView.Adapter<SpeechCommandAdapter.SpeechCommandAdapterViewHolder> {
    private List<SpeechCommand> speechCommands;
    private List<String> relayNames;
    private CommandItemClickListener itemClickListener;

    public SpeechCommandAdapter(List<SpeechCommand> speechCommands, List<String> relayNames, CommandItemClickListener itemClickListener) {
        this.speechCommands = speechCommands;
        this.relayNames = relayNames;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public SpeechCommandAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_control_speech_command, parent, false);

        return new SpeechCommandAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpeechCommandAdapterViewHolder holder, int position) {
        SpeechCommand speechCommand = speechCommands.get(position);
        if(speechCommand == null)
            return;

        holder.bind(speechCommand, relayNames);

        // Click Event listener
        holder.relativeLayoutControlCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClickListener(speechCommand, position, false);
            }
        });

        holder.relativeLayoutControlCommand.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemClickListener.onItemLongClickListener(speechCommand, position, true);
                return true;
            }
        });

        holder.switchCommandState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                itemClickListener.onSwitchClickListener(speechCommand, position, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(speechCommands == null || speechCommands.isEmpty()) {
            return 0;
        }
        else {
            return speechCommands.size();
        }
    }

    public class SpeechCommandAdapterViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout relativeLayoutControlCommand;
        private TextView textViewContent;
        private TextView textViewAction;
        private SwitchCompat switchCommandState;

        public SpeechCommandAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            // Mapping
            relativeLayoutControlCommand = (RelativeLayout) itemView.findViewById(R.id.relative_layout_speech_command_item);
            textViewContent = (TextView) itemView.findViewById(R.id.text_view_display_command_content);
            textViewAction = (TextView) itemView.findViewById(R.id.text_view_display_relay_control_action);
            switchCommandState = (SwitchCompat) itemView.findViewById(R.id.switch_speech_command);
        }

        void bind(SpeechCommand speechCommand, List<String> relayNames) {
            textViewContent.setText(speechCommand.getCommands().toString().trim());

            textViewAction.setText(speechCommand.listActionsToString(relayNames));
            switchCommandState.setChecked(speechCommand.isOn());
        }
    }
}
