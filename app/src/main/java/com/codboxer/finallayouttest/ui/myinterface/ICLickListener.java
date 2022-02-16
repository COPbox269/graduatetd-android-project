package com.codboxer.finallayouttest.ui.myinterface;

import com.codboxer.finallayouttest.model.ItemData;

/**
 * @author: Tran Ngoc Man
 * @usage: Click event listener for checkboxes RecycleView
 */
public interface ICLickListener {
    void clickItem(ItemData itemData, int position, boolean isChecked, int size);
}
