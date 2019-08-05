package me.marnic.bedwars.api.bwobject;

import java.util.List;

/*
 * Copyright (c) 03.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */


/**
 * The IFinishable interface is used check if a object is fully configured/finished
 */
public interface IFinishable {

    /**
     * This method is used to check if a object is fully configured
     * @return the state if finished or not
     */
    boolean isFinished();

    /**
     * This method is used to get the name of the missing variables
     * @return a string list of the missing variables
     */
    List<String> getNotFinished();
}
