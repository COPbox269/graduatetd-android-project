package com.codboxer.finallayouttest.repository;

import com.codboxer.finallayouttest.model.ItemData;

import java.util.List;
import java.util.TreeMap;

public interface ItemChangeListener {
    void onItemLoaded(List<ItemData> items, List<ItemData> checkedItems);
}
