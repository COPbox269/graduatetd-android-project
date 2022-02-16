package com.codboxer.finallayouttest.model;

import java.util.List;

public class Control {
    private List<Relay> relays;
    private List<Boolean> states;

    public Control() {}

    public Control(List<Relay> relays, List<Boolean> states) {
        this.relays = relays;
        this.states = states;
    }

    public List<Relay> getRelays() {
        return relays;
    }

    public void setRelays(List<Relay> relays) {
        this.relays = relays;
    }

    public List<Boolean> getStates() {
        return states;
    }

    public void setStates(List<Boolean> states) {
        this.states = states;
    }
}
