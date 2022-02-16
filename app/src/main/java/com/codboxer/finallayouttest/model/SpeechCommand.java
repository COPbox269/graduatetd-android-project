package com.codboxer.finallayouttest.model;

import java.util.List;

/**
 * @author Tran Ngoc Man
 * 06/05/2021
 */
public class SpeechCommand {
    public static final int NONE_STATE_ACTION = 0;
    public static final int ON_STATE_ACTION = 1;
    public static final int OFF_STATE_ACTION = 2;

    private int id;
    private boolean isOn;
    private List<String> commands;
    private List<Integer> actions;

    public SpeechCommand() {
    }

    public SpeechCommand(int id, boolean isOn, List<String> commands, List<Integer> actions) {
        this.id = id;
        this.isOn = isOn;
        this.commands = commands;
        this.actions = actions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public List<Integer> getActions() {
        return actions;
    }

    public void setActions(List<Integer> actions) {
        this.actions = actions;
    }

    public String listActionsToString(List<String> relayNames) {
        String content = "";

        int i = 0;
        for(Integer action : actions) {
            if(action != NONE_STATE_ACTION) {
                content += relayNames.get(i);
                if(action == ON_STATE_ACTION) {
                    content += " [ON] ";
                }
                else if(action == OFF_STATE_ACTION) {
                    content += " [OFF] ";
                }
            }
            i++;
        }

        return content.trim();
    }
}
