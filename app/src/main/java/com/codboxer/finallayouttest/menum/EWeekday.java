package com.codboxer.finallayouttest.menum;

public enum EWeekday {
    Monday("MON"),
    Tuesday("TUE"),
    Wednesday("WED"),
    Thursday("THUR"),
    Friday("FRI"),
    Saturday("SAT"),
    Sunday("SUN");

    private final String value;

    EWeekday(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
