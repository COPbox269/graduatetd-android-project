package com.codboxer.finallayouttest.model;

import androidx.annotation.Nullable;

import java.util.Collections;

public class ItemData implements Comparable<ItemData>, Cloneable {
    int id;
    private String name;

    public ItemData() {}

    public ItemData(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return this.getId() == ((ItemData) obj).getId();
    }

    @Override
    public int compareTo(ItemData compareItem) {
        int compareId = compareItem.getId();
        // ascending order
        return this.getId() - compareId;

        // descending order
        //return compareId - this.getId();
    }

    public ItemData clone() throws CloneNotSupportedException {
        return (ItemData) super.clone();
    }
}
