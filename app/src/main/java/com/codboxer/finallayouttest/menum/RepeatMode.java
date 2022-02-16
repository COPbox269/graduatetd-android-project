package com.codboxer.finallayouttest.menum;

public enum RepeatMode {
    Once(0),
    Daily(1),
    Repeat(2);

    private final int value;

    RepeatMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
