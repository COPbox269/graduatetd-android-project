package com.codboxer.finallayouttest.repository;

import com.codboxer.finallayouttest.model.Relay;

import java.util.List;

public interface RelayOfControlChangeListener {
    public void onRelaysLoaded(List<Relay> relays);
}
