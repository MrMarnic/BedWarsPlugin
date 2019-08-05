package me.marnic.bedwars.api.util;

import me.marnic.bedwars.main.BedWars;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
 * Copyright (c) 04.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

/**
 * Helper class for world stuff
 */
public class WorldUtil {

    /**
     * Used to backupd world in folder that begins with "backup"
     * @param world world to backup
     */
    public static void backupWorld(World world) {
        File backupWorldFile = new File(world.getWorldFolder().getParent() + "//backup" + world.getWorldFolder().getName());

        if (!backupWorldFile.exists()) {
            try {
                FileUtils.copyDirectory(world.getWorldFolder(), backupWorldFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Formerly used to recreated world
     * @param world world to recreate
     */
    @Deprecated
    public static void recreateWorld(World world) {
        File worldFile = world.getWorldFolder();
        File backupWorldFile = new File(world.getWorldFolder().getParent() + "//backup" + world.getWorldFolder().getName());

        Bukkit.getServer().unloadWorld(world, false);

        if (backupWorldFile.exists()) {
            try {
                FileUtils.deleteDirectory(worldFile);
                FileUtils.copyDirectory(backupWorldFile, worldFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Bukkit.broadcastMessage("World " + world.getName() + " could not be recreated! Backup is missing!");
        }
    }

    /**
     * Used to create save of worlds to load
     * @param list string list of worlds to load on startup
     */
    public static void createSaveFile(List<String> list) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(BedWars.INSTANCE.getDataFolder().getAbsolutePath() + "//worlds.dat")));
            for (String txt : list) {
                writer.write(txt);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns list of worlds to load on startup
     * @return string list of world names
     */
    public static List<String> getWorldsToLoad() {
        ArrayList<String> list = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(BedWars.INSTANCE.getDataFolder().getAbsolutePath() + "//worlds.dat")));
            String s;
            while ((s = reader.readLine()) != null) {
                list.add(s);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
