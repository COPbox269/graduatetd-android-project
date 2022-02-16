package com.codboxer.finallayouttest.repository;

import com.codboxer.finallayouttest.model.Control;
import com.codboxer.finallayouttest.model.Relay;

import java.util.List;

public interface ControlChangeListener {
    public void onStatesChange(List<Boolean> states);
    public void onRelaysChange(List<Relay> relays);
}
