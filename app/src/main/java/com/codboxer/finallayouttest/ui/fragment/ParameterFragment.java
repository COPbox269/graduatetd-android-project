package com.codboxer.finallayouttest.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.codboxer.finallayouttest.R;
import com.codboxer.finallayouttest.model.Sensor;
import com.codboxer.finallayouttest.viewmodel.AppViewModel;

/**
 * @author Tran Ngoc Man
 * 19/05/21
 */
public class ParameterFragment extends Fragment {
    TextView textViewDisplayVoltage;
    TextView textViewDisplayCurrent;
    TextView textViewDisplayPower;
    TextView textViewDisplayEnergy;

    AppViewModel appViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_parameter, container, false);

        textViewDisplayVoltage = (TextView) rootView.findViewById(R.id.text_view_display_voltage);
        textViewDisplayCurrent = (TextView) rootView.findViewById(R.id.text_view_display_current);
        textViewDisplayPower = (TextView) rootView.findViewById(R.id.text_view_display_power);
        textViewDisplayEnergy = (TextView) rootView.findViewById(R.id.text_view_display_energy);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        appViewModel.initSensorData();
        appViewModel.getSensorData().observe(requireActivity(), new Observer<Sensor>() {
            @Override
            public void onChanged(Sensor sensor) {
                textViewDisplayVoltage.setText(String.valueOf(sensor.getVoltage()).trim() + " V");

                textViewDisplayCurrent.setText(String.valueOf(sensor.getCurrent()).trim() + " A");

                textViewDisplayPower.setText(String.valueOf(sensor.getPower()).trim() + " W");

                textViewDisplayEnergy.setText(String.valueOf(sensor.getEnergy()).trim() + " kWh");
            }
        });

        return rootView;
    }
}