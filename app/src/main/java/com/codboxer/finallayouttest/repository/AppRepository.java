package com.codboxer.finallayouttest.repository;

import androidx.lifecycle.LiveData;

import com.codboxer.finallayouttest.model.Control;
import com.codboxer.finallayouttest.model.ItemData;
import com.codboxer.finallayouttest.model.Relay;
import com.codboxer.finallayouttest.model.TimerSchedule;

import java.util.List;

/**
 *
 * Created by Tran Ngoc Man 26/03/2021
 *
 * @quote Create a Repository Interface  and its implementation class AppRepositoryImpl.java
 *        Method fetchAllSchedule will be called to get all the apps from DAO
 */

public interface AppRepository {
  public LiveData<List<TimerSchedule>> fetchAllSchedules();

  public LiveData<Control> fetchControl();
}
