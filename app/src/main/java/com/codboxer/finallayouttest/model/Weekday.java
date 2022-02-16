package com.codboxer.finallayouttest.model;

public class Weekday extends ItemData implements Cloneable{
    private int id;
    private String name;

    public Weekday() {
        super();
    }

    public Weekday(int id, String name) {
        super(id, name);
        this.id = id;
        this.name = name;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Weekday clone() throws CloneNotSupportedException {
        return (Weekday) super.clone();
    }
}
