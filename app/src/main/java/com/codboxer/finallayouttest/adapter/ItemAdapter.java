package com.codboxer.finallayouttest.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.codboxer.finallayouttest.R;
import com.codboxer.finallayouttest.model.ItemData;
import com.codboxer.finallayouttest.ui.myinterface.ICLickListener;


import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private static final String TAG = ItemAdapter.class.getSimpleName();

    private final List<ItemData> itemDataList;
    private final List<ItemData> checkedItems;
    private final ICLickListener icLickListener;

    public ItemAdapter(List<ItemData> itemDataList, List<ItemData> checkedItems, ICLickListener icLickListener) {
        this.itemDataList = itemDataList;
        this.checkedItems = checkedItems;
        this.icLickListener = icLickListener;
    }

    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_bottom_sheet_item_checkbox, parent, false);

        return new ItemViewHolder(view);
    }

    // Display item of adapter
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        final ItemData itemData = itemDataList.get(position);
        if(itemData == null) {  // ItemList doest have element
            return;
        }

        // Bind data to view holder (display)
        holder.bind(itemData, checkedItems);

        // Click check box listener
        holder.checkBoxItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                icLickListener.clickItem(itemData, position, isChecked, getItemCount());
            }
        });
    }

    // Return the total number of items in data set held by the adapter
    @Override
    public int getItemCount() {
        // Return the number of elements in list
        if(itemDataList != null)
            return itemDataList.size();
        else
            return 0;
    }

    // Class represent item
    public class ItemViewHolder  extends RecyclerView.ViewHolder {
        private final CheckBox checkBoxItem;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            // itemView = rootView in onCreateViewHolder()
            checkBoxItem = (CheckBox) itemView.findViewById(R.id.checkbox_item);
        }

        void bind(ItemData itemData, List<ItemData> checkedItems) { // itemData is list to display, checkedItems id list to set check
            int id = itemData.getId();
            String name = itemData.getName();
            // Set text to check box
            checkBoxItem.setText(name);
            // Set checked
            for(ItemData item: checkedItems) {
                int checkedId = item.getId();
                String checkedName = item.getName();
                if(id == checkedId || name.equals(checkedName)) {
                    checkBoxItem.setChecked(true);
                }
            }
        }
    }
}
