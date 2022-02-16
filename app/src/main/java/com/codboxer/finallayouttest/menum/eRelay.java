package com.codboxer.finallayouttest.menum;

public enum eRelay {
    Relay_1(0),
    Relay_2(1),
    Relay_3(2),
    Relay_4(3);

    private final int value;

    private eRelay(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
