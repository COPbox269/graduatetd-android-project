package com.codboxer.finallayouttest.model;

/**
 * @author Tran Ngoc Man
 * Created 26/03/2021
 */

public class Relay extends ItemData implements Cloneable{
    private int id;  // ex: "relay-1"
    private String name;

    public Relay() {
        super();
    }

    public Relay(int id, String name) {
        super(id, name);
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
    public String toString() {
        return name;
    }

    public Relay clone() throws CloneNotSupportedException {
        return (Relay) super.clone();
    }
}
