package com.codboxer.finallayouttest.model;

import android.util.Log;


import com.codboxer.finallayouttest.menum.EWeekday;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Tran Ngoc Man
 * Created 26/03/2021
 */

public class Repeat implements Cloneable {
    public static final int ONE = 0;
    public static final int DAILY = 1;
    public static final int WEEKDAYS = 2;

    // 0 -> 2
    private int id;

    private List<Weekday> weekdays;

    public Repeat() {}

    public Repeat(int id, List<Weekday> weekdays) {
        this.id = id;
        this.weekdays = weekdays;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Weekday> getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(List<Weekday> weekdays) {
        this.weekdays = weekdays;
    }

    @Override
    public String toString() {
        StringBuilder content = new StringBuilder();
        switch (id) {
            case 0: content.append("ONCE");
                    break;

            case 1: content.append("DAILY");
                    break;

            case 2:
                // Notes: see NoteBook (Android Studio/Note) for names of weekday list
                if(weekdays != null && !weekdays.isEmpty()) {
                    int size = weekdays.size();
                    if(size == 7) {     // have full weekday
                        content.append("DAILY");
                    }
                    else if(size > 0) {
                        for (EWeekday eweekday : EWeekday.values()) {
                            for(Weekday weekday: weekdays) {
                                if(String.valueOf(eweekday).equals(weekday.getName())) {
                                    content.append(eweekday.getValue());
                                    content.append(", ");
                                }
                            }
                        }
                        content.deleteCharAt(content.length() - 2);    // delete ", "
                    }
                }
                break;
        }

        return content.toString().trim();
    }

    public Repeat clone() throws CloneNotSupportedException {
        return (Repeat) super.clone();
    }
}
