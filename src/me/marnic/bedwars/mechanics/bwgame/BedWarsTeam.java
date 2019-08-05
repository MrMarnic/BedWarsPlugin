package me.marnic.bedwars.mechanics.bwgame;

import me.marnic.bedwars.api.bwobject.IFinishable;
import me.marnic.bedwars.api.util.NameObjectPair;
import me.marnic.bedwars.api.util.ObjectsUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Copyright (c) 03.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BedWarsTeam implements IFinishable, ConfigurationSerializable {
    private ChatColor chatColor;
    private int playerCount;
    private Location bedBlockPos;
    private Location bedBlockPos2;
    private Location spawnLocation;
    private List<BedWarsSpawner> spawners;

    public BedWarsTeam() {
        this.spawners = new ArrayList<>();
    }

    public BedWarsTeam(Map<String, Object> map) {
        this.chatColor = ChatColor.valueOf((String) map.get("chatColor"));
        this.playerCount = (int) map.get("playerCount");
        this.bedBlockPos = (Location) map.get("bedBlockPos");
        this.bedBlockPos2 = (Location) map.get("bedBlockPos2");
        this.spawnLocation = (Location) map.get("spawnLocation");
        this.spawners = new ArrayList<>();

        int size = (int) map.get("spawnerSize");

        for (int i = 0; i < size; i++) {
            BedWarsSpawner spawner = (BedWarsSpawner) map.get("spawners." + i);
            spawner.setTeam(this);
            spawners.add(spawner);
        }
    }

    public void setChatColor(ChatColor chatColor) {
        this.chatColor = chatColor;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public void setBedBlockPos(Location bedBlockPos) {
        this.bedBlockPos = bedBlockPos;
    }

    public void setBedBlockPos2(Location bedBlockPos2) {
        this.bedBlockPos2 = bedBlockPos2;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public Location getBedBlockPos() {
        return bedBlockPos;
    }

    public Location getBedBlockPos2() {
        return bedBlockPos2;
    }

    public List<BedWarsSpawner> getSpawners() {
        return spawners;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    @Override
    public boolean isFinished() {
        return ObjectsUtil.nonNull(spawnLocation, chatColor, bedBlockPos) && ObjectsUtil.checkFinishables(spawners) && playerCount > 0;
    }

    @Override
    public List<String> getNotFinished() {
        ArrayList<String> list = new ArrayList<>();
        list.add("BedWars Team " + chatColor.name() + ":");
        if (playerCount < 1) {
            list.add("playerCount must be higher than 0");
        }
        list.addAll(ObjectsUtil.nonNullOuts(new NameObjectPair("spawnLocation", spawnLocation), new NameObjectPair("chatColor", chatColor), new NameObjectPair("bedBlockPos", bedBlockPos)));
        list.addAll(ObjectsUtil.checkFinishablesOuts(new NameObjectPair("spawners", spawners)));
        return list;
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("spawnLocation", spawnLocation);
        map.put("chatColor", chatColor.name());
        map.put("playerCount", playerCount);
        map.put("bedBlockPos", bedBlockPos);
        map.put("bedBlockPos", bedBlockPos2);

        int size = spawners.size();
        int index = 0;

        map.put("spawnerSize", size);

        for (BedWarsSpawner spawner : spawners) {
            map.put("spawners." + index, spawner);
            index++;
        }

        return map;
    }
}
