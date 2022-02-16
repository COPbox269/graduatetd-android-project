package com.codboxer.finallayouttest.alarm;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Tran Ngoc Man
 * 16/04/2021
 */

public class ScheduleInfoProvider {
    private static final String TAG = ScheduleInfoProvider.class.getSimpleName();

    private static ScheduleInfoProvider instance;

    private int requestCode;

    private List<Integer> allocatedCodes;

//    private List()

    private ScheduleInfoProvider() {
        allocatedCodes = new ArrayList<>();
    }

    public static ScheduleInfoProvider getInstance() {
        if(instance == null) {
            instance = new ScheduleInfoProvider();
        }
        return instance;
    }

    public int generateRequestCode() {
        Random random = new Random();
        int max = 50;
        int min = 1;
        int randomCode;
        do {
            randomCode = random.nextInt((max - min) + 1) + min;    // range [1:50]
        }
        while(allocatedCodes.contains(randomCode)); // the code already exists

        requestCode = randomCode;

        // add new requestCode to list
        allocatedCodes.add(requestCode);

        Log.e(TAG, "allocatedCodes after adding: " + allocatedCodes.toString());

        return requestCode;
    }

    public void retrieveRequestCode(int requestCode) {
        // remove canceled code from list
        allocatedCodes.remove(new Integer(requestCode));

        Log.e(TAG, "allocatedCodes after removing: " + allocatedCodes.toString());
    }

    public void setAllAllocatedRequestCodes(List<Integer> allocatedCodes) {
            this.allocatedCodes.addAll(allocatedCodes);

        Log.e(TAG, "allocatedCodes after sync: " + allocatedCodes.toString());

    }
}
