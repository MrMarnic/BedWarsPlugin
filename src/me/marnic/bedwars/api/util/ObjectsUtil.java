package me.marnic.bedwars.api.util;

import me.marnic.bedwars.api.bwobject.IFinishable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*
 * Copyright (c) 03.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

/**
 * Util class for Objects
 */
public class ObjectsUtil {

    /**
     * Returns true if all objs in list are not null
     * @param objs list of objects to check
     * @return true if all objs are not null
     */
    public static boolean nonNull(Object... objs) {
        for (Object obj : objs) {
            if (obj == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns null objects
     * @param objs list of NameObjectPair objects to check
     * @return string list(field names) of null objects
     */
    public static List<String> nonNullOuts(NameObjectPair... objs) {

        ArrayList<String> list = new ArrayList<>();

        for (NameObjectPair obj : objs) {
            if (obj.getObject() == null) {
                list.add(obj.getName());
            }
        }
        return list;
    }

    /**
     * Used to check if objects in collections are finished
     * @param finishables list of IFinishable
     * @param <T> class that implements IFinishable
     * @return true if all finishables are finished
     */
    public static <T extends IFinishable> boolean checkFinishables(Collection<T> finishables) {
        if (finishables.size() > 0) {
            for (IFinishable iFinishable : finishables) {
                if (!iFinishable.isFinished()) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * Returns objects that are not finished
     * @param pair NameObjectPair that has a collection of NameObjectPairs as object
     * @param <T> not needed
     * @return list of field names of not finished objects
     */
    public static <T extends IFinishable> List<String> checkFinishablesOuts(NameObjectPair pair) {

        Collection<IFinishable> finishables = (Collection<IFinishable>) pair.getObject();

        ArrayList<String> list = new ArrayList<>();
        if (finishables.size() > 0) {
            for (IFinishable iFinishable : finishables) {
                if (!iFinishable.isFinished()) {
                    list.addAll(iFinishable.getNotFinished());
                }
            }
        } else {
            list.add(pair.getName());
            return list;
        }
        return list;
    }
}
