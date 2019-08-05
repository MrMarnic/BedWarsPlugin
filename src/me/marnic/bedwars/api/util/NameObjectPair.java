package me.marnic.bedwars.api.util;

/*
 * Copyright (c) 03.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

/**
 * Just like a Map.Entry
 */
public class NameObjectPair {

    private String name;
    private Object object;


    /**
     *
     * @param name name of the field of the object
     * @param object given object (can be null)
     */
    public NameObjectPair(String name, Object object) {
        this.name = name;
        this.object = object;
    }

    public String getName() {
        return name;
    }

    public Object getObject() {
        return object;
    }
}
