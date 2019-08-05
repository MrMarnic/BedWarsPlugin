package me.marnic.bedwars.mechanics.bwgame;

import me.marnic.bedwars.api.bwobject.IFinishable;
import me.marnic.bedwars.api.util.NameObjectPair;
import me.marnic.bedwars.api.util.ObjectsUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
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

/**
 * Class for all stuff in a BedWars map
 */
public class BedWarsMap implements IFinishable, ConfigurationSerializable {
    private String name;
    private World gameWorld;
    private World lobbyWorld;
    private Map<ChatColor, BedWarsTeam> teams;
    private Location lobbySpawn;
    private int maxPlayers;
    private int teamPlayers;

    public BedWarsMap() {
        this.teams = new HashMap<>();
    }

    /**
     * Used for deserialization
     * @param map
     */
    public BedWarsMap(Map<String, Object> map) {
        this.name = (String) map.get("name");
        this.gameWorld = Bukkit.getWorld((String) map.get("gameWorld"));
        this.lobbyWorld = Bukkit.getWorld((String) map.get("lobbyWorld"));

        this.lobbySpawn = (Location) map.get("lobbySpawn");
        this.teams = new HashMap<>();
        int teamSize = (int) map.get("teamSize");

        for (int i = 0; i < teamSize; i++) {
            String path = "teams." + i;
            BedWarsTeam team = (BedWarsTeam) map.get(path);
            teams.put(team.getChatColor(), team);
            this.maxPlayers = team.getPlayerCount() * teamSize;
            this.teamPlayers = team.getPlayerCount();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGameWorld(World gameWorld) {
        this.gameWorld = gameWorld;
    }

    public void setLobbyWorld(World lobbyWorld) {
        this.lobbyWorld = lobbyWorld;
    }

    public void setLobbySpawn(Location lobbySpawn) {
        this.lobbySpawn = lobbySpawn;
    }

    /**
     * Returns map of teams
     * @return HashMap of ChatColor and BedWarsTeam
     */
    public Map<ChatColor, BedWarsTeam> getTeams() {
        return teams;
    }

    public String getName() {
        return name;
    }

    public World getGameWorld() {
        return gameWorld;
    }

    public World getLobbyWorld() {
        return lobbyWorld;
    }

    public Location getLobbySpawn() {
        return lobbySpawn;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getTeamPlayers() {
        return teamPlayers;
    }

    @Override
    public boolean isFinished() {
        return ObjectsUtil.nonNull(lobbySpawn, lobbyWorld, gameWorld, name) && ObjectsUtil.checkFinishables(teams.values()) && teams.size() > 1;
    }

    @Override
    public List<String> getNotFinished() {
        ArrayList<String> list = new ArrayList();

        list.add("BedWars Map:");
        list.addAll(ObjectsUtil.nonNullOuts(new NameObjectPair("lobbySpawn", lobbySpawn), new NameObjectPair("lobbyWorld", lobbyWorld), new NameObjectPair("gameWorld", gameWorld), new NameObjectPair("name", name)));
        if (teams.size() < 2) {
            list.add("Team size must be higher than 1");
        }
        list.addAll(ObjectsUtil.checkFinishablesOuts(new NameObjectPair("teams", teams.values())));

        return list;
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("gameWorld", gameWorld.getName());
        map.put("lobbyWorld", lobbyWorld.getName());
        map.put("lobbySpawn", lobbySpawn);
        map.put("teamSize", teams.size());

        int index = 0;
        for (BedWarsTeam team : teams.values()) {
            map.put("teams." + index, team);
            index++;
        }
        return map;
    }
}
