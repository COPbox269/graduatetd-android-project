/*
    @author: Tran Ngoc Man

    @return:

    @date:                        @task:                        @bug:
    ...                            ...                          ...

*/

package com.codboxer.finallayouttest.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codboxer.finallayouttest.R;
import com.codboxer.finallayouttest.adapter.ItemAdapter;
import com.codboxer.finallayouttest.model.ItemData;
import com.codboxer.finallayouttest.repository.ItemChangeListener;
import com.codboxer.finallayouttest.ui.myinterface.DismissDialogListener;
import com.codboxer.finallayouttest.ui.myinterface.ICLickListener;
import com.codboxer.finallayouttest.viewmodel.AppViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CheckBoxItemBottomSheetDialog extends BottomSheetDialogFragment {
    private static final String TAG = CheckBoxItemBottomSheetDialog.class.getSimpleName();
    private String mTAG;

    ItemAdapter itemAdapter;
    private AppViewModel appViewModel;

    // This list use for saving selected checkboxes name
    List<ItemData> mCheckedItems;

    DismissDialogListener dismissDialogListener;

    public CheckBoxItemBottomSheetDialog(String TAG) {
        mTAG = TAG;
        mCheckedItems = new ArrayList<>();
    }

    public CheckBoxItemBottomSheetDialog(String TAG, DismissDialogListener dismissDialogListener) {
        mTAG = TAG;
        mCheckedItems = new ArrayList<>();
        this.dismissDialogListener = dismissDialogListener;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dialog_bottomsheet_list_item_checkbox_list, container, false);

        // Mapping
        RecyclerView recyclerViewData = rootView.findViewById(R.id.recycle_view_item);

        // Set up layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewData.setLayoutManager(linearLayoutManager);

        // Set data to adapter
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        appViewModel.getItems(mTAG, new ItemChangeListener() {
                    @Override
                    public void onItemLoaded(List<ItemData> items, List<ItemData> checkedItems) {
                        mCheckedItems = checkedItems;
                        itemAdapter = new ItemAdapter(items, checkedItems, icLickListener);
                        recyclerViewData.setAdapter(itemAdapter);
                    }
                });

                /* Create under line */
//        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
//        recyclerViewData.addItemDecoration(itemDecoration);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set dialog shape
        setStyle(STYLE_NORMAL, R.style.BottomDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        // set selected checkbox to mutableLiveDataSchedule
//        appViewModel.setScheduleInfo(mTAG, mCheckedItems);
        // when list is empty, set default name ...

        // Sort list
        Collections.sort(mCheckedItems);

        // TODO send current checkboxes info
        appViewModel.updateScheduleClone(mTAG, mCheckedItems);

        // Make sure the current checkbox dialog is weekday checkboxes
        if(mTAG == "WeekdayCheckboxes") {
            dismissDialogListener.onHide();
        }
    }

    // Click items
    private ICLickListener icLickListener = new ICLickListener() {
        @Override
        public void clickItem(ItemData itemData, int position, boolean isChecked, int size) {
            if(isChecked) {
                mCheckedItems.add(itemData);
            }
            else {
                // use lambda
                // or mCheckedItems.removeIf(item -> position == item.getId());
                mCheckedItems.removeIf(item -> item.equals(itemData));
            }
        }
    };
}
