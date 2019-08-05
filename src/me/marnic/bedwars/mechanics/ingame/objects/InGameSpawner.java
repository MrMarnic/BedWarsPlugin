package me.marnic.bedwars.mechanics.ingame.objects;

import me.marnic.bedwars.mechanics.bwgame.BedWarsSpawner;
import org.bukkit.World;

/*
 * Copyright (c) 05.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

/**
 * In game functionality of a spawner
 */
public class InGameSpawner {
    private BedWarsSpawner spawner;

    public InGameSpawner(BedWarsSpawner spawner) {
        this.spawner = spawner;
    }

    private int time;

    /**
     * Used to update the time of a spawner
     * @param world world it is in
     */
    public void updateSecond(World world) {
        time++;
        if (time == spawner.getSpawnTime()) {
            time = 0;
            world.dropItem(spawner.getSpawnerPos(), spawner.getDrop());
        }
    }
}
